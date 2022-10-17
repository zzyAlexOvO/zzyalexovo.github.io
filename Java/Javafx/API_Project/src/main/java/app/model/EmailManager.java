package app.model;

import app.model.utils.Routes;

import java.io.IOException;

public class EmailManager {
    private String token;
    public EmailManager(String token){
        this.token = token;
    }
    public void send(String fromE, String toE, String subject, String body) throws IOException {
        new Routes().email(token,fromE,toE,subject,body);
    }
}
