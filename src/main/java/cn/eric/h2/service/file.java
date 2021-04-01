package cn.eric.h2.service;


import cn.eric.h2.util.http.HttpUtilOld;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class file {

    private static final String CONTENT_CHARSET = "UTF-8";
    private static final String secret = "bd641e338a3a46df9655c5e1c750d576";
    private static final String appKey = "e8290a61af40c20997ef8e09f92dbbae";
    private static String uri = "/ai-cloud-face/face/tool/detect";
    static String srcName2 = "C:\\Users\\YCKJ2725\\Pictures\\my.jpg";

    public static void main(String[] args) throws IOException {

//        String srcName2 = "/Users/zhengshunfu/Desktop/22.jpeg";
        File fileB = new File(srcName2);
        BufferedImage bim2 = ImageIO.read(fileB);
        try {
            Map<Object, Object> parameters = new HashMap<>();
            String nonceStr = System.currentTimeMillis() + "";
            parameters.put("appKey", appKey);
            parameters.put("nonceStr", nonceStr);
            parameters.put("sign", createSign(parameters));
            parameters.put("img", Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(fileB))));
            parameters.put("mode", true);
            String post = HttpUtilOld.post("https://api-ai.cloudwalk.cn" + uri, JSONObject.toJSONString(parameters));
//                String post = post(JSONObject.toJSONString(parameters),"https://api-ai.cloudwalk.cn" + uri);
            System.out.println(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String createSign(Map<Object, Object> parameters) throws Exception {
        SortedMap<Object, Object> parametersForSign = new TreeMap<>();
        parametersForSign.put("uri", uri);
        parametersForSign.putAll(parameters);
        StringBuilder sb = new StringBuilder();
        Set es = parametersForSign.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                if (!it.hasNext()) {
                    sb.append(k).append("=").append(v);
                } else {
                    sb.append(k).append("=").append(v).append("&");
                }
            }
        }
        System.out.println(sb);
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
        mac.init(secretKey);
        byte[] hash = mac.doFinal(sb.toString().getBytes(CONTENT_CHARSET));
        return new BASE64Encoder().encode(hash);
    }
}
