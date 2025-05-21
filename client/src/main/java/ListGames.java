import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListGames {

    public void listGames(String urlString, String authToken) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setDoOutput(false);
        connection.addRequestProperty("Accept", "text/html");
        connection.addRequestProperty("Authorization", authToken);
        connection.connect();

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


