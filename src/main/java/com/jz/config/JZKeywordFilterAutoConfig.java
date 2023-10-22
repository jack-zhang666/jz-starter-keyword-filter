package com.jz.config;

import com.jz.util.JZKeywordFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/21 20:13
 */
@EnableConfigurationProperties(JZKeywordProperties.class)
public class JZKeywordFilterAutoConfig {

    @Autowired
    JZKeywordProperties jzKeywordProperties;

    @Autowired
    ResourceLoader resourceLoader;

    int matchType = 1;

    /**
     * 加载敏感词列表
     */
    @PostConstruct
    public void initSentsitive(){
        Resource resource = resourceLoader.getResource(jzKeywordProperties.getFile());
        String path = null;

        try {
            path = resource.getFile().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JZKeywordFilterUtil.loadWordFromFile(path);
    }

    /**
     * 提供进行敏感词转换的方法
     * 可以写成接口，提供多个转换的方法
     */
    public String replaceWord(String keyword, String replaceWord){
        return JZKeywordFilterUtil.replaceSensitiveWord(keyword,matchType,replaceWord);
    }

}
