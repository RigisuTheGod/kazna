import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

class TransferForm extends JFrame {
    private JTextField BalanceField, SumField;
    public static String[] CardList = new String[2];

    public TransferForm() {
        setTitle("Казна:Перевод средств");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1,10,10));

        ArrayList<String> card_cont = new ArrayList<>();
        SecurCon.FillCombo(card_cont);
        String[] CardFiller = new String[card_cont.size()];
        int i = 0;
        for (String card : card_cont) {
            CardFiller[i] = card;
            i+=1;
        }

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                String card = (String)box.getSelectedItem();
                BalanceInfo Updater = new BalanceInfo();
                try {
                    SecurCon.servMes("trans");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                SecurCon.CardInfoUpdate(Updater,card);
                BalanceField.setText(Updater.balance +" " +Updater.currency);
            }
        };



        panel.add(new JLabel("Карта отправителя:"));
        JComboBox Card1ComboBox = new JComboBox(CardFiller);
        Card1ComboBox.setEditable(false);
        Card1ComboBox.addActionListener(actionListener);
        panel.add(Card1ComboBox);

        panel.add(new JLabel("Карта получателя:"));
        JComboBox Card2ComboBox = new JComboBox(CardFiller);
        Card2ComboBox.setEditable(true);
        panel.add(Card2ComboBox);

        panel.add(new JLabel("Баланс:"));
        BalanceField = new JTextField();
        panel.add(BalanceField);

        panel.add(new JLabel("Сумма перевода:"));
        SumField = new JTextField();
        panel.add(SumField);


        JButton TransferButton = new JButton("Перевести");
        TransferButton.addActionListener(e -> {
            CardList[0] = (String)Card1ComboBox.getSelectedItem();
            CardList[1] = (String)Card2ComboBox.getSelectedItem();
            Dialog dlg = new Dialog(CardList[0]);
            boolean test_res = dlg.result;
            if (test_res) {
                if (SecurCon.BalanceTransfer(CardList,SumField.getText())) {
                    JOptionPane.showMessageDialog(TransferForm.this,
                            "Перевод успешен!", "Успех",
                            JOptionPane.INFORMATION_MESSAGE);
                    new MainFrame().setVisible(true);
                    dispose(); // Закрываем текущее окно
                } else {
                    JOptionPane.showMessageDialog(TransferForm.this,
                            "Ошибка перевода.", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(TransferForm.this,
                        "Неправильный CVV2-код.", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(TransferButton);

        JButton backButton = new JButton("Назад");
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose(); // Закрываем текущее окно
        });
        panel.add(backButton);

        add(panel);
    }
}
