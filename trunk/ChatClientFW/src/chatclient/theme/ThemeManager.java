/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.theme;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class ThemeManager {

    public static final String THEMEXML = "theme.xml";
    public static final String MAIN_BACKGROUND = "mainbackground";
    public static final String LOGIN_BACKGROUND = "loginbackground";
    public static final String CHAT_BACKGROUND = "chatbackground";
    public static final String MAIN_IMAGE = "mainimage";
    public static final String USER_ICON = "usericon";
    public static final String ADD_USER_ICON = "addusericon";
    private static Log log = LogFactory.getLog(ThemeManager.class);

    /**
     * Carica il tema dall'xml che sta nella 
     * cartella passata come paramtro
     * @param themeFolder - la cartella dove è contenuto il tema
     * @return
     */
    public static Map<String, ImageIcon> loadTheme(String themeFolder) {
        try {
            Properties properties = new Properties();
            File xml = new File(themeFolder + "/" + THEMEXML);
            if (xml.isFile()) {
                properties.loadFromXML(new FileInputStream(xml));
            } else {
                log.warn("file " + xml.getPath() + " not Found");
                return new HashMap<String, ImageIcon>();
            }
            Set<Object> keys = properties.keySet();

            Map<String, ImageIcon> images = new HashMap();

            for (Object object : keys) {
                String key = (String) object;
                String imageSt = properties.getProperty(key);

                File image = new File(themeFolder + "/" + imageSt);
                if (image.isFile()) {
                    properties.loadFromXML(new FileInputStream(xml));
                    ImageIcon icon = new ImageIcon(image.toURI().toURL());
                    log.trace("loaded image : " + image.getName());
                    images.put(key, icon);
                } else {
                    log.warn("image " + image.getPath() + " not Found");
                }
            }
            return images;
        } catch (IOException ex) {
            log.error(ex);
            return new HashMap<String, ImageIcon>();
        }
    }//    public static void main(String[] args) {
//        Map<String,ImageIcon> images = loadTheme("C:/Users/Luigi/Documents/ProgettiNetBeans/ChatClientFW/images/theme/default/");
//        for (String string : images.keySet()) {
//            System.out.println(string+ "  : "+images.get(string).getDescription());
//        }
//    }
}
