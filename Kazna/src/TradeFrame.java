import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

class TradeFrame extends JFrame {
    private JTextField amountField;
    private JSlider monthsSlider;
    private JLabel monthlyPaymentLabel;

    public TradeFrame() {
        setTitle("Казна:Портфолио");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLocationRelativeTo(null);

        // Создаем панель для размещения элементов
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2,10,10));
        ArrayList<TradInfo> data = new ArrayList<>();
        String[] col = {"Акции", "Количество"};
        try {
            SecurCon.servMes("port");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        SecurCon.TradeGet(data);
        String[][] dataTab = new String[data.size()][col.length];
        int i = 0;
        for (TradInfo fillerData : data){
            dataTab[i][0] = fillerData.nam;
            dataTab[i][1] = fillerData.ammo;
            i++;
        }


        // создаем новую таблицу и устанавливаем данные и названия столбцов
        JTable table = new JTable(dataTab, col);

        // добавляем таблицу на панель прокрутки
        JScrollPane scrollPane = new JScrollPane(table);

        // добавляем панель прокрутки на фрейм
        panel.add(scrollPane);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(backButton);

        add(panel);
    }
}