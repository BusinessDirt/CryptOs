package com.example.cryptos;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SecureMessagingUtils {

    public static KeyPair generateKeyPair() throws Exception {
        // Use Android Keystore for key generation
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                "secure_key_alias",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
        keyPairGenerator.initialize(builder.build());
        return keyPairGenerator.generateKeyPair();
    }

    // ... (existing methods)

    public static void saveToFile(byte[] data, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
            fos.flush();
        }
    }

    public static void scheduleFileDestruction(final File file, long delayInSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                // Securely delete the file after the specified delay
                if (file.exists()) {
                    securelyDeleteFile(file);
                }
            }
        }, delayInSeconds, TimeUnit.SECONDS);
        scheduler.shutdown();
    }

    // Securely overwrite the file content before deletion
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void securelyDeleteFile(File file) {
        try {
            byte[] data = new byte[(int) file.length()];
            Arrays.fill(data, (byte) 0); // Overwrite with zeros
            Files.write(file.toPath(), data, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
    }

    // Copy the given text to the clipboard
    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public static String outputBuilder(String message, String key) {
        return String.format("<output message='%s' key='%s'>", message, key);
    }
}
