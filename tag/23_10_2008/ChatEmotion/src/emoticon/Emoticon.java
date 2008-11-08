/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package emoticon;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Luigi
 */
public class Emoticon {

    private String name;
    private String shortcut;
    private ImageIcon imageIcon;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
    
    
    
}
