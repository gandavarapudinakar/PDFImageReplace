package com.zessta;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

/**
 * PDFImageTextReplace
 */
public class PDFImageTextReplace {
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static void main(String[] args) {
        try {
            String imageUrl = "adhar1.jpg";
            Image image = ImageIO.read(new File(imageUrl));
            BufferedImage bi = toBufferedImage(image);
            ITesseract instance = new Tesseract();
            instance.setDatapath("C:\\Program Files (x86)\\Tesseract-OCR");
            instance.setLanguage("eng");
            //searching for adhar number by iterating text
            for (Word word : instance.getWords(bi, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
                Rectangle rect = word.getBoundingBox();
                System.out.println(rect.getMinX() + "," + rect.getMaxX() + "," + rect.getMinY() + "," + rect.getMaxY()
                        + ": " + word.getText());
                Pattern adharNumber = Pattern.compile("\\d{4}.\\d{4}.\\d{4}");
                //if string of word matches to adhar number replace with the black image
                if (adharNumber.matcher(word.getText()).find()) {
                    System.out.println(word.getText());
                    final BufferedImage bfimage = ImageIO.read(new File(imageUrl));
                    Graphics g = bfimage.getGraphics();
                    BufferedImage blackimage = new BufferedImage((int) (rect.getMaxX() - rect.getMinX()),
                            (int) (rect.getMaxY() - rect.getMinY()), BufferedImage.OPAQUE);
                    g.drawImage(blackimage, (int) rect.getMinX(), (int) rect.getMinY(), null);
                    g.dispose();
                    ImageIO.write(bfimage, "jpg", new File("adharHidden1.jpg"));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}