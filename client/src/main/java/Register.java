import com.google.gson.Gson;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register {

    public void registerUser(String urlString, String username, String password, String email) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");

        connection.connect();

        UserData info = new UserData(username, password, email);
        String serialize = new Gson().toJson(info);

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(serialize.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            String response = new String(responseBody.readAllBytes());
            System.out.println(response);
        }
        else {
            InputStream responseBody = connection.getErrorStream();
            String response = new String(responseBody.readAllBytes());
            System.out.println(response);
        }
    }
}
