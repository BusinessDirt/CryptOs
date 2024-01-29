package com.example.cryptos;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.security.KeyPair;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {

    private EditText messageInput;
    private TextView messageOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.messageInput = findViewById(R.id.messageInput);
        this.messageOutput = findViewById(R.id.messageOutput);

        findViewById(R.id.buttonEncrypt).setOnClickListener(view -> encryptMessage());
        findViewById(R.id.buttonDecrypt).setOnClickListener(view -> decryptMessage());

    }

    private void encryptMessage() {
        try {
            String message = this.messageInput.getText().toString();

            // Generate secret key
            SecretKey encryptedKey = KeyGenerator.getInstance("AES").generateKey();

            // Create Cipher object
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptedKey);

            // Encrypt message
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Save log files and schedule self-destruction after 10 seconds
            saveLogFiles(encryptedMessage, encryptedKey.getEncoded());
            File messageFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_message");
            File keyFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_key");
            SecureMessagingUtils.scheduleFileDestruction(messageFile, 10);
            SecureMessagingUtils.scheduleFileDestruction(keyFile, 10);

            // Send to Output Box
            String output = "Encrypted Message: " + Base64.encodeToString(encryptedMessage, Base64.DEFAULT)
                    + "\nEncrypted Key: " + Base64.encodeToString(encryptedKey.getEncoded(), Base64.DEFAULT);

            this.messageOutput.setText(SecureMessagingUtils.outputBuilder(Base64.encodeToString(encryptedMessage, Base64.DEFAULT),
                    Base64.encodeToString(encryptedKey.getEncoded(), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptMessage() {
        //TODO
        try {
            String message = this.messageInput.getText().toString();

            // Generate secret key
            SecretKey encryptedKey = KeyGenerator.getInstance("AES").generateKey();

            // Create Cipher object
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptedKey);

            // Encrypt message
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            // Save log files and schedule self-destruction after 10 seconds
            saveLogFiles(encryptedMessage, encryptedKey.getEncoded());
            File messageFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_message");
            File keyFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_key");
            SecureMessagingUtils.scheduleFileDestruction(messageFile, 10);
            SecureMessagingUtils.scheduleFileDestruction(keyFile, 10);

            // Send to Output Box
            String output = "Encrypted Message: " + Base64.encodeToString(encryptedMessage, Base64.DEFAULT)
                    + "\nEncrypted Key: " + Base64.encodeToString(encryptedKey.getEncoded(), Base64.DEFAULT);

            this.messageOutput.setText(SecureMessagingUtils.outputBuilder(Base64.encodeToString(encryptedMessage, Base64.DEFAULT),
                    Base64.encodeToString(encryptedKey.getEncoded(), Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveLogFiles(byte[] encryptedMessage, byte[] encryptedKey) {
        try {
            File messageFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_message");
            File keyFile = new File(getFilesDir(), UUID.randomUUID().toString() + "_key");

            // Save encrypted message to file
            SecureMessagingUtils.saveToFile(encryptedMessage, messageFile);

            // Save encrypted key to file
            SecureMessagingUtils.saveToFile(encryptedKey, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}