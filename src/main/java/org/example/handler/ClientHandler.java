package org.example.handler;

import org.example.model.Controller;
import org.example.model.HttpTriplet;
import org.example.model.RequestType;
import org.example.util.ServerUtil;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
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
            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String contentType = ServerUtil.getContentType(path);
                String []dateAndServer=ServerUtil.getDate();
                String responseHeader = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + " "+fileBytes.length +"\r\n" + dateAndServer[0]+
                        dateAndServer[1]+
                        "Cache-Control: max-age=3600\r\n" +
                        "\r\n";  // blank line separates headers from body

                //   writeOutput(outputStream,200," OK", Arrays.toString(fileBytes),path);
                outputStream.write(responseHeader.getBytes());
                outputStream.write(fileBytes);
            } else {
                String body = "<h1>404 Not Found</h1>";
                writeOutput(outputStream,404," <h1>404 Not Found</h1>",body,path);
            }
        }catch (Exception ex){
            logger.info("Something wrong happen while getting the file:"+ex.getMessage());
        }

    }



    static Map<String, Map<String,Method>> routeMap = new HashMap<>();
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
                        routeMap.computeIfAbsent("GET",k->new HashMap<>()).put(path,method);
                    }
                    if (method.isAnnotationPresent(RequestType.Put.class)) {
                        RequestType.Put put = method.getAnnotation(RequestType.Put.class);
                        String path = put.value();
                        routeMap.computeIfAbsent("PUT",k->new HashMap<>()).put(path,method);
                    }
                    if (method.isAnnotationPresent(RequestType.Post.class)) {
                        RequestType.Post post = method.getAnnotation(RequestType.Post.class);
                        String path = post.value();
                        routeMap.computeIfAbsent("POST",k->new HashMap<>()).put(path,method);
                    }
                    if (method.isAnnotationPresent(RequestType.Delete.class)) {
                        RequestType.Delete delete = method.getAnnotation(RequestType.Delete.class);
                        String path = delete.value();
                        routeMap.computeIfAbsent("DELETE",k->new HashMap<>()).put(path,method);
                    }
                }
            }
            } catch(Exception e){
                logger.warning("Something wrong Happen in registerAnnotatedRoutes :"+e.getMessage());
            }

    }
    public static void requestHandler( String path,String method1,OutputStream outputStream){
        boolean existsMethod = Optional.ofNullable(routeMap.get(method1)).isPresent();
        Map<String, String> queryParams=new HashMap<>();
        String queryString="";
        if(path.contains("?")){
            queryString=path.substring(path.indexOf("?")+1);
            path=path.substring(0,path.indexOf("?"));
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2); // use limit=2 to handle values with '='
                if (kv.length == 2) {
                    String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
                    queryParams.put(key, value);
                }
            }
        }
        if (existsMethod) {
            String finalPath = path;
            boolean existsParameter= Optional.ofNullable(routeMap.get(method1))
                    .map(k->k.get(finalPath)).isPresent();
            if(existsParameter) {
                Method method = routeMap.get(method1).get(path);
                //to support Query Param
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter param = parameters[i];

                    if (param.isAnnotationPresent(RequestType.RequestParam.class)) {
                        RequestType.RequestParam annotation = param.getAnnotation(RequestType.RequestParam.class);
                        String paramName = annotation.value();
                        String value = queryParams.get(paramName);

                        // Convert to correct type
                        if (param.getType().equals(String.class)) {
                            args[i] = value;
                        } else if (param.getType().equals(int.class)) {
                            args[i] = Integer.parseInt(value);
                        } else {
                            args[i] = null; // fallback
                        }
                    }
                }


                Object controller = instanceMap.get(method.getDeclaringClass().getName());
                try {
                    String responseBody = (String) method.invoke(controller,args);
                    Optional<HttpTriplet> httpTriplet= getStatusAndRequestType(method1);
                    if(httpTriplet.isEmpty()) {
                        writeOutput(outputStream, 200, " OK", responseBody, path);
                    }else{
                        writeOutput(outputStream, httpTriplet.get().getStatusCode(),httpTriplet.get().getStatusMessage() , responseBody, path);
                    }
                } catch (Exception ex) {
                    logger.info("Something wrong happen in RequestHandler: " + ex.getMessage());
                }
            }else{
                writeOutput(outputStream,400," Bad Request","You have entered a wrong path",path);
            }
        }else {
            writeOutput(outputStream,400," Bad Request","You have Entered a wrong method",path);
        }

    }

    private static void writeOutput(OutputStream outputStream,int statusCode,String requestType , String body,String path){
        logger.warning(body);
        String []dateAndServer=ServerUtil.getDate();
        String response = "HTTP/1.1 " + statusCode + requestType + "\r\n" +
                "Content-Type: " + ServerUtil.getContentType(path) + "\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                dateAndServer[0]  +
                dateAndServer[1]  +
                "Cache-Control: max-age=3600\r\n" +  // Optional caching
                "\r\n" +
                body;

        try {
            outputStream.write(response.getBytes());
            outputStream.flush();
        }catch (Exception ex){
            logger.warning("Exception occurred while returning the output Stream");
        }
    }

    private static Optional<HttpTriplet> getStatusAndRequestType(String method){

        List<HttpTriplet> httpTripletList = new ArrayList<>();

        httpTripletList.add(new HttpTriplet(200, " OK", "GET"));
        httpTripletList.add(new HttpTriplet(100, " Continue", "GET"));
        httpTripletList.add(new HttpTriplet(101, " Switching Protocols", "GET"));
        httpTripletList.add(new HttpTriplet(201, " Created", "POST"));
        httpTripletList.add(new HttpTriplet(202, " Accepted", "POST"));
        httpTripletList.add(new HttpTriplet(204, " No Content", "DELETE"));

        httpTripletList.add(new HttpTriplet(301, " Moved Permanently", "GET"));
        httpTripletList.add(new HttpTriplet(302, " Found", "GET"));
        httpTripletList.add(new HttpTriplet(304, " Not Modified", "GET"));

        httpTripletList.add(new HttpTriplet(400, " Bad Request", "POST"));
        httpTripletList.add(new HttpTriplet(401, " Unauthorized", "GET"));
        httpTripletList.add(new HttpTriplet(403, " Forbidden", "GET"));
        httpTripletList.add(new HttpTriplet(404, " Not Found", "GET"));
        httpTripletList.add(new HttpTriplet(405, " Method Not Allowed", "PUT"));
        httpTripletList.add(new HttpTriplet(409, " Conflict", "POST"));

        httpTripletList.add(new HttpTriplet(500, " Internal Server Error", "GET"));
        httpTripletList.add(new HttpTriplet(501, " Not Implemented", "POST"));
        httpTripletList.add(new HttpTriplet(502, " Bad Gateway", "GET"));
        httpTripletList.add(new HttpTriplet(503, " Service Unavailable", "GET"));
        Optional<HttpTriplet> httpTriplet = httpTripletList.stream()
                .filter(k -> Objects.equals(k.getMethod(), method))
                .findFirst();
        if(httpTriplet.isPresent()){
            return  httpTriplet;
        }
        return httpTriplet;
    }

}
