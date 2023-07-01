package org.enthusa.askdata.gpt;

import org.enthusa.askdata.AbstractTest;
import org.enthusa.askdata.ext.inscode.GptClient;
import org.enthusa.askdata.ext.inscode.GptRequest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author henry
 * @date 2023/7/1
 */
public class ChatGptTest extends AbstractTest {
    @Resource
    private GptClient gptClient;

    @Test
    public void test() throws InterruptedException {
        GptRequest request = GptRequest.newRequest("Translate natural language to SQL queries.");
        request.addUserMsg("你能做什么?");
        request.setApiKey(System.getenv("INSCODE_API_KEY"));
        System.out.println(gptClient.chatCompletion(request));
    }
}
