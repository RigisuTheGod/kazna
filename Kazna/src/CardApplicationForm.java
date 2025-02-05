import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

class CardApplicationForm extends JFrame {
    private JTextField CardNum, namet, date_exp;
    private JPasswordField CVVcode;

    public CardApplicationForm() {
        setTitle("Казна:Добавление карты");
        setSize(350, 225);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2,10,10));

        panel.add(new JLabel("Номер карты:"));
        CardNum = new JTextField();
        panel.add(CardNum);

        panel.add(new JLabel("CVV-код:"));
        CVVcode = new JPasswordField();
        panel.add(CVVcode);

        panel.add(new JLabel("Имя держателя:"));
        namet = new JTextField();
        panel.add(namet);

        panel.add(new JLabel("Срок:"));
        date_exp = new JTextField();
        panel.add(date_exp);

        JButton addCardButton = new JButton("Добавить карту");
        addCardButton.addActionListener(e -> {
            String cardtext = CardNum.getText();
            String passCVV = new String(CVVcode.getPassword());
            String surname = namet.getText();
            String exp_date = date_exp.getText();
            try {
                SecurCon.servMes("add");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if (SecurCon.addUserCard(cardtext, passCVV, surname, exp_date)) {
                JOptionPane.showMessageDialog(CardApplicationForm.this,
                        "Карта добавлена успешно!", "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
                new MainFrame().setVisible(true);
                dispose(); // Закрываем текущее окно
            } else {
                JOptionPane.showMessageDialog(CardApplicationForm.this,
                        "Добавить карту не удалось.", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addCardButton);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });
        panel.add(backButton);

        add(panel);
    }
}