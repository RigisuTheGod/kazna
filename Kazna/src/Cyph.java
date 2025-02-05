import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Cyph {

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateKey(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes()); // Хэшируем текст и используем результат как ключ
    }

    private static byte[] generateIv(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        byte[] iv = new byte[16];
        System.arraycopy(hash, 0, iv, 0, iv.length); // Используем первые 16 байт хеша
        return iv;
    }

    private static String encrypt(String plainText, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedText, byte[] key, byte[] iv) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, "UTF-8");
    }

    public static String mes_enc(String oText, String pkey) throws Exception {
        byte[] key = generateKey(pkey); // Генерация ключа на основе заданного текста
        byte[] iv = generateIv(pkey); // Генерация IV на основе исходного текста

        String encryptedText = encrypt(oText, key, iv);

        System.out.println("Открытый текст: " + encryptedText);
        return encryptedText;
    }

    public static String mes_dec(String eText, String pkey) throws Exception {
        byte[] key = generateKey(pkey); // Генерация ключа на основе заданного текста
        byte[] iv = generateIv(pkey); // Генерация IV на основе исходного текста

        String decryptedText = decrypt(eText, key, iv);

        System.out.println("Шифртекст: " + decryptedText);
        return decryptedText;
    }

}
