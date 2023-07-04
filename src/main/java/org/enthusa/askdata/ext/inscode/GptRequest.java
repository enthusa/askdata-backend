package org.enthusa.askdata.ext.inscode;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author henry
 * @date 2023/7/1
 */
@Data
public class GptRequest {
    private enum Role {
        System, Assistant, User
    }

    @Getter
    private List<Message> messages;

    /**
     * 在0和2之间使用什么采样温度？较高的值如0.8会使输出更随机，而较低的值如0.2会使其更加集中和确定性。
     * 默认值: 1
     */
    @Setter
    @Getter
    private Double temperature;

    @Setter
    @Getter
    private List<String> stop;

    @JsonProperty("apikey")
    @JSONField(name = "apikey")
    private String apiKey;

    private GptRequest() {
        messages = new ArrayList<>();
    }

    private void addQueryWithRole(Role role, String query) {
        Message msg = new Message();
        msg.setRole(role.name().toLowerCase());
        msg.setContent(query);
        messages.add(msg);
    }

    public void addUserMsg(String msg) {
        addQueryWithRole(Role.User, msg);
    }

    public void addAssistantMsg(String msg) {
        addQueryWithRole(Role.Assistant, msg);
    }

    public static GptRequest newRequest(String systemDesc) {
        GptRequest request = new GptRequest();
        request.addQueryWithRole(Role.System, systemDesc);
        return request;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
