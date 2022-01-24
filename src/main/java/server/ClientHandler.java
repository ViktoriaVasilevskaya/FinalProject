package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import java.net.http.*;
import java.util.Optional;

public class ClientHandler implements HttpHandler {
    private HttpClient httpClient;
    private LRUCache cache;
    private ArrayList<NodeEndpoint> endpoints;

    public ClientHandler(LRUCache cache, ArrayList<NodeEndpoint> endpoints) {
        this.cache = cache;
        httpClient = java.net.http.HttpClient.newHttpClient();
        this.endpoints = endpoints;
    }

    private void updateOtherNodes(String name, String value) {
        for (NodeEndpoint ep : endpoints) {
            StringBuilder sb = new StringBuilder();
            sb.append("http://").append(ep.host).append(":").append(ep.port).append("/endpoint?").
                    append(name).append("=").append(value);
            System.out.println(sb.toString());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(sb.toString()))
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println)
                    .join();
        }

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException { //инкапсулирует полученный HTTP запрос, получает запрос клиента и отправляет ответ
        String requestParamValue=null;

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        } else if("POST".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(httpExchange);
        }

        handleResponse(httpExchange,requestParamValue);
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(httpExchange
                    .getRequestURI()
                    .toString());
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            return "";
        }
        List<NameValuePair> params = builder.getQueryParams();
        for(NameValuePair element : params){
            cache.put(element.getName(), element.getValue());
            updateOtherNodes(element.getName(), element.getValue());
            if(cache.contains(element.getName())){
                System.out.println("your data was changed successfully");
                return "already exists";
            } else {
                System.out.println("your data was saved successfully");
                return "success";
            }
        }

        return "";//пара ключ-значение
    }
    private String handleGetRequest(HttpExchange httpExchange) {
        URIBuilder builder;
        try {
            builder = new URIBuilder(httpExchange
                    .getRequestURI()
                    .toString());
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            return "";
        }

        List<NameValuePair> params = builder.getQueryParams();
        for (NameValuePair element : params) {
            System.out.printf("%s : %s\n", element.getName(), element.getValue());
        }
        //если есть такой ключ, то возвращаю значение
        //если нет ключа, то (в идеале) иду в базу данный и беру оттуда
        //иначе говорю, что null
        //из запроса пользователья мы можем получить только ключ, откуда в листе значение???
        for(NameValuePair element : params){
            if(cache.contains(element.getName())){
                System.out.printf("%s : %s\n", element.getName(), cache.get(element.getName()));
                return cache.get(element.getName());
            } else {
                System.out.println("your data was not found");
                return "null";
            }
        }
        //добыть параметр запроса, который будет ключем
        //разбить строку по вопросу и по равно, проверить, что слева key
        return "";
    }

    private void handleResponse(HttpExchange httpExchange, String paramValue)throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();
//вернуть ответ на запрос ключа


        // encode HTML content
//            String htmlResponse = StringEscapeUtils.escapeHtml4(paramValue);

        // this line is a must
        httpExchange.sendResponseHeaders(200, paramValue.length());

        outputStream.write(paramValue.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
//в пост описать протокол общения узлов, отправку изменений
//придумать, как хранить список всех узлов
