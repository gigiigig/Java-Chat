/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient.game.dama;

import chatclient.game.Canvas;
import chatclient.game.GameHome;
import chatcommons.datamessage.MESSAGE;
import chatcommons.datamessage.MessageManger;
import static chatcommons.Commands.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.OutputStream;
import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe principale contine l'area e la logica di gioco
 * @author Luigi
 */
public class DamaCanvas extends JPanel implements Canvas {

    private Log log = LogFactory.getLog(this.getClass());
    public static final int WINDOWX = 6;
    public static final int WINDOWY = 26;
    public static final double SCALEFACTOR = 0.5;
    public static int TRASLAZIONEPEDINA = (int) (5 * SCALEFACTOR);
    public static final int LATOCASELLA = (int) (80 * SCALEFACTOR);
    public static final int RAGGIOPEDINA = (int) ((int) 70 * SCALEFACTOR);
    public static final Color BLACKPLAYER = Color.BLUE;
    public static final Color WHITEPLAYER = Color.WHITE;
    public static final Color SELECTEDPEDINA = Color.PINK;
    public static final int WHITEDIRECTION = -1;
    public static final int BLACKDIRECTION = 1;
    public static final int MESSAGEHEIGTH = (int) (70 * SCALEFACTOR);
    private int activePlayer;
    private Point click;
    private Pedina[][] pedine;
    private Pedina selected;
    private Pedina toMove;
    private OutputStream os;
    private int myColor;
    private boolean myClick;
    private GameHome home;
    private String message;
    private String nickAdversar;

    public DamaCanvas(GameHome home, int myColor, String nickAdversar) {
        startPedine();
        this.addMouseListener(new ClickListener());
        selected = null;
        this.nickAdversar = nickAdversar;
        this.myColor = myColor;
        activePlayer = Pedina.WHITE;
        this.home = home;
        if (myColor == Pedina.BLACK) {
            //modifico la trslazione delle pedine rigirate perchè vangono spostate
            TRASLAZIONEPEDINA = (int) ((5 * SCALEFACTOR) + (5 * SCALEFACTOR) / 100 * 30);
        }
        if (myColor == Pedina.WHITE) {
            message = "Partitia iniziata,inizi tu col binaco";
        } else {
            message = "Partitia iniziata,inizia il tuo avversario col binaco";
        }


    }

    /**
     * scrive il messaggio di gioco nell'area selezionatoS
     * @param g2
     */
    public void insertMessage(Graphics2D g2, String message) {

        g2.setColor(Color.RED);
        g2.setFont(new Font("arial", Font.BOLD, 18));

        g2.drawString(message, 10, 8 * LATOCASELLA + (int) (MESSAGEHEIGTH * 0.8));
    }

    /**
     * Rigira la grafica della scacchiera
     * @param g2
     */
    public void reverseCanvas(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(180), (640 * DamaCanvas.SCALEFACTOR) / 2, (int) ((640 * DamaCanvas.SCALEFACTOR)) / 2);
        g2.setTransform(transform);
//        TRASLAZIONEPEDINA = TRASLAZIONEPEDINA + 1;
    }

    /**
     * il metodo paint contiene tutta la logica del gioco
     * @param g
     */
    @Override
    public void paint(Graphics g) {


        log.debug("paint");

        Graphics2D g2 = (Graphics2D) g;

        //creo lo spazio che contiene i messaggi di gioco
        //i messaggi vanno settati prima di rifgirare la scacchiera
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 8 * LATOCASELLA + getY(), 8 * LATOCASELLA, 30);

        insertMessage(g2, message);

        //se il colore del giocatore è il nbero devo invertire 
        //la scacchiera
        if (myColor == Pedina.BLACK) {
            reverseCanvas(g2);
        }


        boolean alternate = true;

        //creo le caselle
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (alternate) {
                    g2.setColor(Color.CYAN);
                } else {
                    g2.setColor(Color.BLACK);
                }

                g2.fillRect(i * LATOCASELLA + getX(), j * LATOCASELLA + getY(), LATOCASELLA, LATOCASELLA);
                alternate = !alternate;
            }
            alternate = !alternate;
        }

        //riempo con le pedine esistenti
        for (int i = 0; i < 8; i++) {
            Pedina[] pedinas = pedine[i];
            for (int j = 0; j < 8; j++) {

                Pedina pedina = pedinas[j];
                if (pedina != null) {
                    if (pedina.colore == Pedina.WHITE) {
                        g2.setColor(WHITEPLAYER);
                    } else {
                        g2.setColor(BLACKPLAYER);
                    }
                    g2.fillOval((int) (j) * LATOCASELLA + TRASLAZIONEPEDINA, (int) (i) * LATOCASELLA + TRASLAZIONEPEDINA, RAGGIOPEDINA, RAGGIOPEDINA);
                }
            }

        }



        boolean presente = false;
        boolean valid = false;

        //verifico se è stata cliccata qualche casella
        if (click != null) {

            //verifico se sesiste la pedina nella casella cliccata
            Pedina clicked = pedine[(int) click.getY()][(int) click.getX()];
            if (clicked != null) {
                log.debug("clicked X" + clicked.getX());
                log.debug("clicked Y" + clicked.getY());
            }
            presente = clicked != null;
            valid = (clicked != null && clicked.getColore() == activePlayer);

            log.debug("presente = " + presente);
            log.debug("valid = " + valid);

            if (valid) {
                //se il click è su una casella con una pedina presente la seleziono
                g2.setColor(SELECTEDPEDINA);
                g2.fillOval((int) click.getX() * LATOCASELLA + TRASLAZIONEPEDINA, (int) click.getY() * LATOCASELLA + TRASLAZIONEPEDINA, RAGGIOPEDINA, RAGGIOPEDINA);
                selected = clicked;
            } else if (!presente) {
                //altrimenti il click è su una csella libera
                //verifco se c'è una pedina selezionata e 
                //se la casella libera è valida la sposto
                //sennò cancello se c'è la pedina selezionata
                if (selected != null) {

                    //verifico se lo spostamento è richiesto su una casella valida 
                    int direction = 0;
                    if (selected.getColore() == Pedina.WHITE) {
                        direction = WHITEDIRECTION;
                    } else {
                        direction = BLACKDIRECTION;
                    }
                    boolean validX = Math.abs(click.getX() - selected.getX()) == 1;
                    boolean validY = (click.getY() - selected.getY()) * direction == 1;
                    log.debug("validX = " + (click.getX() - selected.getX()));
                    log.debug("validY = " + (click.getY() - selected.getY()));

                    //verificano se c'è una posizione libera per mangiare
                    boolean isEatenX = Math.abs(click.getX() - selected.getX()) == 2;
                    boolean isEatenY = (click.getY() - selected.getY()) * direction == 2;
                    int eatenedY = (int) (click.getY() + (-1 * direction));
                    int eatenedX = (int) (click.getX()) - ((int) (click.getX() - selected.getX()) / 2);

                    //devo anche verificare che ci si una pedina avversaria da mangiare
                    Pedina eatenable = pedine[eatenedY][eatenedX];
                    log.debug("eatenable.getX() = " + eatenedX);
                    log.debug("eatenable.getY() = " + eatenedY);

                    boolean isEaten = eatenable != null && eatenable.getColore() != selected.getColore() && isEatenX && isEatenY;

                    //se una delle condizioni valide per lo spostamento è 
                    //verificata inizio lo spostamento
                    if ((validX && validY) || (isEaten)) {

                        if (isEatenX && isEatenY) {
                            //verifico se è stata mangiata una pedina
                            log.debug("tentativo di mangiare");
                            int eatenX = selected.getX() + (int) (click.getX() - selected.getX()) / 2;
                            int eatenY = selected.getY() + (int) (click.getY() - selected.getY()) / 2;

                            //verifico il colore della casella della pedina mangiata
                            //TODO : IDIOTA MUOVI SEPRE SULLLE CASELLE CHIARE!!! 
                            if ((eatenX % 2 == 0) == (eatenY % 2 == 0)) {
                                g2.setColor(Color.CYAN);
                            } else {
                                g2.setColor(Color.BLACK);
                            }
                            //elimino la pedina mangiata
                            g2.fillRect(eatenX * LATOCASELLA + getX(), eatenY * LATOCASELLA + getY(), LATOCASELLA, LATOCASELLA);
                            pedine[eatenY][eatenX] = null;
                        }

                        //verifico il colore della pedina da muovere
                        if (selected.colore == Pedina.WHITE) {
                            g2.setColor(WHITEPLAYER);
                        } else {
                            g2.setColor(BLACKPLAYER);
                        }

                        log.debug("getX()%2 = " + selected.getX() % 2);
                        log.debug("getY()%2 = " + selected.getY() % 2);

                        //verifco il colore della casella della pedina da spostare
                        //TODO : IDIOTA MUOVI SEPRE SULLLE CASELLE CHIARE!!! 

                        if ((selected.getX() % 2 == 0) == (selected.getY() % 2 == 0)) {
                            g2.setColor(Color.CYAN);
                        } else {
                            g2.setColor(Color.BLACK);
                        }

                        //elimino la pedina mossa
                        g2.fillRect(selected.getX() * LATOCASELLA + getX(), selected.getY() * LATOCASELLA + getY(), LATOCASELLA, LATOCASELLA);
                        pedine[selected.getY()][selected.getX()] = null;
                        activePlayer = Pedina.revertColore(selected.getColore());

                        //lancio il thread per l'animazione dello spostamento
                        toMove = new Pedina(selected.getX() * LATOCASELLA, selected.getY() * LATOCASELLA, selected.getColore());
                        new Thread(new TranslatePedina(this, toMove, (int) click.getX(), (int) click.getY())).start();

                        selected = null;


                    } else {
                        //se il click è su una casella non valida
                        //per lo spostamento canclello la selezione sulla pedina 
                        if (selected.colore == Pedina.WHITE) {
                            g2.setColor(WHITEPLAYER);
                        } else {
                            g2.setColor(BLACKPLAYER);
                        }

                        g2.fillOval(selected.getX() * LATOCASELLA + TRASLAZIONEPEDINA, selected.getY() * LATOCASELLA + TRASLAZIONEPEDINA, RAGGIOPEDINA, RAGGIOPEDINA);
                        selected = null;
                    }
                }


            }
        }
        if (toMove != null) {
            //canclello la selezione sulla pedina 
            if (toMove.colore == Pedina.WHITE) {
                g2.setColor(WHITEPLAYER);
            } else {
                g2.setColor(BLACKPLAYER);
            }

            g2.fillOval((int) toMove.getX() + TRASLAZIONEPEDINA, toMove.getY() + TRASLAZIONEPEDINA, RAGGIOPEDINA, RAGGIOPEDINA);
        }


//      
    }

    // <editor-fold defaultstate="collapsed" desc=" Gettert an Setter ">
    public Point getClick() {
        return click;
    }

    public void setClick(Point click) {
        this.click = click;
    }

    public Pedina[][] getPedine() {
        return pedine;
    }

    public void setPedine(Pedina[][] pedine) {
        this.pedine = pedine;
    }

    public Pedina getToMove() {
        return toMove;
    }

    public void setToMove(Pedina toMove) {
        this.toMove = toMove;
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public int getMyColor() {
        return myColor;
    }

    public void setMyColor(int myColor) {
        this.myColor = myColor;
    }

    public boolean isMyClick() {
        return myClick;
    }

    public void setMyClick(boolean myClick) {
        this.myClick = myClick;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public GameHome getHome() {
        return home;
    }

    public void setHome(GameHome home) {
        this.home = home;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickAdversar() {
        return nickAdversar;
    }

    public void setNickAdversar(String nickAdversar) {
        this.nickAdversar = nickAdversar;
    }
// </editor-fold>
    /**
     * Creo e riempo l'array delle pedine che rappresenta la partita
     */
    private void startPedine() {

        log.debug("start pedine");
        pedine =
                new Pedina[8][8];

        boolean alternate = true;
        for (int i = 0; i <
                3; i++) {
            for (int j = 0; j <
                    8; j++) {
                if (alternate) {
                    pedine[i][j] = new Pedina(j, i, Pedina.BLACK);
                }

                alternate = !alternate;
            }

            alternate = !alternate;
        }

        for (int i = 5; i <
                8; i++) {
            for (int j = 0; j <
                    8; j++) {
                if (alternate) {
                    pedine[i][j] = new Pedina(j, i, Pedina.WHITE);
                }

                alternate = !alternate;
            }

            alternate = !alternate;
        }




    }

    @Override
    public String toString() {

        StringBuffer toReturn = new StringBuffer();
        toReturn.append("\n");
        for (int i = 0; i < pedine.length; i++) {
            Pedina[] pedinas = pedine[i];
            for (int j = 0; j < pedinas.length; j++) {
                Pedina pedina = pedinas[j];
                if (pedina != null) {
                    toReturn.append("[ " + pedina.colore + " ]");
                } else {
                    toReturn.append("[   ]");
                }
            }
            toReturn.append("\n");
        }

        return toReturn.toString();
    }
}

/**
 * Questa classe anima lo spostamento della pedina,vine lanciata con un thread
 * @author Luigi
 */
class TranslatePedina implements Runnable {

    private Log log = LogFactory.getLog(this.getClass());
    DamaCanvas canvas;
    Pedina pedina;
    int goalX;
    int goalY;

    public TranslatePedina(DamaCanvas canvas, Pedina selected, int goalX, int goalY) {
        this.canvas = canvas;
        this.pedina = selected;
        this.goalX = goalX;
        this.goalY = goalY;
    }
    static int FRAMES = 30;

    /**
     * Il metodo che contiene l'animazione dela pedina
     */
    public void run() {
        log.debug("Thread is runnning");
        int x = pedina.getX();
        int y = pedina.getY();

        for (int i = 0; i <= FRAMES + (FRAMES * 20 / 100); i++) {

//            System.out.println("pedina.getX() = " + x);
            double xMov = ((goalX * DamaCanvas.LATOCASELLA - x) / FRAMES);
//            System.out.println("xmov = " + xMov);
            double yMov = ((goalY * DamaCanvas.LATOCASELLA - y) / FRAMES);

            canvas.getToMove().setX((int) ((canvas.getToMove().getX() + xMov)));
            canvas.getToMove().setY((int) ((canvas.getToMove().getY() + yMov)));
            canvas.repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                log.error(ex);
            }
        }


        canvas.setToMove(null);
        canvas.getPedine()[goalY][goalX] = new Pedina(goalX, goalY, pedina.getColore());

        //dopo il movimento della pedina imposto i nuovii messaggi
        if (canvas.getMyColor() == canvas.getActivePlayer()) {
            canvas.setMessage("Vai è il tuo turno!!");
        } else {
            canvas.setMessage("E' il turno del tuo avversario");
        }

        canvas.repaint();

        log.debug(canvas.toString());

    }
}

/**
 * Listener che ad ogni click del mouse sulla scacchiera 
 * setta su canvas la posizione del click e lancia il repaint()
 * @author Luigi
 */
class ClickListener extends MouseAdapter {

    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void mousePressed(MouseEvent e) {
//        super.mouseClicked(e);
        log.trace("Mouse clicked");
        DamaCanvas canvas = (DamaCanvas) e.getComponent();
        if (canvas.getMyColor() == canvas.getActivePlayer()) {

            Point point = null;
            int pointX = (int) e.getPoint().getX() / DamaCanvas.LATOCASELLA;
            int pointY = (int) e.getPoint().getY() / DamaCanvas.LATOCASELLA;
            if (canvas.getMyColor() == Pedina.WHITE) {
                point = new Point(pointX, pointY);
            } else {
                //se ho la scacchiera invertita inverto la posizione del click
                point = new Point(7 - pointX, 7 - pointY);
            }
            canvas.setClick(point);
            canvas.setMyClick(true);
            try {
//                canvas.getOs().write((point.getX() + "-" + point.getY() + "\n").getBytes(Util.DEFAULTENCODING));
//                String toSend = REQUEST + canvas.getHome().getNickAdeversar() + SEPARATOR + Request.DAMAPOSITION + SEPARATOR + (point.getX() + DIVISOR + point.getY()) + "\n";

                MESSAGE message = MessageManger.createRequest(Request.DAMAPOSITION, null, null);
                MessageManger.addReceiver(message, canvas.getNickAdversar());
//                message.getReceivers().add(canvas.getNickAdversar());
//                message.getParameters().add(0, "" + point.getX());
//                message.getParameters().add(1, "" + point.getY());
                MessageManger.addParameterAt(message,"X", "" + point.getX(), 0);
                MessageManger.addParameterAt(message,"Y", "" + point.getY(), 1);

                MessageManger.directWriteMessage(message, canvas.getOs());
            } catch (Exception e1) {
                log.error(e1);
            }


            canvas.repaint();
        }
    }
}




