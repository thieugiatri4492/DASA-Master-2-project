package net.ws.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class JSonHandler {
    private JSonHandler() {
    }

    public static void writeJson(String content, String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fop = new FileOutputStream(file)) {
            if (!file.exists()) 
                file.createNewFile();
            // get the content in bytes
            byte[] contentInBytes = content.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String readJsonToken(String filePath) {
        JSONParser parser = new JSONParser();
        String token = null;
        Object obj;
        try {
            obj = parser.parse(new FileReader(filePath));
            JSONObject jsonObject = (JSONObject) obj;
            token = (String) jsonObject.get("access_token");
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }
        return token;
    }
}
