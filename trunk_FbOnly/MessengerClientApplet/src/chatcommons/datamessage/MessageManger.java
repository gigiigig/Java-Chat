/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatcommons.datamessage;

import chatcommons.datamessage.generated.Content;
import chatcommons.datamessage.generated.Contents;
import chatcommons.datamessage.generated.MESSAGE;
import chatcommons.datamessage.generated.Parameter;
import chatcommons.datamessage.generated.Parameters;
import chatcommons.datamessage.generated.Receivers;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import static chatcommons.Commands.*;
import org.exolab.castor.xml.ValidationException;

/**
 *
 * @author Luigi
 */
public class MessageManger {

    private static Log log = LogFactory.getLog(MessageManger.class);
    private static String DEFAULTENCODING = "UTF-8";

    public static boolean directWriteMessage(MESSAGE message, OutputStream os) throws SocketException {

        log.debug("inizio marshall");
        try {
            synchronized (os) {
                try {
                    String xml = messageToString(message);
                    log.trace("xml to send :" + xml);
                    os.write(xml.getBytes(DEFAULTENCODING));
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

        // Create a File to marshal to
        OutputStreamWriter writer = new OutputStreamWriter(baos);
        try {
            // Marshal the person object
            Marshaller.marshal(message, writer);
        } catch (MarshalException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            String xml = baos.toString(DEFAULTENCODING);
            //metto l'xml su un unica riga
            xml = xml.replace("\n", "");
            toReturn = xml + "\n";
//            log.debug("xml to send : " + toReturn);
            return toReturn;
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public static String messageToStringFormatted(MESSAGE message) {
        String toReturn = "\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create a File to marshal to
        OutputStreamWriter writer = new OutputStreamWriter(baos);
        try {
            // Marshal the person object
            message.marshal(writer);
        } catch (MarshalException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            String xml = baos.toString(DEFAULTENCODING);
            xml = xml.replace("><", ">\n<");
//            toReturn = "\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + xml + "\n";
            toReturn = xml + "\n";
//            log.debug("xml to send : " + toReturn);
            return toReturn;
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }

    public static String messageToStringFormattedJAXBVersion(MESSAGE message) {
        String toReturn = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(message.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
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

        MESSAGE message = null;

        try {
            message = (MESSAGE) Unmarshaller.unmarshal(MESSAGE.class, new StringReader(xml));
        } catch (MarshalException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(MessageManger.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    //  // <editor-fold defaultstate="collapsed" desc=" parseXMLJaxb ">
//    public static MESSAGE parseXMLJaxb(String xml) {
//        ObjectFactory objectFactory = new ObjectFactory();
//        MESSAGE toReturn = new MESSAGE();
//
//
//        try {
//            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(toReturn.getClass().getPackage().getName());
//            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
//            toReturn = (MESSAGE) unmarshaller.unmarshal(new StreamSource(new StringReader(xml))); //NOI18N
//
//            return toReturn;
//        } catch (javax.xml.bind.JAXBException ex) {
//            // XXXTODO Handle exception
//            log.error(ex); //NOI18N
//
//            return null;
//        }
//
//    }

// </editor-fold>
    public static MESSAGE createRequest(String name, List<String> receivers, Map<String, String> parameters) {
        MESSAGE request = new MESSAGE();
        request.setParameters(new Parameters());
        request.setReceivers(new Receivers());
        request.setType(REQUEST);
        if (name != null) {
            request.setName(name);
        }
        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().addReceiver(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                Parameter msgParamter = new Parameter();
                msgParamter.setName(key);
                msgParamter.setContent(parameters.get(key));
                request.getParameters().addParameter(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createCommand(String name, Map<String, String> parameters) {
        MESSAGE request = new MESSAGE();

        request.setParameters(new Parameters());
        request.setReceivers(new Receivers());
        
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
                Parameter msgParamter = new Parameter();
                msgParamter.setName(key);
                msgParamter.setContent(parameters.get(key));
                request.getParameters().addParameter(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createMessage(String name, List<String> receivers, Map<String, String> parameters) {
        MESSAGE request = new MESSAGE();
        request.setParameters(new Parameters());
        request.setReceivers(new Receivers());
        request.setContents(new Contents());
        request.setType(MESSAGE);
        if (name != null) {
            request.setName(name);
        }
        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().addReceiver(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                Parameter msgParamter = new Parameter();
                msgParamter.setName(key);
                msgParamter.setContent(parameters.get(key));
                request.getParameters().addParameter(msgParamter);
            }
        }
        return request;
    }

    public static MESSAGE createFiletransfer(List<String> receivers, byte[] data, Map<String, String> parameters) {
        MESSAGE request = new MESSAGE();
        request.setParameters(new Parameters());
        request.setReceivers(new Receivers());
        request.setType(REQUEST);
        request.setName(Request.FILETRANSFER);

        if (data != null) {
//            request.setData(data);
        }

        if (receivers != null) {
            for (String receiver : receivers) {
                request.getReceivers().addReceiver(receiver);
            }
        }
        if (parameters != null) {

            Set<String> paramKeys = parameters.keySet();
            for (String key : paramKeys) {
                Parameter msgParamter = new Parameter();
                msgParamter.setName(key);
                msgParamter.setContent(parameters.get(key));
                request.getParameters().addParameter(msgParamter);
            }
        }
        return request;
    }

    public static void addParameter(MESSAGE message, String name, String parameter) {

        Parameter msgParamter = new Parameter();
        msgParamter.setName(name);
        msgParamter.setContent(parameter);
        message.getParameters().addParameter(msgParamter);
    }


    public static void addParameterAt(MESSAGE message, String name, String parameter, int position) {
        Parameter msgParamter = new Parameter();
        msgParamter.setName(name);
        msgParamter.setContent(parameter);
        message.getParameters().addParameter(position, msgParamter);
    }

    public static void addReceiver(MESSAGE message, String receiver) {
        message.getReceivers().addReceiver(receiver);
    }

    public static void addReceiverAt(MESSAGE message, String receiver, int position) {
        message.getReceivers().addReceiver(position, receiver);
    }

    public static void addContent(MESSAGE message, String name, byte[] data) {
        Content content = new Content();
        content.setName(name);
        content.setValue(data);
        message.getContents().addContent(content);
    }

    public static void addContentAt(MESSAGE message, String name, byte[] data, int position) {

        Content content = new Content();
        content.setName(name);
        content.setValue(data);
        message.getContents().addContent(position, content);
    }
}