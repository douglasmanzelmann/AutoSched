import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by dmanzelmann on 5/6/2015.
 */
public class AutoSchedGUI extends JFrame  {
    AutoSched sched;

    public AutoSchedGUI() throws InterruptedException {
        //sched = new AutoSched();

        pack();
        setLocationRelativeTo(null);
        setTitle("Weekly Schedule");
        setSize(600, 600);
        setMinimumSize(new Dimension(600, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());

        // Panel Read Sched
        JPanel readSchedPanel = new JPanel();

        // Login info for ReadSched
        JPanel readSchedPanelLogin = new JPanel();
        JLabel userNameLabel = new JLabel("Enter username");
        JTextField userNameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Enter password");
        JTextField passwordField = new JTextField(10);
        readSchedPanelLogin.add(userNameLabel);
        readSchedPanelLogin.add(userNameField);
        readSchedPanelLogin.add(passwordLabel);
        readSchedPanelLogin.add(passwordField);
        readSchedPanel.add(readSchedPanelLogin);

        // Dates for ReadSched
        JPanel readSchedDates = new JPanel();
        JTextField readSchedMonth = new JTextField("Month");
        JTextField readSchedDay = new JTextField("Day");
        JTextField readSchedYear = new JTextField("Year");
        readSchedMonth.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                readSchedMonth.setText("");
            }
        });
        readSchedDay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                readSchedDay.setText("");
            }
        });
        readSchedYear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                readSchedYear.setText("");
            }
        });
        readSchedDates.add(readSchedMonth);
        readSchedDates.add(readSchedDay);
        readSchedDates.add(readSchedYear);
        readSchedPanel.add(readSchedDates);


        add(readSchedPanel);
        validate();
    }



    public static void main(String[] args) throws InterruptedException{
        AutoSchedGUI autoSchedGUI = new AutoSchedGUI();
    }
}
