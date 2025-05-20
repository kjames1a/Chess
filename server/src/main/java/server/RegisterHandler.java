package server;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.RegisterResult;
import model.UserData;
import service.RegisterService;


class RegisterHandler implements HttpHandler {
    private final DataAccess dataAccess;

    public RegisterHandler(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                    InputStream reqBody = exchange.getRequestBody();
                    String reqData = readString(reqBody);
                    System.out.println(reqData);
                    UserData request = (UserData)gson.fromJson(reqData, UserData.class);
                    RegisterService service = new RegisterService(dataAccess);
                    RegisterResult result = service.register(request);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream resBody = exchange.getResponseBody();
                    String serialize = gson.toJson(result);
                    writeString(serialize, resBody);
                    resBody.close();
                    success = true;
                }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
