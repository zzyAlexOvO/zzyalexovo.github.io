package app.model.utils;

import app.model.HttpHandler;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Routes {
    public HttpResponse search(String pattern, String ts, String publicKey, String hash) {
        HttpResponse response;
        if (pattern == null || pattern.isEmpty()){
            response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/characters?ts=%s&apikey=%s&hash=%s",ts,publicKey,hash));
        }else {
            response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/characters?nameStartsWith=%s&ts=%s&apikey=%s&hash=%s", pattern, ts, publicKey, hash));
        }
        return response;
    }
    public HttpResponse character(int id, String ts, String publicKey, String hash){
        HttpResponse response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/characters/%d?ts=%s&apikey=%s&hash=%s",id, ts,publicKey,hash));
        return response;
    }
    public HttpResponse character(String name, String ts, String publicKey, String hash){
        HttpResponse response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s&ts=%s&apikey=%s&hash=%s",name, ts,publicKey,hash));
        return response;
    }
    public HttpResponse characterComics(int id, String ts, String publicKey, String hash){
        HttpResponse response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/characters/%d/comics?ts=%s&apikey=%s&hash=%s",id, ts,publicKey,hash));
        return response;
    }
    public HttpResponse comic(int id, String ts, String publicKey, String hash){
        HttpResponse response = HttpHandler.getRequest(String.format("https://gateway.marvel.com:443/v1/public/comics/%d?ts=%s&apikey=%s&hash=%s",id, ts,publicKey,hash));
        return response;
    }
    public HttpResponse email(String token, String from, String to, String subject, String content){
        Map<String,String> header = new HashMap<>(){{
            put("Authorization","Bearer " + token);
            put("Content-Type", "application/json");
        }};
        String body = String.format("{\"personalizations\": [{\"to\": [{\"email\": \"%s\"}]}],\"from\": {\"email\": \"%s\"},\"subject\": \"%s\",\"content\": [{\"type\": \"text/html\", \"value\": \"%s\"}]}",to,from,subject,content);
        System.out.println("Content: " + content);
        HttpResponse response = HttpHandler.postRequest(header,body,"https://api.sendgrid.com/v3/mail/send");
        return response;
    }
}
