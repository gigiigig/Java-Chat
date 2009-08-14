/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.commons;

import chatcommons.Commands;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MESSAGE.Parameters.Parameter;
import java.nio.Buffer;
import java.util.List;

/**
 *
 * @author Luigi
 */
public class MessageToJson {

    /**
     * Trasforma il messaggio in un oggetto JSON
     * @param message
     * @return
     */
    public static String messageToJson(MESSAGE message) {
        StringBuffer result = new StringBuffer();
        result.append("var e = {");
        result.append("'type' : ");
        result.append("'" + message.getType() + "'");
        result.append(", 'name' : ");
        result.append("'" + message.getName() + "'");
        result.append(", 'sender' : ");
        result.append("'" + message.getSender() + "'");

        List<Parameter> parameters = message.getParameters().getParameter();

        if (message.getName().equals(Commands.Command.ADDUSER)) {
            result.append(" , "+list("nick", parameters));
        } else if (parameters.size() != 0) {
            result.append(", '" + message.getParameters().getParameter().get(0).getName() + "' : '");
            result.append(message.getParameters().getParameter().get(0).getValue() + "'");
        }
        result.append("};");
        return result.toString();
    }

    /*
     *crea un oggetti JSON che rappresenta una lista di prpietà con lo stesso
     * nome , e le nomina da 1 a n
     *
     */
    private static String list(String property, List<Parameter> parameters) {
        StringBuffer result = new StringBuffer();
        result.append(property + " : {");
        int cont = 0;
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(property)) {
                if(cont!=0){
                    result.append(" , ");
                }
                result.append("'" + cont + "' : '" + parameter.getValue() + "'");
                cont++;
            }
        }
        result.append("}");
        return result.toString();
    }
}
