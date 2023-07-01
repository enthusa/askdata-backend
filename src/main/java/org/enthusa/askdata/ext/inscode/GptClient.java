package org.enthusa.askdata.ext.inscode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.enthusa.avatar.core.consts.TextConstant;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author henry
 * @date 2023/7/1
 */
@Slf4j
public class GptClient {
    private OkHttpClient httpClient;

    private GptClient(Builder builder) {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        public GptClient build() {
            return new GptClient(this);
        }
    }

    public String chatCompletion(GptRequest completion) {
        String json = JSON.toJSONString(completion);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url("https://inscode-api.csdn.net/api/v1/gpt/")
                .post(requestBody)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.ACCEPT_CHARSET, "utf-8")
                .header(HttpHeaders.CONNECTION, "Keep-Alive")
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String text = response.body().string();
                return TextConstant.LINE_SPLITTER.splitToStream(text).map(data -> data.substring(5)).filter(data -> !"[DONE]".equals(data)).map(data -> {
                    JSONObject obj = JSON.parseObject(data);
                    JSONObject delta = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("delta");
                    return delta.getString("content");
                }).filter(Objects::nonNull).collect(Collectors.joining());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
