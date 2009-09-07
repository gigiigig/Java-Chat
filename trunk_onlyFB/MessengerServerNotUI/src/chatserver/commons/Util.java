/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.commons;

import chatserver.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public class Util {

    private Log log = LogFactory.getLog(this.getClass());
    public static String DEFAULTENCODING = "UTF-8";

    public static Util getInstance() {
        return new Util();
    }

    public Util() {
    }

    public boolean writeProperties(Properties properties) {

        try {
            FileOutputStream fos = new FileOutputStream(getPath().concat("properties.xml"));
            properties.storeToXML(fos, "");
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }

    }

    public Properties readProperties() {

        Properties properties = new Properties();
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

    public String getPath() {

        String fullPath = Main.class.getResource("").toString();
        String[] temp = fullPath.split("/");

        String path = "";
        if (!temp[1].contains(":")) {
            path = "/";
        }
        for (int i = 1; i < temp.length - 2; i++) {
            String string = temp[i];
            path += string + "/";
        }

        return path;




    }
}
