import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserFrame extends JFrame {
    private JComboBox<String> accountFromComboBox;
    private JComboBox<String> accountToComboBox;
    private JTextField balanceFieldFrom;
    private JTextField balanceFieldTo;
    private JTextField transferAmountField;

    public UserFrame() {
        setTitle("Админ:Список Пользователей");
        setSize(1000, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создаем панель для размещения элементов
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        ArrayList<UserInfo> data = new ArrayList<>();
        String[] col = {"ID", "Логин","Пароль","Фамилия","Имя","Отчество","Дата рождения","Счет"};
        SecurCon.UserGet(data);
        String[][] dataTab = new String[data.size()][col.length];
        int i = 0;
        for (UserInfo fillerData : data) {
            dataTab[i][0] = fillerData.id;
            dataTab[i][1] = fillerData.login;
            dataTab[i][2] = fillerData.pass;
            dataTab[i][3] = fillerData.surname;
            dataTab[i][4] = fillerData.first_name;
            dataTab[i][5] = fillerData.last_name;
            dataTab[i][6] = fillerData.doe;
            dataTab[i][7] = fillerData.bill;
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
            UserInfo cont = new UserInfo();
            int selectedRow = table.getSelectedRow();
            cont.id = String.valueOf(table.getValueAt(selectedRow, 0));
            cont.login = String.valueOf(table.getValueAt(selectedRow, 1));
            cont.pass = String.valueOf(table.getValueAt(selectedRow, 2));
            cont.surname = String.valueOf(table.getValueAt(selectedRow, 3));
            cont.first_name = String.valueOf(table.getValueAt(selectedRow, 4));
            cont.last_name = String.valueOf(table.getValueAt(selectedRow, 5));
            cont.doe = String.valueOf(table.getValueAt(selectedRow, 6));
            cont.bill = String.valueOf(table.getValueAt(selectedRow, 7));
            SecurCon.UpdateUserInfo(cont);// Закрываем текущее окно
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
