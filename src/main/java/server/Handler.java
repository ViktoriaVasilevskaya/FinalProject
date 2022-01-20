package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;

public class Handler implements HttpHandler {
    private final Map<String, String> map = new ConcurrentHashMap<>();

        @Override
        public void handle(HttpExchange httpExchange) throws IOException { //инкапсулирует полученный HTTP запрос, получает запрос клиента и отправляет ответ
            String requestParamValue=null;

            if("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(httpExchange);
            } else if("POST".equals(httpExchange)) {
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
            if(map.containsKey(element.getName())){
                map.remove(element.getName());
                map.put(element.getName(), element.getValue());
                System.out.println("your data was changed successfully");
            } else {
                map.put(element.getName(), element.getValue());
                System.out.println("your data was saved successfully");
            }
        }
        //если есть такой ключ, то изменить значение в мэпе на новое (удалить старое, внести новое)
        //если такого ключа нет, то внести данные




        return"";//пара ключ-значение
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
            if(map.containsKey(element.getName())){
                System.out.printf("%s : %s\n", element.getName(), map.get(element.getName()));
            } else {
                System.out.println("your data was not found");
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
    public void remove(String key){
            map.remove(key);
    }
    //описать метод remove;
}
//в пост описать протокол общения узлов, отправку изменений
//придумать, как хранить список всех узлов
