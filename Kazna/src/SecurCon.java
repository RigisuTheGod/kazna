import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class SecurCon {
    private static final String URL = "jdbc:postgresql://localhost:5432/kazna";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";
    private static String state_key = "";
    public static int acc_id;

    public static boolean passCheck(String password) {
        boolean Let = false;
        boolean Dig = false;
        boolean Big = false;
        if (password.length() < 8) {return false;}
        for (char c : password.toCharArray()) {
            if (!Character.isLetter(c)){ Dig = true;}
            if (!Character.isLetter(c)){ Let = true;}
            if (!Character.isUpperCase(c)){Big = true;}
        }
        if (Let && Dig && Big){return true;}
        return false;
    }

    public static boolean[] authenticateUser(String username, String password) {
        boolean[] boolcheck = new boolean[2];
        Arrays.fill(boolcheck, Boolean.FALSE);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            password = Cyph.sha256(password);
            username = Cyph.mes_enc(username,password);
            String sql = "SELECT * FROM auth_info WHERE login = ? AND pass = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            // Проверяем, найдены ли совпадения
            if (rs.next()) {
                boolean role = rs.getBoolean("opps");
                if (role) {
                    boolcheck[0] = true;
                }
                acc_id = rs.getInt("id");
                state_key = password;
                boolcheck[1] = true; // Если запись найдена, аутентификация успешна
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Выводим сообщение об ошибке
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return boolcheck;
    }

    public static boolean registerUser(String username, String password, String surname,
                                 String name, String patronymic, String birthDate) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            password = Cyph.sha256(password);
            username = Cyph.mes_enc(username,password);
            String sql = "INSERT INTO auth_info (\"login\", \"pass\",\"opps\") " + "VALUES ('" + username + "', '" + password + "',FALSE)";
            int rowsAffected = stmt.executeUpdate(sql);
            sql = "SELECT \"id\" FROM auth_info WHERE \"login\" = '" + username + "' AND \"pass\" = '" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("id");
            String symbols = "0123456789";
            String bill = new Random().ints(12, 0, symbols.length())
                    .mapToObj(symbols::charAt)
                    .map(Object::toString)
                    .collect(Collectors.joining());
            surname = Cyph.mes_enc(surname,password);
            name = Cyph.mes_enc(name,password);
            patronymic = Cyph.mes_enc(patronymic,password);
            birthDate = Cyph.mes_enc(birthDate,password);
            bill = Cyph.mes_enc(bill,password);
            sql = "INSERT INTO conf_info (\"acc_id\",\"surname\", \"first_name\",\"last_name\",\"date\",\"bill\") " + "VALUES (" + id + ",'" + surname + "','" + name + "','" + patronymic + "','" + birthDate + "','" + bill + "')";
            rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0; // Возвращаем true, если строка была добавлена
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Возвращаем false, если возникла ошибка
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addUserCard(String CardNum, String passCVV, String surname, String exp_date) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            String sql = "SELECT \"id\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int conf_id = rs.getInt("id");
            sql = "SELECT \"bill\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            rs = stmt.executeQuery(sql);
            rs.next();
            String bill = rs.getString("bill");
            CardNum = Cyph.mes_enc(CardNum,state_key);
            surname = Cyph.mes_enc(surname,state_key);
            String val = Cyph.mes_enc("1000",state_key);
            exp_date = Cyph.mes_enc(exp_date,state_key);
            passCVV = Cyph.sha256(passCVV);
            sql = "INSERT INTO eco_info (\"conf_id\",\"bill\", \"cards\",\"balance\",\"currency\",\"name\",\"cvv_code\",\"date_exp\") " + "VALUES (" +conf_id + ",'" + bill + "','" + CardNum + "','" + val + "','RUB','" + surname + "','" + passCVV + "','" + exp_date + "')";
            int rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0; // Возвращаем true, если строка была добавлена
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Возвращаем false, если возникла ошибка
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> FillCombo(ArrayList<String> Combo_box) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            String sql = "SELECT \"id\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int conf_id = rs.getInt("id");
            sql = "SELECT \"bill\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            rs = stmt.executeQuery(sql);
            rs.next();
            String bill = rs.getString("bill");
            sql = "SELECT \"cards\" FROM eco_info WHERE \"conf_id\" = '" + conf_id +"' AND \"bill\" = '" + bill + "'";
            rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false ){
                String a = Cyph.mes_dec(rs.getString("cards"),state_key);
                Combo_box.add(a);
                rs.next();
            }
            return Combo_box;
        } catch (SQLException e) {
            e.printStackTrace();
            return Combo_box;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BalanceInfo CardInfoUpdate(BalanceInfo Info_list, String card) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            card = Cyph.mes_enc(card,state_key);
            String sql = "SELECT \"balance\",\"currency\",\"name\",\"date_exp\" FROM eco_info WHERE \"cards\" = '" + card + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false ){
                Info_list.balance = Cyph.mes_dec(rs.getString("balance"),state_key);
                Info_list.currency = rs.getString("currency");
                Info_list.holder = Cyph.mes_dec(rs.getString("name"),state_key);
                Info_list.doe = Cyph.mes_dec(rs.getString("date_exp"),state_key);
                rs.next();
            }
            return Info_list;
        } catch (SQLException e) {
            e.printStackTrace();
            return Info_list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String BillGet(String bill) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            String sql = "SELECT \"bill\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            bill = Cyph.mes_dec(rs.getString("bill"),state_key);
            return bill;
        } catch (SQLException e) {
            e.printStackTrace();
            return bill;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean BalanceTransfer(String[] CardtoCard, String summary) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            System.out.println(CardtoCard[0]);
            CardtoCard[0] = Cyph.mes_enc(CardtoCard[0],state_key);
            CardtoCard[1] = Cyph.mes_enc(CardtoCard[1],state_key);
            String sql = "SELECT \"balance\",\"currency\" FROM eco_info WHERE \"cards\" = '" + CardtoCard[0] + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String sum1 = rs.getString("balance");
            sql = "SELECT \"balance\",\"currency\" FROM eco_info WHERE \"cards\" = '" + CardtoCard[1] + "'";
            rs = stmt.executeQuery(sql);
            rs.next();
            String sum2 = rs.getString("balance");
            sum1 = Cyph.mes_dec(sum1,state_key);
            sum2 = Cyph.mes_dec(sum2,state_key);
            try
            {
                int x = Integer.parseInt(sum1.trim());
                int y = Integer.parseInt(sum2.trim());
                int z = Integer.parseInt(summary.trim());
                x = x-z;
                y = y+z;
                sum1 = Integer.toString(x);
                sum2 = Integer.toString(y);
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }

            sum1 = Cyph.mes_enc(sum1,state_key);
            sum2 = Cyph.mes_enc(sum2,state_key);
            sql = "UPDATE eco_info SET balance='"+sum1+"' WHERE cards = '"+CardtoCard[0]+"';";
            int rowsAffected = stmt.executeUpdate(sql);
            sql = "UPDATE eco_info SET balance='"+sum2+"' WHERE cards ='"+CardtoCard[1]+"';";
            rowsAffected = stmt.executeUpdate(sql);
            sql = "INSERT INTO trans (\"cardfrom\",\"cardto\", \"summ\") " + "VALUES ('" +CardtoCard[0] + "','" + CardtoCard[1] + "','" + summary + "')";
            rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<TradInfo> TradeGet(ArrayList<TradInfo> cont) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            String sql = "SELECT \"id\" FROM conf_info WHERE \"acc_id\" = '" + acc_id +"'" ;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String conf_id = rs.getString("id");
            sql = "SELECT \"trad_name\",\"ammount\" FROM trader_info WHERE \"con_id\" = " + conf_id ;
            rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false ){
                TradInfo Mid_cont = new TradInfo();
                System.out.println(rs);
                Mid_cont.nam = rs.getString("trad_name");
                Mid_cont.ammo = rs.getString("ammount");
                cont.add(Mid_cont);
                rs.next();
            }
            return cont;
        } catch (SQLException e) {
            e.printStackTrace();
            return cont;
        }
    }


    public static ArrayList<StartupInfo> StartupGet(ArrayList<StartupInfo> cont) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM startups" ;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false ){
                StartupInfo Mid_cont = new StartupInfo();
                Mid_cont.st_id = rs.getString("id");
                Mid_cont.st_name = rs.getString("startup_name");
                Mid_cont.st_idea = rs.getString("idea");
                Mid_cont.st_amm_need = rs.getString("ammount_needed");
                Mid_cont.st_amm_now = rs.getString("ammount_now");
                Mid_cont.st_bill = rs.getString("st_bill");
                cont.add(Mid_cont);
                rs.next();
            }
            return cont;
        } catch (SQLException e) {
            e.printStackTrace();
            return cont;
        }
    }

    public static boolean transConfirm(String cardNum, String cvv2code) {
        boolean isConfirmed = false;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            cardNum = Cyph.mes_enc(cardNum,state_key);
            cvv2code = Cyph.sha256(cvv2code);
            String sql = "SELECT * FROM eco_info WHERE \"cards\" = '" + cardNum + "' AND \"cvv_code\" = '" + cvv2code + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                isConfirmed = true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isConfirmed;
    }

    public static ArrayList<UserInfo> UserGet(ArrayList<UserInfo> cont) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT auth_info.id,login,pass,surname,first_name,last_name,date,bill FROM auth_info,conf_info WHERE auth_info.id = conf_info.acc_id;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            while (rs.isAfterLast() == false) {
                UserInfo Mid_cont = new UserInfo();
                Mid_cont.id = rs.getString("id");
                Mid_cont.pass = rs.getString("pass");
                Mid_cont.login = Cyph.mes_dec(rs.getString("login"),Mid_cont.pass);
                Mid_cont.surname = Cyph.mes_dec(rs.getString("surname"),Mid_cont.pass);
                Mid_cont.first_name = Cyph.mes_dec(rs.getString("first_name"),Mid_cont.pass);
                Mid_cont.last_name = Cyph.mes_dec(rs.getString("last_name"),Mid_cont.pass);
                Mid_cont.doe = Cyph.mes_dec(rs.getString("date"),Mid_cont.pass);
                Mid_cont.bill = Cyph.mes_dec(rs.getString("bill"),Mid_cont.pass);
                cont.add(Mid_cont);
                rs.next();
            }
            return cont;
        } catch (SQLException e) {
            e.printStackTrace();
            return cont;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public static ArrayList<TradHisCont> TradHisGet(ArrayList<TradHisCont> cont) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT id,cardfrom,cardto,summ FROM trans";
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                while (rs.isAfterLast() == false ){
                    TradHisCont Mid_cont = new TradHisCont();
                    Mid_cont.id = rs.getString("id");
                    Mid_cont.cardfrom = rs.getString("cardfrom");
                    Mid_cont.cardto = rs.getString("cardto");
                    Mid_cont.sum = rs.getString("summ");
                    cont.add(Mid_cont);
                    rs.next();
                }
                return cont;
            } catch (SQLException e) {
                e.printStackTrace();
                return cont;
            }
    }

    public static boolean StBalanceTransfer(String[] CardtoCard, String summary) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            CardtoCard[0] = Cyph.mes_enc(CardtoCard[0],state_key);
            String sql = "SELECT \"balance\",\"currency\" FROM eco_info WHERE \"cards\" = '" + CardtoCard[0] + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String sum1 = rs.getString("balance");
            sql = "SELECT \"ammount_now\" FROM startups WHERE \"st_bill\" = '" + CardtoCard[1] + "'";
            rs = stmt.executeQuery(sql);
            rs.next();
            String sum2 = rs.getString("ammount_now");
            sum1 = Cyph.mes_dec(sum1,state_key);
            sum2 = sum2;
            try
            {
                int x = Integer.parseInt(sum1.trim());
                int y = Integer.parseInt(sum2.trim());
                int z = Integer.parseInt(summary.trim());
                x = x-z;
                y = y+z;
                sum1 = Integer.toString(x);
                sum2 = Integer.toString(y);
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
            sum1 = Cyph.mes_enc(sum1,state_key);
            CardtoCard[0] = Cyph.mes_enc(CardtoCard[0],state_key);
            sql = "UPDATE eco_info SET balance='"+sum1+"' WHERE cards = '"+CardtoCard[0]+"';";
            int rowsAffected = stmt.executeUpdate(sql);
            sql = "UPDATE startups SET ammount_now='"+sum2+"' WHERE st_bill ='"+CardtoCard[1]+"';";
            rowsAffected = stmt.executeUpdate(sql);
            sql = "INSERT INTO trans (\"cardfrom\",\"cardto\", \"summ\") " + "VALUES ('" +CardtoCard[0] + "','" + CardtoCard[1] + "','" + summary + "')";
            rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<UserInfo> UpdateUserInfo(UserInfo cont) {
        ArrayList<UserInfo> container = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            cont.login = Cyph.mes_enc(cont.login,cont.pass);
            cont.surname = Cyph.mes_enc(cont.surname,cont.pass);
            cont.first_name = Cyph.mes_enc(cont.first_name,cont.pass);
            cont.last_name = Cyph.mes_enc(cont.last_name,cont.pass);
            cont.doe = Cyph.mes_enc(cont.doe,cont.pass);
            cont.bill = Cyph.mes_enc(cont.bill,cont.pass);
            String sql = "UPDATE conf_info SET surname='" + cont.surname + "', first_name='" + cont.first_name + "',last_name='" + cont.last_name + "',date='" + cont.doe + "',bill='"+ cont.bill +"' WHERE acc_id = " + cont.id;
            Statement stmt = connection.createStatement();
            System.out.println(sql);
            int rowsAffected = stmt.executeUpdate(sql);
            sql = "UPDATE auth_info SET login='" + cont.login + "',pass='" +Cyph.sha256(cont.pass) +"' WHERE id = " + cont.id;
            System.out.println(sql);
            stmt = connection.createStatement();
            rowsAffected = stmt.executeUpdate(sql);
            sql = "SELECT auth_info.id,login,pass,surname,first_name,last_name,date,bill FROM auth_info,conf_info WHERE auth_info.id = conf_info.acc_id;";
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false) {
                UserInfo Mid_cont = new UserInfo();
                Mid_cont.id = rs.getString("id");
                Mid_cont.login = Cyph.mes_dec(rs.getString("login"),Mid_cont.pass);
                Mid_cont.pass = rs.getString("pass");
                Mid_cont.surname = Cyph.mes_dec(rs.getString("surname"),Mid_cont.pass);
                Mid_cont.first_name = Cyph.mes_dec(rs.getString("first_name"),Mid_cont.pass);
                Mid_cont.last_name = Cyph.mes_dec(rs.getString("last_name"),Mid_cont.pass);
                Mid_cont.doe = Cyph.mes_dec(rs.getString("date"),Mid_cont.pass);
                Mid_cont.bill = Cyph.mes_dec(rs.getString("bill"),Mid_cont.pass);
                container.add(Mid_cont);
                rs.next();
            }
            return container;
        } catch (SQLException e) {
            e.printStackTrace();
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<UserRole> UpdateUserRole(UserRole cont) {
        ArrayList<UserRole>container = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE auth_info SET opps=" +cont.role+" WHERE id = " + cont.id;
            System.out.println(sql);
            Statement stmt = connection.createStatement();
            int rowsAffected = stmt.executeUpdate(sql);
            sql = "SELECT id,login,opps FROM auth_info";
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            while (rs.isAfterLast() == false) {
                UserRole Mid_cont = new UserRole();
                Mid_cont.id = rs.getString("id");
                Mid_cont.login = rs.getString("login");
                if (rs.getString("opps").equals("t")){
                    Mid_cont.role = true;
                } else {
                    Mid_cont.role = false;
                }
                container.add(Mid_cont);
                rs.next();
            }
            return container;
        } catch (SQLException e) {
            e.printStackTrace();
            return container;
        }
    }

    public static ArrayList<UserRole> UserRoleGet(ArrayList<UserRole> cont) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT id,login,opps FROM auth_info";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            while (rs.isAfterLast() == false) {
                UserRole Mid_cont = new UserRole();
                Mid_cont.id = rs.getString("id");
                Mid_cont.login = rs.getString("login");
                if (rs.getString("opps").equals("t")){
                    Mid_cont.role = true;
                } else {
                    Mid_cont.role = false;
                }
                System.out.println(Mid_cont.role);
                cont.add(Mid_cont);
                rs.next();
            }
            return cont;
        } catch (SQLException e) {
            e.printStackTrace();
            return cont;
        }
    }

    public static void servMes(String mes) throws Exception {
        Socket socket = new Socket("localhost", 8080);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(mes);
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        socket.close();
    }

    public static String genMes() throws Exception {
        String symbols = "abcdefghijklmnopqrstuvwxyz";
        String cash = new Random().ints(12, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
        return cash;
    }
}
