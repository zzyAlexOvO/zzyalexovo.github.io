package app.model.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Hash {
    public static String md5(String ts, String privateKey, String publicKey) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(ts.getBytes());
        md.update(privateKey.getBytes());
        md.update(publicKey.getBytes());
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
