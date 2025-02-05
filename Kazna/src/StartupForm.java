import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

class StartupForm extends JFrame {
    private JComboBox<String> accountFromComboBox;
    private JComboBox<String> accountToComboBox;
    private JTextField balanceFieldFrom;
    private JTextField balanceFieldTo;
    private JTextField transferAmountField;

    public StartupForm() {
        setTitle("Казна:Поддержка стартапов");
        setSize(1000, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем панель для размещения элементов
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        ArrayList<StartupInfo> data = new ArrayList<>();
        String[] col = {"Название", "Описание","Целевая сумма","Собрано средств", "Счет"};
        try {
            SecurCon.servMes("startup");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        SecurCon.StartupGet(data);
        String[][] dataTab = new String[data.size()][col.length];
        int i = 0;
        for (StartupInfo fillerData : data) {
            dataTab[i][0] = fillerData.st_name;
            dataTab[i][1] = fillerData.st_idea;
            dataTab[i][2] = fillerData.st_amm_need;
            dataTab[i][3] = fillerData.st_amm_now;
            dataTab[i][4] = fillerData.st_bill;
            i++;
        }


        // создаем новую таблицу и устанавливаем данные и названия столбцов
        JTable table = new JTable(dataTab, col);

        // добавляем таблицу на панель прокрутки
        JScrollPane scrollPane = new JScrollPane(table);

        // добавляем панель прокрутки на фрейм
        panel.add(scrollPane);

        JButton StButton = new JButton("Спонсировать");
        StButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            StTransferForm.StCard = table.getValueAt(selectedRow, 4);
            new StTransferForm().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        StButton.setPreferredSize(new Dimension(100,125));
        panel.add(StButton);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        backButton.setPreferredSize(new Dimension(100,125));
        panel.add(backButton);

        add(panel);
    }
}