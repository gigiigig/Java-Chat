/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chatclient.game.dama;

/**
 *
 * @author Luigi
 */
/**
 * Rappresenta una pedina,contiene il colore e la posizione 
 * sulla scacchiera  
 * @author Luigi
 */
 public class Pedina {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int x;
    int y;
    int colore;
    boolean dama;

    public Pedina(int x, int y, int colore) {
        this.x = x;
        this.y = y;
        this.colore = colore;
        this.dama = false;

    }

    public Pedina(int x, int y, int colore, boolean dama) {
        this.x = x;
        this.y = y;
        this.colore = colore;
        this.dama = dama;
    }

    

     // <editor-fold defaultstate="collapsed" desc="Getter and Setter">
     public int getColore() {
         return colore;
     }

     public void setColore(int colore) {
         this.colore = colore;
     }

     public int getX() {
         return x;
     }

     public void setX(int x) {
         this.x = x;
     }

     public int getY() {
         return y;
     }

     public void setY(int y) {
         this.y = y;
     }

     public boolean isDama() {
         return dama;
     }

     public void setDama(boolean dama) {
         this.dama = dama;
     }
// </editor-fold>



    public static int revertColore(int colore) {
        if (colore == BLACK) {
            return WHITE;
        }
        if (colore == WHITE) {
            return BLACK;
        }
        return -1;
    }//    public static void main(String[] args) {
//        JFrame jFrame = new JFrame();
//        jFrame.setSize((int) ((640 * Canvas.SCALEFACTOR + Canvas.WINDOWX)), (int) ((640 * Canvas.SCALEFACTOR + Canvas.WINDOWY)));
//        jFrame.setContentPane(new Canvas(),);
//        jFrame.setResizable(false);
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jFrame.setVisible(true);
//    }
}
