package cn.eric.h2.util;

import cn.eric.h2.spring.SpringContextHolder;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name:cloudwalk-common
 * File Name:StringUtil.java
 * Package Name cn.cloudwalk.common.lang:
 * Date:2016年3月19日下午4:39:34
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd  All Rights Reserved.
 */

/**
 * ClassName:StringUtil <br/>
 * Description: String的工具类. <br/>
 * Date: 2016年3月19日 下午4:39:34 <br/>
 *
 * @author 陈腾
 * @version 1.0.0
 * @since JDK 1.7
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BIAOJI = "<([a-zA-Z]+)[^<>]*>";
    private static final String PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return String
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            LOGGER.info(e.getMessage());
        }
        return "";
    }

    public static String abbr2(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        // 是不是HTML代码
        boolean isCode = false;
        // 是不是HTML特殊字符,如&nbsp;
        boolean isHTML = false;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.info(e.getMessage());
            }

            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String tempResult = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        tempResult = tempResult.replaceAll(
                "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                "");
        // 去掉成对的HTML标记
        tempResult = tempResult.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile(BIAOJI);
        Matcher m = p.matcher(tempResult);
        List<String> endHTML = Lists.newArrayList();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * changeUTF8 此函数是将字符串乱码转化成UTF-8格式
     *
     * @param str 传入乱码的String
     * @return String 返回转码后的String
     */
    public static String changeUTF8(String str) {
        String resultStr = "";
        if (null != str) {
            try {
                resultStr = new String(str.getBytes(StandardCharsets.ISO_8859_1), CHARSET_NAME);
            } catch (Exception e) {
                LOGGER.info(str + "字符转化出错");
            }
        }
        return resultStr;
    }

    public static StringBuffer compareAndDeleteFirstChar(StringBuffer sb, char c) {
        if ((sb.length() > 0) && (sb.charAt(0) == c)) {
            sb = sb.deleteCharAt(0);
        }
        return sb;
    }

    public static StringBuilder compareAndDeleteFirstChar(StringBuilder sb, char c) {
        if ((sb.length() > 0) && (sb.charAt(0) == c)) {
            sb = sb.deleteCharAt(0);
        }
        return sb;
    }

    public static StringBuffer compareAndDeleteLastChar(StringBuffer sb, char c) {
        if ((sb.length() > 0) && (sb.charAt(sb.length() - 1) == c)) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

    public static StringBuilder compareAndDeleteLastChar(StringBuilder sb, char c) {
        if ((sb.length() > 0) && (sb.charAt(sb.length() - 1) == c)) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }

    /**
     * 判断字符串是否包含汉字
     *
     * @param str String
     * @return boolean
     */
    public static boolean containsChinese(String str) {
        if (!hasText(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endsWithIgnoreCase(String inputText, String suffix) {
        StringBuilder regex = new StringBuilder();
        regex.append(".*");
        for (int i = 0; i < suffix.length(); i++) {
            char c = suffix.charAt(i);
            regex.append("[");
            regex.append(Character.toLowerCase(c));
            regex.append(Character.toUpperCase(c));
            regex.append("]");
        }
        return Pattern.matches(regex.toString(), inputText);
    }

    public static String firstLetterToUpperCase(String s) {
        String m = Character.toUpperCase(s.charAt(0)) + "";
        if (s.length() > 1) {
            m += s.substring(1);
        }

        return m;
    }

    /**
     * 转换为字节数组
     *
     * @param str
     * @return byte[]
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获得字符的高八位的值
     *
     * @param c char
     * @return int
     */
    public static int getHighBit(char c) {
        int intValue = c;
        String hexValue = Integer.toHexString(intValue);
        int len = hexValue.length();
        if (len < 4) {
            for (int i = len; i < 4; i++) {
                hexValue = "0" + hexValue;
            }
        }
        hexValue = hexValue.substring(0, 2);

        return Integer.parseInt(hexValue, 16);
    }

    /**
     * 获得字符的低八位的值
     *
     * @param c char
     * @return int
     */
    public static int getLowBit(char c) {
        int intValue = c;
        String hexValue = Integer.toHexString(intValue);
        int len = hexValue.length();
        if (len < 4) {
            for (int i = len; i < 4; i++) {
                hexValue = "0" + hexValue;
            }
        }
        hexValue = hexValue.substring(2, 4);

        return Integer.parseInt(hexValue, 16);
    }

    /**
     * 获得i18n字符串
     */
    public static String getMessage(String code, Object[] args) {
        LocaleResolver localLocaleResolver = SpringContextHolder.getBean(LocaleResolver.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Locale localLocale = localLocaleResolver.resolveLocale(request);
        return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
    }

    /**
     * 根据URL截取不带后缀的文件名
     *
     * @param url String
     * @return String
     */
    public static String getPageName(String url) {
        int fullNameEndIndex = url.indexOf("?");
        if (fullNameEndIndex == -1) {
            fullNameEndIndex = url.length();
        }
        String temp = url.substring(0, fullNameEndIndex);
        String fullFileName = temp.substring(temp.lastIndexOf("/") + 1);
        String pageName = "";
        int fileNameEndIndex = fullFileName.lastIndexOf(".");
        if (fileNameEndIndex == -1) {
            fileNameEndIndex = fullFileName.length();
        }
        pageName = fullFileName.substring(0, fileNameEndIndex);
        return pageName;
    }

    /**
     * getRandomNickName 生成随机昵称
     *
     * @return String
     */
    public static String getRandomString() {
        List<String> aList = new ArrayList<String>();
        List<String> aListnum = new ArrayList<String>();
        //随机数字
        String[] aStringnum = "0,1,2,3,5,6,7,8,9".split(",");
        //随机字母
        String[] aStrings = "q,w,e,r,t,y,u,i,o,p,a,s,d,f,g,h,j,k,l,z,x,c,v,b,n,m".split(",");
        aList = Arrays.asList(aStrings);
        aListnum = Arrays.asList(aStringnum);
        Collections.shuffle(aList, new Random());
        Collections.shuffle(aListnum, new Random());
        return "cloudwalk"
                + aListnum.toString().replaceAll(", ", "").replace("[", "").replace("]", "").substring(0, 4)
                + aList.toString().replaceAll(", ", "").replace("[", "").replace("]", "").substring(0, 4);
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else {
            if (isNotBlank(remoteAddr)) {
                remoteAddr = request.getHeader("Proxy-Client-IP");
            } else {
                if (isNotBlank(remoteAddr)) {
                    remoteAddr = request.getHeader("WL-Proxy-Client-IP");
                }
            }
        }

        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 当 text 不为 null 且长度不为 0
     *
     * @param text String
     * @return boolean
     */
    public static boolean hasLength(String text) {
        return (text != null) && (text.length() > 0);
    }

    /**
     * text 不能为 null 且必须至少包含一个非空格的字符
     *
     * @param text String
     * @return boolean
     */
    public static boolean hasText(String text) {
        return hasLength(text) && Pattern.matches(".*\\S.*", text);
    }

    /**
     * 根据正则表达式查找第count次匹配的开始索引
     *
     * @param regex 正则表达式
     * @param input CharSequence 要匹配的字符序列
     * @param count 第几次匹配
     * @return int
     */
    public static int indexOf(String regex, CharSequence input, int count) {
        int index = -1;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        for (int i = 1; m.find(); i++) {
            if (count == i) {
                index = m.start();
                break;
            }
        }
        return index;
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是超链接
     *
     * @param a String
     * @return boolean
     */
    public static boolean isA(String a) {
        if (!hasText(a)) {
            return false;
        }

        return a.matches("(<a\\s*href=[^>]*>)");
    }

    /**
     * 判断是否汉字
     *
     * @param c char 要判断的字符
     * @return boolean
     */
    public static boolean isChinese(char c) {
        String tmp = String.valueOf(c);
        return tmp.matches("[\u4e00-\u9fa5]+");
    }

    /**
     * 判断整个字符串是否都是汉字
     *
     * @param str String
     * @return boolean
     */
    public static boolean isChinese(String str) {
        return str.matches("[\u4e00-\u9fa5]+");
    }

    /**
     * 判断是否Email字符串
     *
     * @param email String
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (!hasText(email)) {
            return false;
        }
        return email.matches("[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*");
    }

    /**
     * 判断是否是一个整数串
     *
     * @param value String
     * @return boolean
     */
    public static boolean isInteger(String value) {
        if ((value.startsWith("+")) || (value.startsWith("-"))) {
            value = value.substring(1);
        }
        return isNumber(value);
    }

    /**
     * 判断是否合法的手机号码. 手机号码为11位数字。 国家号码段分配如下： 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     *
     * @param mobiles 手机号码
     * @return boolean
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile(PHONE);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isMobileNO2(String mobiles) {
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(mobiles);
        return m.find();
    }

    /**
     * 判断是否是一个数字串
     *
     * @param value
     * @return boolean
     */
    public static boolean isNumber(String value) {
        if (!hasText(value)) {
            return false;
        }
        boolean isInt = true;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (!Character.isDigit(ch)) {
                isInt = false;
                break;
            } //if
        } //for
        return isInt;
    }

    /**
     * 判断是否URL
     *
     * @param url String
     * @return boolean
     */
    public static boolean isURL(String url) {
        if (!hasText(url)) {
            return false;
        }
        return url.matches("http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串 例如：row.user.id 返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (String val2 : vals) {
            val.append("." + val2);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    public static boolean like(String inputText, String likeText) {
        StringBuilder regex = new StringBuilder();
        regex.append(".*");
        for (int i = 0; i < likeText.length(); i++) {
            char c = likeText.charAt(i);
            regex.append("[");
            regex.append(Character.toLowerCase(c));
            regex.append(Character.toUpperCase(c));
            regex.append("]");
        }
        regex.append(".*");
        return Pattern.matches(regex.toString(), inputText);
    }

    public static boolean likeSentence(String inputText, String likeText) {
        return likeSentence(inputText, likeText, ';', ' ');
    }

    public static boolean likeSentence(String inputText, String likeText, char sentenceSeparator,
                                       char wordSeparator) {
        String[] sentenceArr = inputText.split(sentenceSeparator + "");
        for (String sentence : sentenceArr) {
            if (startsWithIgnoreCase(sentence, likeText)) {
                return true;
            }
            String[] wordArr = sentence.split(wordSeparator + "");
            if (wordArr.length < likeText.length()) {
                continue;
            }
            boolean wordMatch = true;
            for (int i = 0; i < likeText.length(); i++) {
                if (!startsWithIgnoreCase(wordArr[i], likeText.charAt(i) + "")) {
                    wordMatch = false;
                    break;
                }
            }
            if (wordMatch) {
                return true;
            }
        }

        return false;
    }

    public static boolean likeSentence2(String inputText, String likeText) {
        return likeSentence2(inputText, likeText, ';', ' ');
    }

    public static boolean likeSentence2(String inputText, String likeText, char sentenceSeparator,
                                        char wordSeparator) {
        String[] sentenceArr = inputText.split(sentenceSeparator + "");

        String[] wordArr = sentenceArr[0].split(wordSeparator + "");
        for (String word : wordArr) {
            if (startsWithIgnoreCase(word, likeText)) {
                return true;
            }
        }
        if (sentenceArr.length < likeText.length()) {
            return false;
        }
        for (int i = 0; i < likeText.length(); i++) {
            String[] wordArr2 = sentenceArr[i].split(wordSeparator + "");
            boolean wordMatch = false;
            for (String word : wordArr2) {
                if (startsWithIgnoreCase(word, likeText.charAt(i) + "")) {
                    wordMatch = true;
                    break;
                }
            }
            if (!wordMatch) {
                return false;
            }
        }

        return true;
    }

    public static String lineSeparator() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        pw.close();
        return sw.toString();
    }

    public static void main(String[] args) {
        //    String s1 = "http://www.baidu.com/index?url=http://www.baidu.com";
        //    String s2 = "yuancihang@tom.com_";
        //    System.out.println(StringUtil.isURL(s1));
        //    System.out.println(StringUtil.isEmail(s2));

        LOGGER.info(MessageFormat.format("{0}2222{1}----", 10.1133f, "eee"));
        LOGGER.info(NumberFormat.getPercentInstance().format(0.123));
        LOGGER.info(NumberFormat.getCurrencyInstance().format(0.123));
        LOGGER.info(String.format("qq%.2frr", 0.12345));
        LOGGER.info(String.valueOf(like("cloudwalk", "AN")));
        LOGGER.info(String.valueOf(likeSentence("chen teng;ge", "ct")));
        LOGGER.info(String.valueOf(likeSentence2("chen teng;ge", "g")));
        LOGGER.info(String.valueOf(isMobileNO("12016155153")));
        LOGGER.info(getRandomString());
    }

    public static int matchCount(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static void matcher(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        while (m.find()) {
            LOGGER.info("[" + m.start() + "," + m.end() + ") " + m.group());
        }
    }

    /**
     * 删除文本中的HTML标记
     *
     * @param htmlText String HTML文本
     * @return String
     */
    public static String removeHtmlTag(String htmlText) {
        String regex = "<.+?>";
        return htmlText.replaceAll(regex, "");
    }

    /**
     * 根据正则表达式替换字符串
     *
     * @param regex       String 正则表达式
     * @param input       CharSequence 要匹配的字符序列
     * @param replacement 替换字符串
     * @param index       int
     *                    替换第几个匹配的,0表示替换所有匹配的(相当于replaceAll),1表示替换第一个匹配的(相当于replaceFirst),2表示替换第二个匹配的,以此类推.
     * @return String
     */
    public static String replace(String regex, CharSequence input, String replacement, int index) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        for (int i = 1; m.find(); i++) {
            if ((index == 0) || (index == i)) {
                m.appendReplacement(sb, replacement);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String replace(String regex, String excludeString, CharSequence input,
                                 String replacement, int index) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        for (int i = 1; m.find(); i++) {
            boolean flag = true;
            if (m.start() + excludeString.length() <= input.length()) {
                String sub = input.subSequence(m.start(), m.start() + excludeString.length()).toString();
                if (sub.equals(excludeString)) {
                    flag = false;
                }
            }
            if (((index == 0) || (index == i)) && flag) {
                m.appendReplacement(sb, replacement);
            }

        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        return m.replaceAll("");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html
     * @return String
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }

    /**
     * 截取两段文本中间的文本
     *
     * @param sourceText String 源文本
     * @param beginText  String 前段文本
     * @param endText    String 后段文本
     * @return String[]
     */
    public static String[] splitText(String sourceText, String beginText, String endText) {
        String[] textArr = new String[3];

        if (beginText == null || endText == null) {
            textArr[0] = "";
            textArr[1] = sourceText;
            textArr[2] = "";
            return textArr;
        }
        int preBeginIndex = sourceText.indexOf(beginText.trim());
        //前端字符串
        if (preBeginIndex < 0) {
            return new String[]{"", sourceText, ""};
        } else {
            textArr[0] = sourceText.substring(0, preBeginIndex) + beginText;
        }
        int preEndIndex = preBeginIndex + beginText.length();

        int posBeginIndex = sourceText.indexOf(endText.trim());
        //中间字符串
        if (posBeginIndex <= 0) {
            textArr[1] = sourceText.substring(preEndIndex);
            textArr[2] = "";
            return textArr;
        } else {
            textArr[1] = sourceText.substring(preEndIndex, posBeginIndex);
        }

        int posEndIndex = posBeginIndex + endText.length();
        //后端字符串
        if (posEndIndex >= sourceText.length()) {
            textArr[2] = "";
            return textArr;
        } else {
            textArr[2] = endText + sourceText.substring(posEndIndex);
        }
        return textArr;
    }

    public static boolean startsWithIgnoreCase(String inputText, String prefix) {
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            regex.append("[");
            regex.append(Character.toLowerCase(c));
            regex.append(Character.toUpperCase(c));
            regex.append("]");
        }
        regex.append(".*");
        return Pattern.matches(regex.toString(), inputText);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
     * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
     * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param txt
     * @return String
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为字节数组
     *
     * @param bytes
     * @return String
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    public static String toString(String[] arr, String separator) {
        StringBuilder resultBuilder = new StringBuilder();
        for (String el : arr) {
            resultBuilder.append(el).append(separator);
        }
        String result = resultBuilder.toString();
        if (result.endsWith(separator)) {
            result = result.substring(0, result.length() - separator.length());
        }
        return result;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") ==
     * "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * toUtf8String 转化格式
     *
     * @param s
     * @return String
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes(StandardCharsets.UTF_8);
                } catch (Exception ex) {
                    LOGGER.info("ex is ", ex);
                    b = new byte[0];
                }
                for (byte element : b) {
                    int k = element;
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
