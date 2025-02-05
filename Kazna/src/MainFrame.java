import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Казна:Главное меню");
        setSize(250, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1,10,10));

        JButton button1 = new JButton("Добавление карты");
        button1.addActionListener(e -> {
            new CardApplicationForm().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button1);

        JButton button2 = new JButton("Перевод средств");
        button2.addActionListener(e -> {
            new TransferForm().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button2);

        JButton button4 = new JButton("Портфолио");
        button4.addActionListener(e -> {
            new TradeFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button4);

        JButton button5 = new JButton("Баланс");
        button5.addActionListener(e -> {
            new CardInfoForm().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button5);

        JButton button6 = new JButton("Поддержка стартапов");
        button6.addActionListener(e -> {
            new StartupForm().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button6);

        add(panel);
    }
}
