package cn.eric.h2.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * @ClassName ImageCheck
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2020/6/18
 * @Version V1.0
 **/
public class ImageCheck {
    private static Logger logger = LoggerFactory.getLogger(ImageCheck.class);

    private ImageCheck() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isImageFromBase64(String base64Str) {
        boolean flag = false;
        try {
            BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64(base64Str)));
            if (null == bufImg) {
                return flag;
            }
            flag = true;
        } catch (Exception e) {
            logger.error("isImageFromBase64 exception", e);
        }
        return flag;
    }
}
