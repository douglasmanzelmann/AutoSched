import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by dmanzelmann on 5/14/2015.
 */
public class PresentationScheduleStatusModal extends JDialog {

    public PresentationScheduleStatusModal(String className, BufferedImage image) {
        BorderLayout layout = new BorderLayout();
        setTitle(className);
        ImagePanel screenshot = new ImagePanel(image);
        JButton closeButton = new JButton("Close");

        add(screenshot, BorderLayout.CENTER);
        add(closeButton, BorderLayout.PAGE_END);

        pack();
        setVisible(true);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        JFrame test = new JFrame();
        JButton openModal = new JButton("open");
        BufferedImage image;

        test.add(openModal);

        try {
            File img = new File("\\\\private\\Home\\Desktop\\test_buffered_image.JPG");
            image = ImageIO.read(img);

            openModal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PresentationScheduleStatusModal statusModal = new PresentationScheduleStatusModal("test", image);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        test.setDefaultCloseOperation(EXIT_ON_CLOSE);
        test.setVisible(true);
        test.validate();
    }
}
