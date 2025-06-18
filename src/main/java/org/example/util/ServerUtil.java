package org.example.util;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class ServerUtil {
    private static Logger logger=Logger.getLogger(ServerUtil.class.getName());
    public static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }
    public  static void getFile(String path, OutputStream outputStream){
        if (path.equals("/")) {
            path = "/index.html"; // Default fallback
        }
        File file = new File("src/main/static" + path); // static/index.html
        try {
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String contentType = getContentType(path);

                String responseHeader = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + fileBytes.length + "\r\n" +
                        "\r\n";

                outputStream.write(responseHeader.getBytes());
                outputStream.write(fileBytes);
            } else {
                String body = "<h1>404 Not Found</h1>";
                String response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "\r\n" + body;

                outputStream.write(response.getBytes());
            }
        }catch (Exception ex){
            logger.info("Something wrong happen while getting the file:"+ex.getMessage());
        }

    }
}
