package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.example.handler.ClientHandler;
import org.example.model.*;

public class Main {

    private static Logger logger=Logger.getLogger(Main.class.getName());

    public static void main(String[] args){
        Properties props = new Properties();
        int port = 8080; // default

        try {
            props.load(new FileInputStream("src/main/resources/application.properties"));  //we can achive the same thing using @value method in SpringBoot Application , but we used JAVA to get more control
            String portStr = props.getProperty("server.port");
            if (portStr != null) {
                port = Integer.parseInt(portStr);
            }
        } catch (IOException e) {
            logger.warning("Could not load application.properties, using default port 8080");
        }

        try ( ServerSocket serverSocket = new ServerSocket(port);){
           logger.info("Server started on port 8080");
            ExecutorService executorService= Executors.newFixedThreadPool(20);
            while(true){
                Socket client=serverSocket.accept();
                System.out.println("--- Client connected from------: " + client.getInetAddress() + ":" + client.getPort());
                executorService.submit(()->handelClient(client));
            }
        }catch (Exception ex){
            logger.info("Something Wrong happen in Server Socket");
        }


    }
    private static void handelClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream())); //client.getInputStream provide raw ascii value in byte format
             //new InputStreamReader make it readable character
             //BufferedReader provide some custom method to read the data line by line
             OutputStream outputStream = client.getOutputStream(); //to return back the output to the server
        ) {
            HttpRequest  httpRequest=new HttpRequest();
            String request=in.readLine();
            if(request==null){
                logger.info("client send a proxy connection");
                return;
            }
            System.out.println("Request:" +request);
            String [] strings = new String[3];  // Method, Path, version
            if(!request.isEmpty()) {  //at 51 we have checked whether the request is null ot not
                strings = request.split(" ");
            }
            httpRequest.setMethod(strings[0]);
            httpRequest.setPath(strings[1]);
            httpRequest.setVersion(strings[2]);
            for(String str:strings){
                System.out.println(str);
            }
            if(strings[1]!=null&&strings[1].equals("/favicon.ico")){  //checking whether the browser make a dummy request to get the icon, if yes we are ignoring it
                String response = "HTTP/1.1 204 No Content\r\n\r\n";
                outputStream.write(response.getBytes());
                outputStream.flush();
                return;
            }
            //handle malformed request
            if (strings.length != 3) {
                outputStream.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                outputStream.flush();
                return;
            }



            Map<String, String> headers = new HashMap<>();
            while ((request = in.readLine()) != null && !request.isEmpty()) {
                System.out.println("Request: " + request);

                if (request.contains(":")) {
                    int idx = request.indexOf(":");
                    String key = request.substring(0, idx).trim();
                    String value = request.substring(idx + 1).trim();
                    headers.put(key, value);
                }
            }
            httpRequest.setHeaders(headers);
            if (httpRequest.getPath().contains("api") || ( !httpRequest.getPath().contains(".") && (!httpRequest.getPath().contains("/")))) {
                ClientHandler.registerAnnotatedRoutes();
                ClientHandler.requestHandler(httpRequest.getPath(),httpRequest.getMethod(),outputStream);
            } else {
                ClientHandler.getFile(httpRequest.getPath(), outputStream);
            }

            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}