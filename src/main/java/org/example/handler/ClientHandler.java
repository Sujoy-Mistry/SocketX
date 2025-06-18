package org.example.handler;

import org.example.controller.RequestController;
import org.example.model.Controller;
import org.example.model.RequestType;
import org.example.util.ServerUtil;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Controller
public class ClientHandler {
    private static final Logger logger=Logger.getLogger(ClientHandler.class.getName());

    public  static void getFile(String path, OutputStream outputStream){
        if (path.equals("/")) {
            path = "/index.html"; // Default fallback
        }

        File file = new File("src/main/static" + path); // static/index.html
        try {
            Thread.sleep(5000);
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String contentType = ServerUtil.getContentType(path);

                String responseHeader = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + fileBytes.length + "\r\n" +
                        "\r\n";

                outputStream.write(responseHeader.getBytes());
                outputStream.write(fileBytes);
            } else {
                String body = "<h1>404 Not Found</h1>";
                String response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: "+ServerUtil.getContentType(path)+"\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "\r\n" + body;

                outputStream.write(response.getBytes());
            }
        }catch (Exception ex){
            logger.info("Something wrong happen while getting the file:"+ex.getMessage());
        }

    }



    static Map<String, Method> routeMap = new HashMap<>();
    static Map<String, Object> instanceMap = new HashMap<>();
    public static void registerAnnotatedRoutes() {

        Set<Class<?>> controllerClasses = ServerUtil.getAllControllers();

        try {
            for (Class<?> controllerClass : controllerClasses) {
                Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
                instanceMap.put(controllerClass.getName(), controllerInstance);

                for (Method method : controllerClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestType.Get.class)) {
                        RequestType.Get get = method.getAnnotation(RequestType.Get.class);
                        String path = get.value();
                        routeMap.put(path, method);
                    }
                }
            }
            } catch(Exception e){
                e.printStackTrace();
            }

    }
    public static void requestHandler( String path,OutputStream outputStream){
        if (routeMap.containsKey(path)) {
            Method method = routeMap.get(path);
            Object controller = instanceMap.get(method.getDeclaringClass().getName());
            try {
                String responseBody = (String) method.invoke(controller);
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: "+ServerUtil.getContentType(path)+"\r\n" +
                        "Content-Length: " + responseBody.length() + "\r\n" +
                        "\r\n" + responseBody;

                outputStream.write(response.getBytes());
                outputStream.flush();
            }catch (Exception ex){
                logger.info("Something wrong happen in RequestHandler: "+ ex.getMessage());
            }
        }else {
            logger.warning("Path isn't present in Controller");
            String response = "HTTP/1.1 204 No Content\r\n\r\n";
            try {
                outputStream.write(response.getBytes());
            }catch (Exception ex){
                logger.warning("Exception occured while returning the output Stream");
            }
        }

    }
}
