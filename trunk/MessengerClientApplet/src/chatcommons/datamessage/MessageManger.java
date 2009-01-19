/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcommons.datamessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static chatcommons.Commands.*;

/**
 *
 * @author Luigi
 */
public class MessageManger {

    private static Log log = LogFactory.getLog(MessageManger.class);
    private static String DEFAULTENCODING = "UTF-8";

    public static boolean directWriteMessage(MESSAGE message, OutputStream os) throws SocketException {

//        try {
//            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(chatcommons.datamessage.MESSAGE.class);
//            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
//            synchronized (os) {
//                marshaller.marshal(message, os);
//                try {
//                    os.write("\n".getBytes(DEFAULTENCODING));
//                } catch (IOException ex) {
//                    log.debug(ex);
//                }
//            }
//            log.debug("fine marshall");
//        } catch (javax.xml.bind.JAXBException ex) {
//            // XXXTODO Handle exception
//            log.error(ex); //NOI18N
//            throw new SocketException();
//
//        }
        try {
            XStream xStream = new XStream(new DomDriver());
            synchronized (os) {
                xStream.toXML(message, os);
                try {
                    os.write("\n".getBytes(DEFAULTENCODING));
                } catch (Exception ex) {
                    log.debug(ex);
                }
            }
            log.debug("fine marshall");
        } catch (Exception ex) {
            // XXXTODO Handle exception
            log.error(ex); //NOI18N
            throw new SocketException();

        }

        return false;
    }

    public static String messageToString(MESSAGE message) {
        String toReturn = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

//        try {
//            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(message.getClass().getPackage().getName());
//            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
//
//            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
//            marshaller.marshal(message, baos);
////            marshaller.marshal(message, System.out);
//        } catch (javax.xml.bind.JAXBException ex) {
//            // XXXTODO Handle exception
//            log.error(ex.getMessage()); //NOI18N
//
//        }


        XStream xStream = new XStream(new DomDriver());
        xstream.alias("blog", Blog.class);
        synchronized (baos) {
            xStream.toXML(message, baos);
            try {
                baos.write("\n".getBytes(DEFAULTENCODING));
            } catch (Exception ex) {
                log.debug(ex);
            }
        }
        log.debug("fine marshall");

        try {

            String xml = baos.toString(DEFAULTENCODING);
            toReturn = xml + "\n";
//            log.debug("xml to send : " + toReturn);
            return toReturn;
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public static String messageToStringFormatted(MESSAGE message) {
        String toReturn = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(message.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N

            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(message, baos);
//            marshaller.marshal(message, System.out);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            log.error(ex.getMessage()); //NOI18N

        }
        try {

            String xml = baos.toString("UTF-8");
            toReturn = "\n" + xml;


//            log.debug("xml to send : " + toReturn);
            return toReturn;



        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            return null;
        }
    }

    public static MESSAGE parseXML(String xml) {
        ObjectFactory objectFactory = new ObjectFactory();
        MESSAGE toReturn = objectFactory.createMESSAGE();


        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(toReturn.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            toReturn = (MESSAGE) unmarshaller.unmarshal(new StreamSource(new StringReader(xml))); //NOI18N

            return toReturn;
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            log.error(ex); //NOI18N

            return null;
        }

    }

    public static MESSAGE createRequest(String name, List<String> receivers, Map<String, String> parameters) {
        ObjectFactory objectFactory = new ObjectFactory();
        MESSAGE request = objectFactory.createMESSAGE();
        request.setParameters(objectFactory.createMESSAGEParameters());
        request.setReceivers(objectFactory.createMESSAGEReceivers());
        request.setType(REQUEST);
        if (name != null) {
            request.setName(name);
        }
        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().getReceiver().add(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
                msgParamter.setName(key);
                msgParamter.setValue(parameters.get(key));
                request.getParameters().getParameter().add(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createCommand(String name, Map<String, String> parameters) {
        ObjectFactory objectFactory = new ObjectFactory();
        MESSAGE request = objectFactory.createMESSAGE();
        request.setParameters(objectFactory.createMESSAGEParameters());
        request.setReceivers(objectFactory.createMESSAGEReceivers());
        request.setType(COMMAND);
        if (name != null) {
            request.setName(name);
        }
//        if (receivers != null) {
//            for (String receiver : receivers) {
//                request.getReceivers().getReceiver().add(receiver);
//            }
//        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
                msgParamter.setName(key);
                msgParamter.setValue(parameters.get(key));
                request.getParameters().getParameter().add(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createMessage(String name, List<String> receivers, Map<String, String> parameters) {
        ObjectFactory objectFactory = new ObjectFactory();
        MESSAGE request = objectFactory.createMESSAGE();
        request.setParameters(objectFactory.createMESSAGEParameters());
        request.setReceivers(objectFactory.createMESSAGEReceivers());
        request.setContents(objectFactory.createMESSAGEContents());
        request.setType(MESSAGE);
        if (name != null) {
            request.setName(name);
        }
        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().getReceiver().add(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
                msgParamter.setName(key);
                msgParamter.setValue(parameters.get(key));
                request.getParameters().getParameter().add(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createFiletransfer(List<String> receivers, byte[] data, Map<String, String> parameters) {
        ObjectFactory objectFactory = new ObjectFactory();
        MESSAGE request = objectFactory.createMESSAGE();
        request.setParameters(objectFactory.createMESSAGEParameters());
        request.setReceivers(objectFactory.createMESSAGEReceivers());
        request.setType(REQUEST);
        request.setName(Request.FILETRANSFER);

        if (data != null) {
            request.setData(data);
        }

        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().getReceiver().add(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
                msgParamter.setName(key);
                msgParamter.setValue(parameters.get(key));
                request.getParameters().getParameter().add(msgParamter);
            }
        }
        return request;
    }

    public static void addParameter(MESSAGE message, String name, String parameter) {
        ObjectFactory objectFactory = new ObjectFactory();

        MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
        msgParamter.setName(name);
        msgParamter.setValue(parameter);
        message.getParameters().getParameter().add(msgParamter);
    }

    public static void addParameterAt(MESSAGE message, String name, String parameter, int position) {
        ObjectFactory objectFactory = new ObjectFactory();

        MESSAGE.Parameters.Parameter msgParamter = objectFactory.createMESSAGEParametersParameter();
        msgParamter.setName(name);
        msgParamter.setValue(parameter);
        message.getParameters().getParameter().add(position, msgParamter);
    }

    public static void addReceiver(MESSAGE message, String receiver) {
        message.getReceivers().getReceiver().add(receiver);
    }

    public static void addReceiverAt(MESSAGE message, String receiver, int position) {
        message.getReceivers().getReceiver().add(position, receiver);
    }

    public static void addContent(MESSAGE message, String name, byte[] data) {
        ObjectFactory objectFactory = new ObjectFactory();

        MESSAGE.Contents.Content content = objectFactory.createMESSAGEContentsContent();
        content.setName(name);
        content.setValue(data);
        message.getContents().getContent().add(content);
    }

    public static void addContentAt(MESSAGE message, String name, byte[] data, int position) {
        ObjectFactory objectFactory = new ObjectFactory();

        MESSAGE.Contents.Content content = objectFactory.createMESSAGEContentsContent();
        content.setName(name);
        content.setValue(data);
        message.getContents().getContent().add(position, content);
    }
}