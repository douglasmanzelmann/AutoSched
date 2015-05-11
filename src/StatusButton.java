import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by dmanzelmann on 5/11/2015.
 */
public class StatusButton extends JButton implements Observer  {
    public StatusButton(String text) {
        super(text);
    }

    public void update(Observable o, Object arg) {
        setText((String)arg);
    }
}
