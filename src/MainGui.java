import Gui.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainGui extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;

    public MainGui() {
        super("Login");
        setupGUI();
    }

    private void setupGUI() {
        // Username field
        usernameField = new JTextField(20);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Panel for components
        JPanel panel = new JPanel();
        panel.add(new JLabel("Username: "));
        panel.add(usernameField);
        panel.add(loginButton);

        // Add panel to the frame
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        if (!username.isEmpty()) {
            dispose();
            try {
                if (Finder.findUser(username) == false) {
                    JOptionPane.showMessageDialog(null, "User not found\nCreating new user");
                    new GuiSide(username, false);
                } else {
                    JOptionPane.showMessageDialog(null, "User found\nGetting user data");
                    new GuiSide(username, true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle any exception that may occur during setup
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGui();
            }
        });
    }
}
