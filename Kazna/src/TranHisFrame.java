import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TranHisFrame extends JFrame {
    private JComboBox<String> accountFromComboBox;
    private JComboBox<String> accountToComboBox;
    private JTextField balanceFieldFrom;
    private JTextField balanceFieldTo;
    private JTextField transferAmountField;

    public TranHisFrame() {
        setTitle("Админ:Транзакции");
        setSize(600, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем панель для размещения элементов
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1,10,10));
        ArrayList<TradHisCont> data = new ArrayList<>();
        String[] col = {"ID", "Адресант","Адресат","Сумма"};
        SecurCon.TradHisGet(data);
        String[][] dataTab = new String[data.size()][col.length];
        int i = 0;
        for (TradHisCont fillerData : data) {
            dataTab[i][0] = fillerData.id;
            dataTab[i][1] = fillerData.cardfrom;
            dataTab[i][2] = fillerData.cardto;
            dataTab[i][3] = fillerData.sum;
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
            new AdminFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(backButton);

        add(panel);
    }
}
