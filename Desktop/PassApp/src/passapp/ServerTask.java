package passapp;

import javafx.concurrent.Task;

import java.io.InputStream;
import java.io.OutputStreamWriter;
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

    public ServerTask(String messageBody) {
        super();
        this.messageBody = messageBody;
    }

    @Override
    protected String call() throws Exception {

        String decrypted = null;

        try {

            String url = "http://127.0.0.1:3000";
            URL object = new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
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

            // Create stream for server response
            InputStream is = con.getInputStream();
            Scanner scanner = new Scanner(is);
            StringBuilder data = new StringBuilder();

            // Get input from server
            while (scanner.hasNext()) {
                data.append(scanner.next());
            }

            decrypted = jbsCrypto.decrypt(data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (decrypted != null) ? decrypted : "Null";
    }

    @Override
    protected void done() {
        super.done();
        System.out.print("\ndone");
    }

    @Override
    public void run() {
        super.run();
        System.out.print("\nrun");
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.print("\nsucceeded");
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        System.out.print("\ncancelled");
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.print("\nfailed");
    }

    @Override
    protected void running() {
        super.running();
        System.out.print("\nrunning");
    }
}
