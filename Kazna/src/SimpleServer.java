import java.io.*;
import java.net.*;

public class SimpleServer {
    private static String pkey = "gen218012";

    public static void main(String[] args) throws IOException {
        // Создаём серверный сокет
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Сервер запущен на порту 8080");

        while (true) {
            // Принимаем соединение от клиента
            Socket clientSocket = serverSocket.accept();
            System.out.println("Новое сообщение от" + clientSocket.getInetAddress());

            // Создаём поток для обслуживания клиента
            new Thread(() -> {
                try {
                    try {
                    // Получаем входной поток от клиента
                    InputStream inputStream = clientSocket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    // Отправляем ответ клиенту
                    OutputStream outputStream = clientSocket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    if (inputStream.available() > 0) { // Добавляем проверку на наличие данных
                        String receivedString = dataInputStream.readUTF();
                        System.out.println(receivedString);
                        if ("log".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("reg".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("add".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("trans".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("port".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("cash".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else if ("startup".equals(receivedString)) {
                            receivedString = SecurCon.genMes();
                            Cyph.mes_enc(receivedString,pkey);
                        } else {
                            System.out.println("Данные не получены");
                        }
                        printWriter.flush();
                    }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    try {
                        clientSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}