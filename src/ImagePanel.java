import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by dmanzelmann on 5/14/2015.
 */
public class ImagePanel extends JPanel {
    private BufferedImage image;
    private JLabel picLabel;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        picLabel = new JLabel(new ImageIcon(image));
        add(picLabel);
    }
}
