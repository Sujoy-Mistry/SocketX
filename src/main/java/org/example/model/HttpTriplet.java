package org.example.model;

public class HttpTriplet {
    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getMethod() {
        return method;
    }

    public HttpTriplet() {
    }

    public HttpTriplet(int statusCode, String statusMessage, String method) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.method = method;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private int statusCode;
    private String statusMessage;
    private String method;
}
