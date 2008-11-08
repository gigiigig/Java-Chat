/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.game.dama;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Administrator
 */
public class ClientReader extends SwingWorker {

    private Log log = LogFactory.getLog(this.getClass());
    private Socket so;
    private DamaCanvas canvas;

    public ClientReader(Socket so, DamaCanvas canvas) {
        this.canvas = canvas;
        this.so = so;
    }

    @Override
    protected Object doInBackground() throws Exception {

        log.debug("start client reader with socket = " + so);

        //leggo l'input stream e lo sscrivo sulla chat riga per riga
        InputStream is = null;
        try {

            is = so.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line = "";

            while ((line = in.readLine()) != null) {

                try {
                    log.debug("received = " + line);
                    String[] posArr = line.split("-");
                    int posX = (int) Double.parseDouble(posArr[0]);
                    log.debug("posX = " + posX);
                    int posY = (int) Double.parseDouble(posArr[1]);
                    log.debug("posY = " + posY);
                    canvas.setClick(new Point(posX, posY));
                    canvas.setMyClick(false);

                    canvas.repaint();
                } catch (Exception e) {
                    log.error(e);
                }
            }

        } catch (java.net.SocketException se) {
            log.error(se);
        } catch (IOException ex) {
            log.error(ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                log.error(ex);
            }
            return null;
        }

    }
}


  

