import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login {

    public void loginUser(String urlString, String username, String password) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "application/json");

        connection.connect();

        UserData info = new UserData(username, password, null);
        var serializer = new Gson();
        var json = serializer.toJson(info);

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(json.getBytes());
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

