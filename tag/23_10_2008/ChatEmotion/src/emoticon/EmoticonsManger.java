/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emoticon;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;
import emoticon.xml.EMOTIONXML;
import emoticon.xml.EmotionType;
import emoticon.xml.ObjectFactory;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
//import net.jmge.gif.Gif89Encoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Luigi
 */
public class EmoticonsManger {

    private LinkedList<Emoticon> emoticons;
    private static Log log = LogFactory.getLog(EmoticonsManger.class);
    public static String EMOTICONSPATH = "images/emoticons/";
    public static int MAX_IMAGE_DIMENSION = 48;

    public EmoticonsManger() {
        loadEmotionsList();
    }

    /**
     * aggiunge al documento uno stile per ogni emoction
     * col nome del emoction = a quello dello stile
     * @param doc
     */
    public void loadEmoticonsOnDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");


        //iserisco gli stili,2 per icona per evitare che
        //ne venga visualizzata solo una
        for (Emoticon emoction : emoticons) {
            ImageIcon icon = emoction.getImageIcon();
            Style s = doc.addStyle(emoction.getShortcut(), regular);
            StyleConstants.setIcon(s, icon);
            s = doc.addStyle(emoction.getShortcut() + "Alt", regular);
            StyleConstants.setIcon(s, icon);
        }
    }

    /**
     * aggiunge al documento uno stile per ogni emoction
     * col nome del emoction = a quello dello stile
     * @param doc
     */
    public void loadEmotionsOnDocument(StyledDocument doc, List<Emoticon> emoticons) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");


        //iserisco gli stili,2 per icona per evitare che
        //ne venga visualizzata solo una
        for (Emoticon emoticon : emoticons) {

            ImageIcon icon = emoticon.getImageIcon();
            Style s = doc.addStyle(emoticon.getShortcut(), regular);
            StyleConstants.setIcon(s, icon);
            s = doc.addStyle(emoticon.getShortcut() + "Alt", regular);
            StyleConstants.setIcon(s, icon);
            log.debug("loaded emoticon Style : " + emoticon.getName());
        }
    }

    /**
     * inserisce nel doc il testo con le emoction che trova nelle lista
     * @param doc Lo styled document in cui ionserire il testo
     * @param message
     */
    public synchronized void insertChatTextWithEmoticons(StyledDocument doc, String message, String style, List<Emoticon> emoticonsExt) {
        log.debug("init");

        boolean alternate = false;
        try {
            doc.insertString(doc.getLength(), message, doc.getStyle(style));
        } catch (BadLocationException ex) {
            log.error(ex);
        }

        //inserisco le emotion solo nell'ulitmo messaggio
        int startIndex = doc.getLength() - message.length();
        int index = startIndex;
        int oldIndex = startIndex;

        List<Emoticon> toInsert = new LinkedList<Emoticon>();
        toInsert.addAll(emoticons);
        if (emoticonsExt != null) {
            toInsert.addAll(emoticonsExt);
        }

        //scandisco e inserisco tutte le emoticon da inserire
        for (Emoticon emoticon : toInsert) {
            try {
                while (oldIndex < doc.getLength() && (index = doc.getText(0, doc.getLength()).indexOf(emoticon.getShortcut(), oldIndex)) != -1) {
                    try {

                        log.trace("index = " + index);
                        log.trace("old index = " + oldIndex);
                        doc.remove(index, emoticon.getShortcut().length());

                        if (alternate) {
                            doc.insertString(index, emoticon.getShortcut(), doc.getStyle(emoticon.getShortcut() + "Alt"));
                        } else {
                            doc.insertString(index, emoticon.getShortcut(), doc.getStyle(emoticon.getShortcut()));
                        }
                        oldIndex = index + emoticon.getShortcut().length();

                        alternate = !alternate;
                    } catch (BadLocationException ex) {
                        log.error(ex);
                    }
                }
                index = 0;
                oldIndex = startIndex;
            } catch (BadLocationException ex) {
                Logger.getLogger(EmoticonsManger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void repleceIEmotionsInText(StyledDocument doc) {
        try {
            int index = 0;
            int oldIndex = 0;

            boolean alternate = false;

            for (Emoticon emotion : emoticons) {

                while (oldIndex < doc.getLength() && (index = doc.getText(0, doc.getLength()).indexOf(emotion.getShortcut(), oldIndex)) != -1) {
                    try {

                        log.trace("index = " + index);
                        log.trace("old index = " + oldIndex);
                        doc.remove(index, emotion.getShortcut().length());

                        if (alternate) {
                            doc.insertString(index, emotion.getShortcut(), doc.getStyle(emotion.getShortcut() + "Alt"));
                        } else {
                            doc.insertString(index, emotion.getShortcut(), doc.getStyle(emotion.getShortcut()));
                        }
                        oldIndex = index + emotion.getShortcut().length();

                        alternate = !alternate;
                    } catch (BadLocationException ex) {
                        log.error(ex);
                    }
                }
                index = 0;
                oldIndex = 0;
            }
        } catch (BadLocationException ex) {
            log.error(ex);
        }
    }

    public void loadEmotionsList() {
        emoticons = new LinkedList<Emoticon>();
        EMOTIONXML emotionxml = loadEmotionXML();
        List<EmotionType> emotionTypes = emotionxml.getEmotions();

        for (EmotionType emotionType : emotionTypes) {
            Emoticon emotion = new Emoticon();
            emotion.setName((String) emotionType.getName());
            emotion.setShortcut((String) emotionType.getShortcut());
            emotion.setFileName((String) emotionType.getImage());
            File file = new File(Util.getInstance().getPath() + EMOTICONSPATH + emotion.getFileName());
            if (file.isFile()) {
                try {
                    ImageIcon icon = new ImageIcon(file.getPath());
                    emotion.setImageIcon(icon);
                    emoticons.add(emotion);
                    log.trace("loaded emoction : " + emotion.getFileName() + " " + emotion.getName());
                } catch (Exception e) {
                    log.warn("unable to load image : " + emotion.getFileName());
                }
            } else {
                log.warn("unable to load image : " + emotion.getFileName());
            }
        }

    }

    public EMOTIONXML loadEmotionXML() {
        EMOTIONXML emotionxml = new ObjectFactory().createEMOTIONXML();
        File file = new File(Util.getInstance().getPath() + EMOTICONSPATH + "emotions.xml");
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(emotionxml.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            emotionxml = (EMOTIONXML) unmarshaller.unmarshal(file); //NOI18N

        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            log.warn("error on loading file : " + file.getPath() + " : " + ex); //NOI18N

        } finally {
            return emotionxml;
        }
    }

    public void writeEmoticonXML(EMOTIONXML emotionxml) {

        File file = new File(Util.getInstance().getPath() + EMOTICONSPATH + "emotions.xml");

        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(emotionxml.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N

            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(emotionxml, new FileOutputStream(file));

            log.debug("xml writed");

        } catch (FileNotFoundException ex) {
            log.error(ex);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            log.warn("file : " + file.getPath() + " not find"); //NOI18N

        }
    }

    public void copyEmoticonFile(File source, String fileName) {
        if (!source.getName().endsWith(".gif")) {
            try {

                int pointIndx = fileName.lastIndexOf(".");
                String format = fileName.substring(pointIndx + 1, fileName.length());
                log.debug("image format : " + format);


                File destination = new File(Util.getInstance().getPath() + EMOTICONSPATH + fileName);
                BufferedImage bufferedImage = ImageIO.read(source);

                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                float scaleFactor = 1;

                if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
                    if (width > height) {
                        scaleFactor = (float) MAX_IMAGE_DIMENSION / width;
                    } else {
                        scaleFactor = (float) MAX_IMAGE_DIMENSION / height;
                    }
                }

                width = (int) (width * scaleFactor);
                height = (int) (height * scaleFactor);


                if (scaleFactor != 1) {
                    Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
                    bufferedImage = toBufferedImage(image);
                }


                PNGImageWriter writer = new PNGImageWriter(new PNGImageWriterSpi());
                writer.setOutput(ImageIO.createImageOutputStream(destination));
                ImageWriteParam defaultWriteParam = writer.getDefaultWriteParam();

                try {
                    defaultWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    defaultWriteParam.setCompressionQuality(0.5f);
                } catch (Exception e) {
                    log.warn(e);
                }

//                writer.write(null, new IIOImage(bufferedImage, null, null), defaultWriteParam);
                writer.write(bufferedImage);
                writer.dispose();
//                ios.close();

            } catch (FileNotFoundException ex) {
                log.error(ex);
            } catch (IOException ex) {
                log.error(ex);
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            try {


                File destination = new File(Util.getInstance().getPath() + EMOTICONSPATH + fileName);
                gifInputToOutput(source, destination);

            } catch (FileNotFoundException ex) {
                log.error(ex);
            } catch (IOException ex) {
                log.error(ex);
            }


        }
    }

    public List<Emoticon> emoticonsInDoc(String text) {


        List<Emoticon> toReturn = new LinkedList<Emoticon>();

        int cont = 0;
        log.debug("text : " + text);

        for (Emoticon emotion : emoticons) {
            if (text.contains(emotion.getShortcut()) && cont > 63) {
                toReturn.add(emotion);
            }
            log.trace("cont : " + cont);
            log.trace("shortcut : " + emotion.getShortcut());
            cont++;
        }
        return toReturn;


    }
    // <editor-fold defaultstate="collapsed" desc=" Getter and Setter ">

    public LinkedList<Emoticon> getEmotions() {
        return emoticons;
    }

    public void setEmotions(LinkedList<Emoticon> emotions) {
        this.emoticons = emotions;
    }

// </editor-fold>
    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.TRANSLUCENT;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics2D g = bimage.createGraphics();
        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    // This method returns a buffered image with the contents of an image and centered to passed dimensions
    public static BufferedImage toBufferedImage(Image image, int width, int height) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.TRANSLUCENT;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            log.debug("buffered image size : h = " + height + " w = " + width);
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics2D g = bimage.createGraphics();
        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));

        // Paint the image onto the buffered image
        g.drawImage(image, width, height, null);
        g.dispose();

        return bimage;
    }

    // This method returns true if the specified image has transparent pixels
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    public static void main(String[] args) {


        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        ImageIcon icon = new ImageIcon(Util.getInstance().getPath() + EMOTICONSPATH);
//         ImageIcon


//        GifImageDecoder  decoder = new GifImageDecoder(ImageIO.createImageInputStream(), arg1);


    }

    public static void gifInputToOutput(File source, File destination) throws FileNotFoundException, IOException {

        GifDecoder decoder = new GifDecoder();
        decoder.read(new FileInputStream(source));

        //crete encoder and set loop repeat
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(0);
        encoder.setQuality(25);

        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        encoder.start(fileOutputStream);

        //leggo i frame col decoder e li rimetto scalati nell'encoder
        for (int i = 0; i < decoder.getFrameCount(); i++) {

            BufferedImage bufferedImage = decoder.getFrame(i);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            float scaleFactor = 1;

            if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
                if (width > height) {
                    scaleFactor = (float) MAX_IMAGE_DIMENSION / width;
                } else {
                    scaleFactor = (float) MAX_IMAGE_DIMENSION / height;
                }
            }

            width = (int) (width * scaleFactor);
            height = (int) (height * scaleFactor);


            if (scaleFactor != 1) {
                Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
                bufferedImage = toBufferedImage(image);
            }

            encoder.addFrame(bufferedImage);

            //cambio il delay ai frame che lo hanno a zero
            log.debug(decoder.getDelay(i));
            if (decoder.getDelay(i) == 0) {
                encoder.setDelay(100);
            } else {
                encoder.setDelay(decoder.getDelay(i));
            }
            encoder.setTransparent(Color.WHITE);


        }

        //termino e chiudo
        encoder.finish();
        fileOutputStream.close();
    }

    public static void gifInputToOutput(File source, OutputStream destination) throws FileNotFoundException, IOException {

        GifDecoder decoder = new GifDecoder();
        decoder.read(new FileInputStream(source));

        //crete encoder and set loop repeat
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(0);

        encoder.start(destination);

        //leggo i frame col decoder e li rimetto scalati nell'encoder
        for (int i = 0; i < decoder.getFrameCount(); i++) {

            BufferedImage bufferedImage = decoder.getFrame(i);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            float scaleFactor = 1;

            if (width > MAX_IMAGE_DIMENSION || height > MAX_IMAGE_DIMENSION) {
                if (width > height) {
                    scaleFactor = (float) MAX_IMAGE_DIMENSION / width;
                } else {
                    scaleFactor = (float) MAX_IMAGE_DIMENSION / height;
                }
            }

            width = (int) (width * scaleFactor);
            height = (int) (height * scaleFactor);


            if (scaleFactor != 1) {
                Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
                bufferedImage = toBufferedImage(image);
            }
            encoder.addFrame(bufferedImage);

            //cambio il delay ai frame che lo hanno a zero
            if (decoder.getDelay(i) == 0) {
                encoder.setDelay(200);
            } else {
                encoder.setDelay(decoder.getDelay(i));
            }
        }

        //termino e chiudo
        encoder.finish();
    }
}
