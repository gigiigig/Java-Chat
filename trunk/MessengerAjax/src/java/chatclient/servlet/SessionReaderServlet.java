/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.servlet;

import chatclient.commons.MessageToJson;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class SessionReaderServlet extends HttpServlet {

    private Log log = LogFactory.getLog(SessionReaderServlet.class);
    //TEMPI IN MILLISECONDI
    public static int POLLING_TIME = 1000;
    public static int CONNESSION_CLOSING_TIME = 10000;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //PrintWriter writer = response.getWriter();

            log.debug("start,mi metto in attesa di un nuovo messaggio nella coda");

            Date start = new Date();
            request.getSession().setAttribute("lastStart", start);

            ConcurrentLinkedQueue<String> messagesqQueue = (ConcurrentLinkedQueue<String>) request.getSession().getAttribute("messages");
            MESSAGE current = null;
            String messageSt = "";
            boolean status = true;

            //ogni quanti giri a vuoto verifico la connessione
            int roundToVerifi = CONNESSION_CLOSING_TIME / POLLING_TIME;

            while (status) {
                log.debug("queue size [" + messagesqQueue.size() + "]");

                int cont = 0;
                while (messagesqQueue.isEmpty()) {
//                log.debug("waiting session message");
                    try {
                        Thread.sleep(POLLING_TIME);
                        if (cont >= roundToVerifi) {
                            //verifico la connessione
                            log.debug("chiudo la connessione");
                            return;
                        }
                        cont++;
                    } catch (InterruptedException ex) {
                        log.error(ex);
                    }
                }
                messageSt = messagesqQueue.poll();
                current = MessageManger.parseXML(messageSt);

                String messageJSON = MessageToJson.messageToJson(current);
                log.debug("messageJSON [" + messageJSON + "]");
                response.getOutputStream().write(messageJSON.getBytes("utf8"));
//            writer.flush();
//            if (messagesqQueue.size() == 0) {
                status = false;
//            }
            }
//            writer.close();
        } catch (Exception exception) {
            log.error(exception);
            log.error("choususra inaspettata");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    }
