/*
 * ChatClientApp.java
 */
package gg.msn.ui;

import java.util.EventObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ChatClientApp extends SingleFrameApplication {
    private static Log log = LogFactory.getLog(ChatClientApp.class);
    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        /*try {*/
//            Properties properties = new Properties();
//            File file = new File("test.properties");
//            System.out.println(file.getPath());
//            properties.load(new FileInputStream(file));
       log.debug("porca madonnnnnnaaaaaa!!!!");
            show(new ChatClientView(this));         
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ChatClientApp
     */
    public static ChatClientApp getApplication() {
        return Application.getInstance(ChatClientApp.class);
    }

       
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(ChatClientApp.class, args);
    }

    @Override
    public void exit(EventObject arg0) {
        super.exit(arg0);
        
    }
    
    

   
    
    
    
    
}
