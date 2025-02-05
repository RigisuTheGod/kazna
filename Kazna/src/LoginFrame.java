import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Казна:Авторизация");
        setSize(250, 250);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,1,10,10));

        panel.add(new JLabel("Логин:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);



        JButton loginButton = new JButton("Войти");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean[] res;
                try {
                    SecurCon.servMes("log");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                res = SecurCon.authenticateUser(username, password);
                if (res[0]) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Добро пожаловать,администратор!", "Успешная авторизация",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new AdminFrame().setVisible(true);
                } else {
                    if (res[1]) {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Добро пожаловать, " + username + "!", "Успешная авторизация",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new MainFrame().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Неверный логин или пароль.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(loginButton);

        JButton registerButton = new JButton("Регистрация");
        registerButton.addActionListener(e -> {
            new RegistrationFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(registerButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true); // Делаем окно видимым
        });
    }
}


