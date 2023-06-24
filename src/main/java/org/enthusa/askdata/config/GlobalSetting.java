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
}
