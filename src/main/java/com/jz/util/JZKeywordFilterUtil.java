package com.jz.util;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jack zhang
 * @version 1.0
 * @description: TODO
 * @date 2023/10/21 22:17
 */
public class JZKeywordFilterUtil {

    /**
     * 从配置文件中加载敏感词列表
     * 后续升级目标：将敏感词全部存储进入mysql中，从mysql中获取。
     */
    public static Trie sensitiveWordMap;

    /**
     * 加载敏感词列表
     * @param list 敏感词列表，从配置文件中获取并存储进入list
     */
    public static void loadWord(List<String> list){

        sensitiveWordMap = new PatriciaTrie();

        Map nowMap;

        String key;

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            key = iterator.next();
            nowMap = sensitiveWordMap;

            for (int i = 0; i < key.length(); i++) {
                char c = key.charAt(i);
                Object o = nowMap.get(String.valueOf(c));
                if (o != null){
                    nowMap = (Trie) o;
                }else {
                    Trie trie = new PatriciaTrie<>();
                    trie.put("isEnd",0);
                    nowMap.put(String.valueOf(c),trie);
                    nowMap = trie;
                }
                if (i == key.length() - 1){
                    nowMap.put("isEnd","1");
                }
            }
        }
        System.out.println(sensitiveWordMap);
    }

    /**
     * 通过类路径加载文件
     */
    public static void loadWordFromFile(String path){
        String encoding = "UTF-8";
        File file = new File(path);

        try {
        if (file.isFile() && file.exists()){
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),encoding);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            ArrayList<String> list = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null){
                list.add(line);
            }

            /**
             * 关闭流对象
             */
            bufferedReader.close();
            inputStreamReader.close();
            loadWord(list);
            }
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某个字符串中的敏感词
     */
    public static Set<String> getSensitiveWord(String text, int machType){

        text = deleteSpecialWord(text);

        Set<String> sensitiveWordList = new HashSet<>();
        for (int i = 0; i < text.length(); i++) {
             int length = checkSensitiveWord(text.toLowerCase(),i,machType);
             if (length > 0){
                 sensitiveWordList.add(text.toLowerCase().substring(i,i + length));
                 i = i + length - 1;
             }
        }

        return sensitiveWordList;
    }

    /**
     * 将敏感词替换为空字符串
     * @param text 用户提交的评论等信息
     * @return 替换完成的字符串
     */
    public static String deleteSpecialWord(String text){

        String regEx = "[\n`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，_·、？]";

        String aa = "";

        Pattern pattern = Pattern.compile(regEx);

        Matcher matcher = pattern.matcher(text);

        return matcher.replaceAll(aa).trim();

    }

    /**
     *
     * @param text 用户评论
     * @param beginIndex 开始索引
     * @param matchType 需要被替换的字符串的长度
     * @return
     */
    public static int checkSensitiveWord(String text, int beginIndex, int matchType){
        boolean flag = false;

        int matchFlag = 0;

        String word;

        Trie nowMap = sensitiveWordMap;

        for (int i = beginIndex; i < text.length(); i++) {

            word = String.valueOf(text.charAt(i));

            nowMap  = (Trie) nowMap.get(word);

            if (nowMap != null){
                matchFlag++;

                if ("1".equals(nowMap.get("isEnd"))){

                    flag = true;

                    if (2 == matchType){
                        break;
                    }
                }
            }else {
                break;
            }
        }
        if (matchFlag < 2 || !flag){
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 替换某个字符串
     */
    public static String replaceSensitiveWord(String text, int machType, String replaceChar){

        String resultText = text;

        Set<String> sensitiveWord = getSensitiveWord(text, machType);
        Iterator<String> iterator = sensitiveWord.iterator();
        String word = null;
        String replaceString = null;

        while (iterator.hasNext()){
            word = iterator.next();

            replaceString = getReplaceChars(replaceChar, word.length());

            resultText = resultText.replaceAll(word,replaceString);
        }
        return resultText;
    }

    private static String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }
        return resultReplace;
    }
}
