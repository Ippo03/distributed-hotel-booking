package org.backend.misc;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

public class Util {

    // Constants
    public static int HEARTBEAT_INTERVAL = 10000;

    public static byte[] imageToByteArray(String imagePath, int targetWidth, int targetHeight, float quality) {
        try {
            File imageFile = new File(imagePath);
            BufferedImage originalImage = ImageIO.read(imageFile);

            // Resize the image
            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            // Compress the image
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new IllegalStateException("No writers found");
            ImageWriter writer = writers.next();
            try (MemoryCacheImageOutputStream output = new MemoryCacheImageOutputStream(byteArrayOutputStream)) {
                writer.setOutput(output);
                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality);
                }
                writer.write(null, new IIOImage(resizedImage, null, null), param);
            }
            writer.dispose();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.err.println("Error resizing and compressing image: " + e.getMessage());
            return null;
        }
    }

    public static String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Compute the hash value
        byte[] hashBytes = digest.digest(password.getBytes());

        // Convert the byte array to hexadecimal representation
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
