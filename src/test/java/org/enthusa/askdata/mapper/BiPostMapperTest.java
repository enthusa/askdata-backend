package org.enthusa.askdata.mapper;

import org.enthusa.askdata.AbstractTest;
import org.enthusa.askdata.entity.BiPost;
import org.enthusa.avatar.face.type.PageModel;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author henry
 * @date 2023/6/24
 */
public class BiPostMapperTest extends AbstractTest {
    @Resource
    private BiPostMapper biPostMapper;

    @Test
    public void test() {
        PageModel<BiPost> pageModel = new PageModel<>();
        pageModel.setOrders("id");
        biPostMapper.selectByPage(pageModel).forEach(System.out::println);

    }
}
