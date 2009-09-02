/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver.listener;

import chatserver.ChatServerView;
import chatserver.OptionsFrame;
import chatserver.commons.Util;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class ChatServerViewListeners {

    private ChatServerView csv;

    public ChatServerViewListeners(ChatServerView csv) {
        this.csv = csv;
    }
        
    /**
     * 
     * @return listner pe la mainView
     */
    public MainViewListerner getMainViewListerner() {
        return new MainViewListerner(csv);
    }

    /**
     * Operazioni per gli eventi dell'applicazione
     */
    public class MainViewListerner extends WindowAdapter {

        private ChatServerView csv;

        public MainViewListerner(ChatServerView csv) {
            super();
            this.csv = csv;
        }

        @Override
        public void windowClosing(WindowEvent arg0) {
            super.windowClosing(arg0);
//            if (csv.getOutputStream() != null) {
//                csv.disconnetti();
//            }
//
//            if(csv.getServerThread()!= null && csv.getServerThread().isActive()!= false){
//                csv.stopServer();
//            }

        }

        @Override
        public void windowOpened(WindowEvent arg0) {
            super.windowOpened(arg0);

            //Crea proprietà

            Properties properties = Util.getInstance().readProperties();

            if (properties != null && !properties.getProperty("port").equals("")) {
                csv.setPort(Integer.parseInt(properties.getProperty("port")));
            } else {


//            port = 3434;
//            ip = "localhost"

                OptionsFrame optionsFrame = new OptionsFrame();
                optionsFrame.setLocationRelativeTo(csv.getFrame());
                optionsFrame.setVisible(true);
                optionsFrame.getPortaText().setText("3434");

//                optionsFrame.toFront();
//                optionsFrame.DO_NOTHING_ON_CLOSE = 1;
                optionsFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

            }
        }
    }
}

