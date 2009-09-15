/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emoticon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
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
        /*try {
        javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(properties.getClass().getPackage().getName());
        javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
        
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //marshaller.marshal(properties,new FileOutputStream(new java.io.File(Properties.class.getClassLoader().getResource("properties.xml").getFile())));
        marshaller.marshal(properties, new FileOutputStream(new java.io.File(getPath() + "properties.xml")));
        return true;
        } catch (FileNotFoundException ex) {
        log.error(ex);
        } catch (javax.xml.bind.JAXBException ex) {
        // XXXTODO Handle exception
        log.error(ex); //NOI18N
        }
        return false;*/
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
        /*Properties properties = new Properties();
        File file = new File(getPath() + "properties.xml");
        if (file.isFile()) {
        try {
        javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(properties.getClass().getPackage().getName());
        javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        properties = (Properties) unmarshaller.unmarshal(file); //NOI18N
        
        return properties;
        } catch (javax.xml.bind.JAXBException ex) {
        // XXXTODO Handle exception
        log.error(ex); //NOI18N
        
        return null;
        }
        } else {
        return null;
        }*/

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

    public String getPath() {

       /* String fullPath = "";

        fullPath = Emoticon.class.getResource("").toString();
        fullPath = fullPath.replace("%20", " ");

        String[] temp = fullPath.split("/");

        String path = "";

        if (!temp[1].contains(":")) {
            path = "/";
        }
        for (int i = 1; i < temp.length - 3; i++) {
            String string = temp[i];
            path += string + "/";
        }
        
        return path;
        */
         return System.getProperty("user.dir") + IOUtils.DIR_SEPARATOR;
    }//    public static void main(String[] args) {
//        Util util = new Util();
//        util.readProperties();
//
//        File file = new File(util.getPath() + "properties.xml");
//       
//    }
}

