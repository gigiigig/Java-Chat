//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-463 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.16 at 03:13:08 PM CEST 
//
package chatcommons.datamessage;

import chatcommons.datamessage.generated.MESSAGE;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exolab.castor.builder.SourceGenerator;
import org.xml.sax.InputSource;

public class CastorGenerator {


    public static void main(String[] args) {

        // <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
//        XStream stream = new XStream(new DomDriver());
//
//        stream.alias("MESSAGE", MESSAGE.class);
//        stream.alias("parameter", Parameter.class);
//        stream.alias("content", Content.class);
//        stream.alias("receiver", String.class);
//
//        stream.useAttributeFor(MESSAGE.class, "xmlns");
//        stream.useAttributeFor(MESSAGE.class, "name");
//        stream.useAttributeFor(MESSAGE.class, "type");
//
//
//        System.out.println(stream.toXML(message));
//        StringWriter writer = null;
//        try {
//            MESSAGE message = new MESSAGE();
//            message.setName("nome");
//            message.setType("type");
//            message.receivers.add("receiver");
//            message.parameters.add(new Parameter("param_name", "param:_type"));
//            writer = new StringWriter();
//            Marshaller.marshal(message, writer);
//            System.out.println(writer.toString());
//
//        } catch (MarshalException ex) {
//            Logger.getLogger(MESSAGE.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ValidationException ex) {
//            Logger.getLogger(MESSAGE.class.getName()).log(Level.SEVERE, null, ex);
//        }  finally {
//            try {
//                writer.close();
//            } catch (IOException ex) {
//                Logger.getLogger(MESSAGE.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

// </editor-fold>

        SourceGenerator generator = new SourceGenerator();
        String xmlSchema = CastorGenerator.class.getResource("dataMessageXML.xsd").getPath();
        InputSource inputSource = new InputSource(xmlSchema);
        generator.setDestDir("./src/test/java");
        generator.setSuppressNonFatalWarnings(true);
        try {
            // uncomment the next line to set a binding file for source generation
//      generator.setBinding(new InputSource(getClass().getResource("binding.xml").toExternalForm()));
            // uncomment the next lines to set custom properties for source generation
//      Properties properties = new Properties();
//      properties.load(getClass().getResource("builder.properties").openStream());
//      generator.setDefaultProperties(properties);
            generator.generateSource(inputSource, CastorGenerator.class.getPackage().getName() + ".generated");
        } catch (IOException ex) {
            Logger.getLogger(CastorGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



