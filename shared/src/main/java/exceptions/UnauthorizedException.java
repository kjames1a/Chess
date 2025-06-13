package exceptions;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

    public class UnauthorizedException extends Exception {

        public UnauthorizedException(String message) {
            super(message);
        }

        public String toJson() {
            return new Gson().toJson(Map.of("message", getMessage()));
        }

        public static exceptions.UnauthorizedException fromJson(InputStream stream) {
            var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
            String message = map.get("message").toString();
            return new exceptions.UnauthorizedException(message);
        }

    }

