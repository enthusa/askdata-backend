package org.enthusa.askdata.controller;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.enthusa.askdata.entity.BiDataSource;
import org.enthusa.askdata.entity.BiField;
import org.enthusa.askdata.entity.BiTable;
import org.enthusa.askdata.ext.inscode.GptClient;
import org.enthusa.askdata.ext.inscode.GptRequest;
import org.enthusa.askdata.mapper.BiDataSourceMapper;
import org.enthusa.askdata.mapper.BiFieldMapper;
import org.enthusa.askdata.mapper.BiTableMapper;
import org.enthusa.avatar.core.utils.RegexUtil;
import org.enthusa.avatar.face.type.PageModel;
import org.enthusa.avatar.face.type.Result;
import org.enthusa.avatar.face.utils.ResultUtil;
import org.enthusa.avatar.face.utils.Validate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author henry
 * @date 2023/7/1
 */
@Slf4j
@RestController
@RequestMapping("/api/bi/venus")
public class BiVenusController {
    private static final Pattern LIMIT_PATTERN = Pattern.compile("limit\\s*\\d+[\\s\\d,;]*$");

    @Resource
    private BiDataSourceMapper biDataSourceMapper;

    @Resource
    private BiTableMapper biTableMapper;

    @Resource
    private BiFieldMapper biFieldMapper;

    @Resource
    private GptClient gptClient;

    @PostMapping("/executeQuery")
    public Result executeQuery(@RequestBody QueryVO queryVO) throws SQLException {
        // Todo: 权限控制
        if (StringUtils.containsAny(queryVO.getSql(), "pwd", "password", "email", "phone")) {
            return ResultUtil.error(110, "包含敏感字段, 请修改 SQL 后再查询");
        }

        // Todo: 改写 SQL 还有很多细节, 比如分区检测
        log.info("==> 改写前: {}", queryVO);
        String sql = queryVO.getSql();
        if (!RegexUtil.hasMatch(LIMIT_PATTERN, sql)) {
            sql = sql.replaceFirst(";$", "") + " limit 1000";
        }
        log.info("==> 改写后: {}", sql);

        // 数据源, 优先选数据表关联的数据源
        Integer dsId = ObjectUtils.defaultIfNull(queryVO.getDs(), 1);
        if (!CollectionUtils.isEmpty(queryVO.getTableIds())) {
            Integer tableId = queryVO.getTableIds().get(0);
            BiTable biTable = biTableMapper.selectByPrimaryKey(tableId);
            dsId = biTable.getDsId();
        }

        QueryRunner qr = new QueryRunner();
        Map<String, Object> model = new HashMap<>();
        try (Connection conn = getConnection(dsId)) {
            long begin = System.currentTimeMillis();
            List<Map<String, Object>> data = qr.query(conn, sql, new MapListHandler());
            long end = System.currentTimeMillis();
            int elapsed = (int) (end - begin);
            model.put("elapsed", elapsed);
            if (CollectionUtils.isEmpty(data)) {
                return ResultUtil.success(model);
            }
            model.put("propList", data.get(0).keySet());
            model.put("data", data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ResultUtil.success(model);
    }

    private Connection getConnection(Integer dsId) throws SQLException {
        BiDataSource ds = biDataSourceMapper.selectByPrimaryKey(dsId);
        byte[] bytes = Base64.getDecoder().decode(ds.getDetails());
        Properties info = JSON.parseObject(new String(bytes), Properties.class);
        return DriverManager.getConnection(info.getProperty("url"), info);

    }

    @PostMapping("/sqlTranslate")
    public Result sqlTranslate(@RequestBody QueryVO queryVO) {
        queryVO.setTableIds(Arrays.asList(1, 2, 3));
        Validate.isEmpty("查询数据表", queryVO.getTableIds());
        long begin = System.currentTimeMillis();
        List<BiTable> tableList = biTableMapper.selectByIds(queryVO.getTableIds());
        Integer dsId = tableList.get(0).getDsId();
        List<String> tablesAndColumn = queryVO.getTablesAndColumn();
        StringBuilder sb = new StringBuilder();
        if (Objects.equals(dsId, 2)) {
            sb.append("### Hive SQL tables, with their properties:\n");
        } else {
            sb.append("### MySQL tables, with their properties:\n");
        }
        sb.append("#\n");
        for (BiTable table : tableList) {
            sb.append(generateTableSchema(table, tablesAndColumn));
        }
        sb.append("#\n");
        sb.append(String.format("### %s\n", queryVO.getQuestion()));
        sb.append("SELECT\n");
        String userMsg = sb.toString();
        log.info("User msg: {}", userMsg);

        GptRequest completion = GptRequest.newRequest("Translate natural language to SQL queries.");
        completion.addUserMsg(userMsg);
        completion.setApiKey(System.getenv("INSCODE_API_KEY"));
        String reply = gptClient.chatCompletion(completion);
        String sql = String.format("SELECT %s", reply);
        String res = sqlFormatFromDsAndSql(dsId, sql);
        long end = System.currentTimeMillis();
        Map<String, Object> model = new HashMap<>();
        model.put("sql", res);
        model.put("elapsed", end - begin);
        return ResultUtil.success(model);
    }

    private String generateTableSchema(BiTable table, List<String> tablesAndColumn) {
        String fieldNames;
        if (!CollectionUtils.isEmpty(tablesAndColumn)) {
            fieldNames = tablesAndColumn.stream().filter(t -> table.getId().equals(Integer.valueOf(t.split("\\.")[0]))).map(t -> t.split("\\.")[1]).collect(Collectors.joining(", "));
        } else {
            PageModel<BiField> pageModel = new PageModel<>();
            pageModel.addCondition("table_id=?", table.getId());
            pageModel.setOrders("column_seq");
            List<BiField> fields = biFieldMapper.selectByPage(pageModel);
            fieldNames = fields.stream().map(BiField::getName).collect(Collectors.joining(", "));
        }
        return String.format("# %s.%s(%s)\n", table.getCatalog(), table.getName(), fieldNames);
    }

    private String sqlFormatFromDsAndSql(Integer ds, String sql) {
        if (ds != 1 && ds != 2) {
            return sql;
        }
        return Objects.equals(2, ds) ? SQLUtils.formatOdps(sql, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION) : SQLUtils.formatMySql(sql, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
    }

    @Data
    public static class QueryVO {
        private List<Integer> tableIds;
        private List<String> tablesAndColumn;
        private String question;
        private Integer ds;
        private String sql;
    }
}
