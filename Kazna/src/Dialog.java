import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

class Dialog extends JDialog {
    public boolean result = false;
    public String NomC;
    private JTextField txtX = new JTextField();
    private JButton btnOk = new JButton("OK");
    private JButton btnNo = new JButton("Выход");

    public Dialog(String CardNum) {
        NomC = CardNum;
        this.setSize(200, 130);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        Container cp = this.getContentPane();
        ((JPanel)cp).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        cp.setLayout(new GridLayout(3, 2, 5, 5));
        cp.add(new JLabel("Введите CVV2-код:"));
        cp.add(txtX);
        cp.add(btnOk);
        cp.add(btnNo);
        btnOk.addActionListener(this::buttonClickOK);
        btnNo.addActionListener(this::buttonClickNo);
        this.setModal(true);
        this.setVisible(true);
    }

    private void buttonClickOK(ActionEvent e) {
        String cvv = txtX.getText();
        if (SecurCon.transConfirm(NomC,cvv)) {
            result = true;
            dispose();
        } else {
            result = false;
            dispose();
        }
    }

    private void buttonClickNo(ActionEvent e) {
        result = false;
        dispose();
    }


}