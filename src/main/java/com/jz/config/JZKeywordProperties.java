package com.jz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/22 11:30
 */
@Data
@ConfigurationProperties(prefix = "sensitive")
public class JZKeywordProperties {

    private String file;

}
