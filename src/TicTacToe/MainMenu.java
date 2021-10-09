package TicTacToe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JFrame{

    private enum Mode {Player, AI}

    private JPanel panel;
    private JComboBox<String> roleList;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JTextField ipField;
    private JTextField portField;
    private JButton startButton;

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }


    private String ipAdress;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;

    private String[] roles = {"Server", "Client"};

    private int width;
    private int height;

    public MainMenu(int width, int height) {
        this.width = width;
        this.height = height;
        panel = createPanel();
        setMainMenuProperties();
    }

    private void setMainMenuProperties() {
        setDefaultLookAndFeelDecorated(true);
        setResizable(false);
        pack();
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createPanel () {

        panel = new JPanel(new GridLayout(6,1));
        panel.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));
        Container cp = getContentPane();
        cp.add(panel);
        panel.setPreferredSize(new Dimension(width, height));

        roleList = new JComboBox<>(roles);

        ipLabel = new JLabel("Ip address : ");
        portLabel = new JLabel("Port : ");

        ipField = new JTextField();
        portField = new JTextField();

        ipLabel.setLabelFor(ipField);
        portLabel.setLabelFor(portField);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    ipAdress = ipField.getText();
                    port = Integer.parseInt(portField.getText());

                    //SwingUtilities.invokeLater(() -> new Window(Mode.Player));

                } catch (NumberFormatException ex){
                    ex.printStackTrace();
                    System.out.println("Please enter a valid port number!");
                }
            }
        });

        panel.add(roleList);
        panel.add(ipLabel);
        panel.add(ipField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(startButton);

        return panel;
    }

}
