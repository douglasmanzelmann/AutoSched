import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by dmanzelmann on 5/6/2015.
 */
public class AutoSchedGUI extends JFrame implements ActionListener {
    AutoSched sched;
    List<Listing> schedule;

    public AutoSchedGUI() throws InterruptedException {
        System.setProperty("jsse.enableSNIExtension", "false");
        sched = new AutoSched();

        // Two columns: One with the schedule listing; Two with the completion status
        JPanel displaySchedulePanel = new JPanel(new GridLayout(0, 2));
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
        JLabel userNameLabel = new JLabel("Select username");
        //JTextField userNameField = new JTextField(10);
        JComboBox userNameBox = new JComboBox(Profiles.getProfiles());
        JLabel passwordLabel = new JLabel("Enter password");
        JPasswordField passwordField = new JPasswordField(10);
        readSchedPanelLogin.add(userNameLabel);
        readSchedPanelLogin.add(userNameBox);
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
                String username = (String)userNameBox.getSelectedItem();
                String password = new String(passwordField.getPassword());
                sched.readSched(username, password, year, month, day);
                schedule = Profiles.filter((String) username, sched.getListings());


                for (Listing l : schedule) {
                    JButton current = new JButton();
                    StatusButton currentStatus = new StatusButton("Not Started");
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
                    l.addObserver(currentStatus);
                    displaySchedulePanel.add(current);
                    displaySchedulePanel.add(currentStatus);

                }
                //listingScrollPane.repaint();
                revalidate();
                repaint();
                sched.loginToMediasite(username, password);
                sched.createMediasitePresentations(schedule, true);
            }
        });

        readSchedPanel.add(readSchedDates);
        readSchedPanel.add(startReadSched);


        // Panel tmsSched Sched
        JPanel tmsSchedPanel = new JPanel();
        tmsSchedPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(
                                EtchedBorder.RAISED, Color.GRAY, Color.DARK_GRAY),
                        "TMS"));

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

    public void actionPerformed(ActionEvent e) {

    }



    public static void main(String[] args) throws InterruptedException{
        AutoSchedGUI autoSchedGUI = new AutoSchedGUI();
    }
}
