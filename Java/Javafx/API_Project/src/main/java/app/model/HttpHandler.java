package app.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpHandler {
    public static HttpResponse<String> postRequest(Map<String,String> header,String body, String URL) {
        try {
            var objectMapper = new ObjectMapper();
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(URL));
            for (String i:header.keySet()){
                builder.setHeader(i,header.get(i));
            }
            HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(body)).build();
            System.out.println("headers: " + request.headers().toString());
            System.out.println("request: " + request.toString());

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            System.out.println("URL error");
            System.out.println("Bad URL: " + URL);
            return null;
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static HttpResponse getRequest(String URL) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(URL))
                    .GET()
                    .build();

            System.out.println("request: " + request.toString());
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
            return response;

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            System.out.println("incorrect URL " + URL);
            return null;
        }
    }

    public static HttpResponse<String> putRequest(Map<String,String> values,String URL) {
        try {
            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper
                    .writeValueAsString(values);
            HttpRequest.Builder builder = HttpRequest.newBuilder(new URI(URL));
            for (String i:values.keySet()){
                builder.setHeader(i,values.get(i));
            }
            HttpRequest request = builder.PUT(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            System.out.println(request.bodyPublisher().toString());

            HttpClient client = HttpClient.newBuilder().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
            return response;
        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            System.out.println("URL error");
            return null;
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }
    }

    public static HttpResponse deleteRequest(String URL) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(URL))
                    .DELETE()
                    .build();

            System.out.println("request: " + request.toString());
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status code was: " + response.statusCode());
            System.out.println("Response headers were: " + response.headers());
            System.out.println("Response body was:\n" + response.body());
            return response;

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
            return null;
        }
    }

}
