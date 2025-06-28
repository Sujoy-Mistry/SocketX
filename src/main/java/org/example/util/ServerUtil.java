package org.example.util;


import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.example.model.Controller;
import org.reflections.Reflections;


public class ServerUtil {
    public static String getContentType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if ((path.contains("api")) || !path.contains(".")) return "application/json";
        return "application/octet-stream";  //it means it's a binary file
    }

    public static Set<Class<?>> getAllControllers() {
        Reflections reflections = new Reflections("org.example.controller");
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public static String[] getDate(){
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);
        String dateHeader = "Date: " + formatter.format(Instant.now()) + "\r\n";
        String serverHeader = "Server: SocketX/1.0\r\n";
        String[]strings=new String[2];
        strings[0]=dateHeader;
        strings[1]=serverHeader;
        return strings;
    }


}
