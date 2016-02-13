package passapp;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import brad.crypto.JBSCrypto;
/**
 * How to Use my encryption module JBSCrypto
 *
 *  JBSCrypto jbsCrypto = new JBSCrypto();
    String encrypted = jbsCrypto.encrypt("This is what I want to encrypt");
    System.out.println("\n\nEncrypted ==> " + encrypted);
    String decrypted = jbsCrypto.decrypt(encrypted);
    System.out.println("\n\nDecrypted ==> " + decrypted);
 */


/**
 * Created by Brad on 2/11/2016.
 *
 * Task for executing connection to server on background thread
 */
public class ServerTask extends Task<String> {

    String messageBody;
    boolean failed = false;

    public ServerTask() {
        super();
    }

    public ServerTask(String messageBody) {
        super();
        this.messageBody = messageBody;
    }

    @Override
    protected String call() throws Exception {

        String decrypted = null;

        try {

            String serverIP = "http://127.0.0.1:3000";
            URL url = new URL(serverIP);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);

            // Set header properties
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("tier1", "45r97diIj3099KpqnzlapEIv810nZaaS0");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");

            // Encrypt message body
            JBSCrypto jbsCrypto = new JBSCrypto();
            String encrypted = jbsCrypto.encrypt(messageBody);

            // Send message to server
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(encrypted);
            wr.flush();
            wr.close();

            BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            StringBuilder data = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                data.append(line);
            }
            bf.close();

            decrypted = jbsCrypto.decrypt(data.toString());

        } catch (ConnectException e) {
            cancel();
        }

        return (decrypted != null) ? decrypted : null;
    }
}
