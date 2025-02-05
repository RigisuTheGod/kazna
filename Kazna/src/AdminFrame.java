import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    public AdminFrame() {
        setTitle("Казна:Панель администратора");
        setSize(300, 200);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1,10,10));

        JButton button1 = new JButton("Пользователи");
        button1.addActionListener(e -> {
            new UserFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button1);

        JButton button2 = new JButton("Транзакции");
        button2.addActionListener(e -> {
            new TranHisFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button2);

        JButton button4 = new JButton("DACL");
        button4.addActionListener(e -> {
            new ContFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(button4);

        add(panel);
    }
}
