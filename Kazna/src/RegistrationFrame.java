import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

class RegistrationFrame extends JFrame {
    private JTextField usernameField, surnameField, nameField, patronymicField, birthDateField;
    private JPasswordField passwordField;

    public RegistrationFrame() {
        setTitle("Казна:Регистрация");
        setSize(350, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2,10,10));

        panel.add(new JLabel("Логин:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Фамилия:"));
        surnameField = new JTextField();
        panel.add(surnameField);

        panel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Отчество:"));
        patronymicField = new JTextField();
        panel.add(patronymicField);

        panel.add(new JLabel("Дата Рождения:"));
        birthDateField = new JTextField();
        panel.add(birthDateField);

        JButton registerButton = new JButton("Зарегистрироваться");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean PassCheck = SecurCon.passCheck(password);
            if (!PassCheck) {
                JOptionPane.showMessageDialog(RegistrationFrame.this,
                        "Слабый пароль.", "Предупреждение",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                String surname = surnameField.getText();
                String name = nameField.getText();
                String patronymic = patronymicField.getText();
                String birthDate = birthDateField.getText();
                try {
                    SecurCon.servMes("reg");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                if (SecurCon.registerUser(username, password, surname, name, patronymic, birthDate)) {
                    JOptionPane.showMessageDialog(RegistrationFrame.this,
                            "Регистрация успешна!", "Успех",
                            JOptionPane.INFORMATION_MESSAGE);
                    new LoginFrame().setVisible(true);
                    dispose(); // Закрываем текущее окно
                } else {
                    JOptionPane.showMessageDialog(RegistrationFrame.this,
                            "Регистрация не удалась.", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(registerButton);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(backButton);

        add(panel);
    }
}