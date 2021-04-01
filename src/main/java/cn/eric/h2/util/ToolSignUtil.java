package cn.eric.h2.util;

import cn.eric.h2.util.http.HttpUtilNew;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ToolSignUtil {


    public static String createSign(final Map<Object, Object> parameters, final String uri, final String secret) throws Exception {
        final SortedMap<Object, Object> parametersForSign = new TreeMap<Object, Object>();
        parametersForSign.put("uri", uri);
        parametersForSign.putAll(parameters);
        final StringBuilder sb = new StringBuilder();
        final Set es = parametersForSign.entrySet();
        final Iterator it = es.iterator();
        while (it.hasNext()) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String k = (String) entry.getKey();
            final Object v = entry.getValue();
            if (null != v && !"sign".equals(k) && !"key".equals(k)) {
                if (!it.hasNext()) {
                    sb.append(k).append("=").append(v);
                } else {
                    sb.append(k).append("=").append(v).append("&");
                }
            }
        }
        final Mac mac = Mac.getInstance("HmacSHA1");
        final SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
        mac.init(secretKey);
        final byte[] hash = mac.doFinal(sb.toString().getBytes(StandardCharsets.UTF_8));
        return new BASE64Encoder().encode(hash);
    }


    public JSONObject common(String uri, String endPoint, final JSONObject params, String appKey, String appSecret, String image) throws Exception {
        final Map<Object, Object> parameters = new HashMap<Object, Object>(16);
//        parameters.putAll(params);

        parameters.put("appKey", appKey);
        parameters.put("nonceStr", "123456");
        parameters.put("sign", createSign(parameters, uri, appSecret));
        parameters.put("img", image);
        final String response = HttpUtilNew.postRequestBody(endPoint + uri, JSON.toJSONString(parameters));
        return JSON.parseObject(response);
    }

    public static String readTxtFile(String filePath) {
        StringBuffer content = new StringBuffer();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                //考虑到编码格式
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception
                e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void main(String[] args) throws Exception {
        String endPoint = "https://api-ai.cloudwalk.cn";
        String appkey = "e8290a61af40c20997ef8e09f92dbbae";
        final String as = "bd641e338a3a46df9655c5e1c750d576";
//        String image = readTxtFile("C:\\Users\\liangce\\Desktop\\新建文件夹\\face2.jpg");
        File fileB = new File("C:\\Users\\YCKJ2725\\Pictures\\my.jpg");
        String image = Base64.getEncoder().encodeToString(IOUtils.toByteArray(new FileInputStream(fileB)));
        ToolSignUtil utl = new ToolSignUtil();
        utl.backgroundcolor(utl, endPoint, image, appkey, as);
    }

    public void backgroundcolor(ToolSignUtil utl, String endPoint, String image, String appkey, String as) throws Exception {
        JSONObject params = new JSONObject();
        String uri = "/ai-cloud-face/face/tool/detect";
//        params.put("img", image);
        System.out.println(utl.common(uri, endPoint, params, appkey, as, image));
    }


}
