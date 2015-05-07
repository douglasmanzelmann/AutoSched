import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Created by dmanzelmann on 5/6/2015.
 */
public class AutoSchedGUI extends JFrame  {
    AutoSched sched;
    List<Listing> schedule;

    public AutoSchedGUI() throws InterruptedException {
        System.setProperty("jsse.enableSNIExtension", "false");
        sched = new AutoSched();
        JPanel displaySchedulePanel = new JPanel(new GridLayout(0, 1));
        JScrollPane listingScrollPane = new JScrollPane(displaySchedulePanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        //JPanel schedulePanel = new JPanel();
        //JTextArea listingTextArea = new JTextArea();


        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("Weekly Schedule");
        setSize(800, 800);
        setMinimumSize(new Dimension(800, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel Read Sched
        JPanel readSchedPanel = new JPanel();
        readSchedPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(
                                EtchedBorder.RAISED, Color.GRAY, Color.DARK_GRAY),
                                "Read Schedule"));


        // Login info for ReadSched
        JPanel readSchedPanelLogin = new JPanel();
        JLabel userNameLabel = new JLabel("Enter username");
        JTextField userNameField = new JTextField(10);
        JLabel passwordLabel = new JLabel("Enter password");
        JPasswordField passwordField = new JPasswordField(10);
        readSchedPanelLogin.add(userNameLabel);
        readSchedPanelLogin.add(userNameField);
        readSchedPanelLogin.add(passwordLabel);
        readSchedPanelLogin.add(passwordField);
        readSchedPanel.add(readSchedPanelLogin);

        // Dates for ReadSched
        JPanel readSchedDates = new JPanel();
        JTextField readSchedMonth = new JTextField("Month", 5);
        JTextField readSchedDay = new JTextField("Day", 5);
        JTextField readSchedYear = new JTextField("Year", 5);
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

        // Start ReadSched with input
        JButton startReadSched = new JButton("Read Schedule");
        startReadSched.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int month = Integer.parseInt(readSchedMonth.getText());
                int day = Integer.parseInt(readSchedDay.getText());
                int year = Integer.parseInt(readSchedYear.getText());

                // do not do this. defeats the purpose of getPassword.
                // will need to refactor code for a char[] later.
                String password = new String(passwordField.getPassword());

                //sched.visitPortalWeek(year, month, day);
                //sched.loginToPortal(userNameField.getText(), password);
                sched.readSched(userNameField.getText(), password, year, month, day);
                schedule = sched.getListings();


                for (Listing l : schedule) {
                    JButton current = new JButton();
                    current.setHorizontalAlignment(SwingConstants.LEFT);

                    if (l.getActivity().equals("Mediasite"))
                        current.setBackground(Color.GREEN);
                    else if (l.getActivity().equals("Videoconference"))
                        current.setBackground(Color.CYAN);
                    else if (l.getActivity().equals("Pre-record"))
                        current.setBackground(Color.ORANGE);
                    else
                        current.setBackground(Color.gray);

                    current.setText(l.toString());
                    displaySchedulePanel.add(current);

                }

                listingScrollPane.repaint();
                revalidate();
                repaint();
            }
        });

        readSchedPanel.add(readSchedDates);
        readSchedPanel.add(startReadSched);

        // Panel Mediasite Sched
        JPanel tmsSchedPanel = new JPanel();
        tmsSchedPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(
                                EtchedBorder.RAISED, Color.GRAY, Color.DARK_GRAY),
                        "TMS Login"));

        JLabel tmsUserNameLabel = new JLabel("Enter username");
        JTextField tmsUserNameField = new JTextField(10);
        JLabel tmsPasswordLabel = new JLabel("Enter password");
        JPasswordField tmsPasswordField = new JPasswordField(10);
        tmsSchedPanel.add(tmsUserNameLabel);
        tmsSchedPanel.add(tmsUserNameField);
        tmsSchedPanel.add(tmsPasswordLabel);
        tmsSchedPanel.add(tmsPasswordField);

        // Create a login panel and add the two login sections
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(readSchedPanel, BorderLayout.PAGE_START);
        loginPanel.add(tmsSchedPanel, BorderLayout.PAGE_END);

        // Create a content panel and add the individual panels


        // Display the content of the schedule
        //JPanel displaySchedulePanel = new JPanel(new GridLayout(0, 1));
        //JTextArea listingTextArea = new JTextArea();
        //listingTextArea.setText("");
        //JScrollPane listingScrollPane = new JScrollPane(displaySchedulePanel);
        //JPanel schedulePanel = new JPanel();
        //schedulePanel.add(listingScrollPane);
       //displaySchedulePanel.add(listingScrollPane);

        // Display Progress Bars
        JProgressBar readSchedProgressBar = new JProgressBar();
        readSchedProgressBar.setStringPainted(true);
        JProgressBar mediasiteSchedProgressBar = new JProgressBar();
        mediasiteSchedProgressBar.setStringPainted(true);
        JProgressBar tmsSchedProgressBar = new JProgressBar();
        tmsSchedProgressBar.setStringPainted(true);


        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(loginPanel, BorderLayout.PAGE_START);
        contentPanel.add(listingScrollPane, BorderLayout.CENTER);
        add(contentPanel);
        pack();
        setVisible(true);
        validate();
    }



    public static void main(String[] args) throws InterruptedException{
        AutoSchedGUI autoSchedGUI = new AutoSchedGUI();
    }
}
