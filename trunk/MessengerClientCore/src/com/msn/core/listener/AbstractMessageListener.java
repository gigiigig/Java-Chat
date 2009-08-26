/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gg.msn.core.listener;

import chatcommons.datamessage.MESSAGE;
import java.io.OutputStream;

/**
 *
 * @author Luigi
 */
public abstract class AbstractMessageListener {
    public abstract void parseMessage(MESSAGE message);
    public abstract String getNick() ;
    public abstract OutputStream getOutputStream() ;
}
