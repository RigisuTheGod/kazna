import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ContFrame extends JFrame {
    private JComboBox<String> accountFromComboBox;
    private JComboBox<String> accountToComboBox;
    private JTextField balanceFieldFrom;
    private JTextField balanceFieldTo;
    private JTextField transferAmountField;

    public ContFrame() {
        setTitle("Админ:DACL");
        setSize(400, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем панель для размещения элементов
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1,10,10));
        ArrayList<UserRole> data = new ArrayList<>();
        String[] col = {"ID", "Логин","Права администратора"};
        SecurCon.UserRoleGet(data);
        String[][] dataTab = new String[data.size()][col.length];
        int i = 0;
        for (UserRole fillerData : data) {
            dataTab[i][0] = fillerData.id;
            dataTab[i][1] = fillerData.login;
            dataTab[i][2] = String.valueOf(fillerData.role);
            i++;
        }


        // создаем новую таблицу и устанавливаем данные и названия столбцов
        JTable table = new JTable(dataTab, col);

        // добавляем таблицу на панель прокрутки
        JScrollPane scrollPane = new JScrollPane(table);

        // добавляем панель прокрутки на фрейм
        panel.add(scrollPane);

        JButton EditButton = new JButton("Изменить");
        EditButton.addActionListener(e -> {
            UserRole cont = new UserRole();
            int selectedRow = table.getSelectedRow();
            cont.id = String.valueOf(table.getValueAt(selectedRow, 0));
            cont.login = String.valueOf(table.getValueAt(selectedRow, 1));
            cont.role = Boolean.valueOf(String.valueOf(table.getValueAt(selectedRow, 2)));
            SecurCon.UpdateUserRole(cont);// Закрываем текущее окно
        });
        panel.add(EditButton);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new AdminFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(backButton);

        add(panel);
    }
}
