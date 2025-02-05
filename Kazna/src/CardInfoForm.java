import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class CardInfoForm extends JFrame {
    private JTextField BillField, BalanceField, NameField, DateField;

    public CardInfoForm(){
        setTitle("Казна:Информация о карте");
        setSize(400, 300);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1,10,10));

        ArrayList<String> card_cont = new ArrayList<>();
        SecurCon.FillCombo(card_cont);
        String[] CardFiller = new String[card_cont.size()];
        int i = 0;
        for (String card : card_cont) {
            CardFiller[i] = card;
            i+=1;
        }


        panel.add(new JLabel("Счет:"));
        BillField = new JTextField();
        panel.add(BillField);
        String a = "";
        a = SecurCon.BillGet(a);
        BillField.setText(a);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                String card = (String)box.getSelectedItem();
                BalanceInfo Updater = new BalanceInfo();
                try {
                    SecurCon.servMes("cash");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                SecurCon.CardInfoUpdate(Updater,card);
                BalanceField.setText(Updater.balance +" " +Updater.currency);
                NameField.setText(Updater.holder);
                DateField.setText(Updater.doe);
            }
        };

        panel.add(new JLabel("Карта:"));
        JComboBox CardComboBox = new JComboBox(CardFiller);
        CardComboBox.setEditable(true);
        CardComboBox.addActionListener(actionListener);
        panel.add(CardComboBox);

        panel.add(new JLabel("Баланс:"));
        BalanceField = new JTextField();
        panel.add(BalanceField);

        panel.add(new JLabel("Имя держателя:"));
        NameField = new JTextField();
        panel.add(NameField);

        panel.add(new JLabel("Срок:"));
        DateField = new JTextField();
        panel.add(DateField);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });

        panel.add(backButton);

        add(panel);
        String carde = CardFiller[0];
        BalanceInfo Starter = new BalanceInfo();
        SecurCon.CardInfoUpdate(Starter,carde);
        BalanceField.setText(Starter.balance +" " +Starter.currency);
        NameField.setText(Starter.holder);
        DateField.setText(Starter.doe);
    }
}