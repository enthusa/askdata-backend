package org.enthusa.askdata.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author henry
 * @date 2023/6/24
 */
@Data
@Component
@Configuration
public class GlobalSetting {
    @Value("${settings.env}")
    private String env;

    @Value("${settings.openai.host}")
    private String openAiHost;

    @Value("${settings.openai.key}")
    private String openAiKey;

    @Value("${settings.mysql.host}")
    private String dbHost;

    @Value("${settings.mysql.port}")
    private String dbPort;

    @Value("${settings.mysql.name}")
    private String dbName;

    @Value("${settings.mysql.user}")
    private String dbUsername;

    @Value("${settings.mysql.pass}")
    private String dbPassword;
}
