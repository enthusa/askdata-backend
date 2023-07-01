package org.enthusa.askdata.config;

import org.enthusa.askdata.ext.inscode.GptClient;
import org.enthusa.avatar.ext.openai.OpenAiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author henry
 * @date 2023/7/1
 */
@Configuration
public class WebConfig {
    @Resource
    private GlobalSetting globalSetting;

    @Bean
    public GptClient gptClient() {
        return GptClient.builder().build();
    }

    @Bean
    public OpenAiClient openAiClient() {
        return OpenAiClient.builder()
                .apiKey(globalSetting.getOpenAiKey())
                .apiHost(globalSetting.getOpenAiHost())
                .build();
    }
}
