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

public class p2pHandler implements HttpHandler {
    private final Map<String, String> map = new ConcurrentHashMap<>();

    private LRUCache cache;

    public p2pHandler(LRUCache cache) {
        this.cache = cache;
    }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException { //инкапсулирует полученный HTTP запрос, получает запрос клиента и отправляет ответ
            String requestParamValue = null;

            if("POST".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handlePostRequest(httpExchange);
            } else {
                return;
            }

            handleResponse(httpExchange, requestParamValue);
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
            map.put(element.getName(), element.getValue());
            if(map.containsKey(element.getName())){
                System.out.println("your data was changed successfully");
            } else {
                System.out.println("your data was saved successfully");
            }
            return "ok";
        }

        return "";//пара ключ-значение
    }

    private void handleResponse(HttpExchange httpExchange, String paramValue)throws  IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
//            StringBuilder htmlBuilder = new StringBuilder();
            // encode HTML content
//            String htmlResponse = StringEscapeUtils.escapeHtml4(paramValue);

            // this line is a must
            httpExchange.sendResponseHeaders(200, paramValue.length());// код возврата и длина тела ответа

            outputStream.write(paramValue.getBytes());
            outputStream.flush();
            outputStream.close();
    }
}
