package gg.msn.core.commons;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import gg.msn.core.manager.PersistentDataManager;
import gg.msn.ui.theme.ThemeManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public class Util {

    private static Log log = LogFactory.getLog(Util.class);
    public static String DEFAULTENCODING = "UTF-8";
    public static final String PROPERTY_THEME_FOLDER = "themefolder";
    public static final String PROPERTY_DOWNLOAD_FOLDER = "downloadFolder";
    public static final String PROPERTY_PORT = "port";
    public static final String PROPERTY_IP = "ip";
    public static final String PROPERTY_NICK = "nick";
    public static final String PROPERTY_FONT = "font";
    public static final String PROPERTY_COLOR = "color";
    public static final String PROPERTY_FACEBOOK_EMAIL = "facebookEmail";
    public static final String PROPERTY_FACEBOOK_PSW = "facebookPsw";
    public static final String PROPERTY_SAVE_FACEBOOK_EMAIL = "saveFacebookEmail";
    public static final String PROPERTY_SAVE_FACEBOOK_PSW = "saveFacebookPsw";
    public static final String PROPERTY_INVISIBLE = "invisible";
    private static final char SEPARATOR = IOUtils.DIR_SEPARATOR;
    public static final String VALUE_DEFAULT_THEME_FOLDER = "images" + SEPARATOR + "theme" + SEPARATOR + "default";
    public static boolean VALUE_DEFAULT_INVISIBLE = false;

    public Util() {
    }

    public static boolean writeProperties(Properties properties) {

        try {
            FileOutputStream fos = new FileOutputStream(getPath().concat("properties.xml"));
            properties.storeToXML(fos, "");
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }

    }

    public static Properties readProperties() {

        Properties properties = new Properties();
        log.debug("path = " + getPath());
        File file = new File(getPath() + "properties.xml");
        if (file.isFile()) {
            try {
                properties.loadFromXML(new FileInputStream(file));
                return properties;
            } catch (Exception ex) {
                // XXXTODO Handle exception
                log.error(ex); //NOI18N

                return null;
            }
        } else {
            return null;
        }
    }

    public static void initializeProperties() {
        //Crea proprietà
        Properties properties = Util.readProperties();

        if (properties == null) {
            properties = new Properties();
            ThemeManager.loadTheme(Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
            properties.setProperty(Util.PROPERTY_THEME_FOLDER, Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
            properties.setProperty(PROPERTY_INVISIBLE, VALUE_DEFAULT_INVISIBLE + "");
            Util.writeProperties(properties);

        } else {

            if (properties != null && !StringUtils.equals(properties.getProperty(Util.PROPERTY_IP), "")) {
                PersistentDataManager.setIp(properties.getProperty(Util.PROPERTY_IP));
            }

            try {
                if (properties != null && !StringUtils.equals(properties.getProperty(Util.PROPERTY_PORT), "")) {
                    PersistentDataManager.setPort(NumberUtils.toInt(properties.getProperty(Util.PROPERTY_PORT)));
                }
            } catch (NumberFormatException numberFormatException) {
                log.error(numberFormatException);
            }


            if (properties == null || properties.getProperty(Util.PROPERTY_THEME_FOLDER) == null || StringUtils.equals(properties.getProperty(Util.PROPERTY_THEME_FOLDER), "")) {
                ThemeManager.loadTheme(Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
                properties.setProperty(Util.PROPERTY_THEME_FOLDER, Util.getPath() + Util.VALUE_DEFAULT_THEME_FOLDER);
                Util.writeProperties(properties);
            }

        }
    }

    public static String getPath() {

        return System.getProperty("user.dir") + IOUtils.DIR_SEPARATOR;
//
//
//        String fullPath = "";
//        fullPath = Util.class.getResource(IOUtils.DIR_SEPARATOR+"").toString();
//        fullPath = fullPath.replace("%20", " ");
//
//        String[] temp = fullPath.split("/");
//
//        String path = "";
//
//        if (!temp[1].contains(":")) {
//            path = "/";
//        }
//        for (int i = 1; i < temp.length - 2; i++) {
//            String string = temp[i];
//            path += string + "/";
//        }
//
//        return path;
    }

    public static void main(String[] args) {
        System.out.println(3 % 4);

    }

    public static byte[] compress(byte[] input) {
        // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();

        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

        // Get the compressed data
        byte[] compressedData = bos.toByteArray();
        return compressedData;
    }

    public static byte[] decompress(byte[] input) {
        // Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(input);

        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        // Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
                log.error(e);

            } catch (Exception e) {
                log.error(e);
            }
        }
        try {
            bos.close();
        } catch (IOException e) {
            log.error(e);

        } catch (Exception e) {
            log.error(e);
        }

        // Get the decompressed data
        byte[] decompressedData = bos.toByteArray();
        return decompressedData;
    }
}

