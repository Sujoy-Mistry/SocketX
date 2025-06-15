package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

    private static Logger logger=Logger.getLogger(Main.class.getName());

    public static void main(String[] args){
        Properties props = new Properties();
        int port = 8080; // default

        try {
            props.load(new FileInputStream("application.properties"));  //we can achive the same thing using @value method in SpringBoot Application , but we used JAVA to get more control
            String portStr = props.getProperty("server.port");
            if (portStr != null) {
                port = Integer.parseInt(portStr);
            }
        } catch (IOException e) {
            logger.warning("Could not load config.properties, using default port 8080");
        }

        try ( ServerSocket serverSocket = new ServerSocket(port);){
           logger.info("Server started on port 8080");
            while (true){
                Socket client=serverSocket.accept();
                handelClient(client);
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
            String request;
            while((request=in.readLine())!=null && !request.isEmpty()) {
                System.out.println("Request:" + request);
            }
            // Build HTTP response
            String body = "OK";
            String response =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "\r\n" +       // A blank line (\r\n alone) separates headers from the body
                            body;        //Every line is terminated by \r\n

           outputStream.write(response.getBytes());  // sends bytes directly to the client (browser, Postman, etc.). That's how low-level TCP servers work — they stream raw data over sockets.
           outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}