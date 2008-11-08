/*
 * ChatServerApp.java
 */

package chatserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ChatServerApp extends SingleFrameApplication {
    

    private Log log = LogFactory.getLog(this.getClass());
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new ChatServerView(this));
        log.info("test");
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ChatServerApp
     */
    public static ChatServerApp getApplication() {
        return Application.getInstance(ChatServerApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(ChatServerApp.class, args);
    }
}
