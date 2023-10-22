package com.jz;

import com.jz.util.JZKeywordFilterUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Set;

@SpringBootTest
class JzStarterKeywordFilterApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void sentiveWord(){
        Resource resource = resourceLoader.getResource("classpath:sentive-word.txt");

        String path = null;

        try {
            path = resource.getFile().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JZKeywordFilterUtil.loadWordFromFile(path);

        String s = "你好，厉害，傻逼";
        System.out.println("待检测语句数：" + s.length());

        //记录时间
        long beginTime = System.currentTimeMillis();
        JZKeywordFilterUtil jzKeywordFilterUtil = new JZKeywordFilterUtil();
        System.out.println("文档中敏感词得数量：" + jzKeywordFilterUtil.sensitiveWordMap.size());

        Set<String> sensitiveWord = jzKeywordFilterUtil.getSensitiveWord(s, 2);
        long endTime = System.currentTimeMillis();
        System.out.println("要被替换的敏感词个数为：" + sensitiveWord.size() + "。包含：" + sensitiveWord);
        System.out.println("总耗时毫秒数为：" + (endTime - beginTime));

        String s1 = jzKeywordFilterUtil.replaceSensitiveWord(s, 1, "*");
        System.out.println("替换完成的字符串：" + s1);

    }
}
