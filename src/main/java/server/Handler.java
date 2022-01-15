package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URIBuilder;

public class Handler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue=null;

            if("GET".equals(httpExchange.getRequestMethod())) {
                requestParamValue = handleGetRequest(httpExchange);
            } else if("POST".equals(httpExchange)) {
                requestParamValue = handlePostRequest(httpExchange);
            }

            handleResponse(httpExchange,requestParamValue);
        }

    private String handlePostRequest(HttpExchange httpExchange) {
        return httpExchange
                .getRequestURI()
                .toString() + "post";
        //пара ключ-значение
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        URIBuilder builder;
            try {
                builder = new URIBuilder(httpExchange.
                        getRequestURI()
                        .toString());
            } catch (URISyntaxException e) {
                System.out.println(e.getMessage());
                return "";
            }

        List<NameValuePair> params = builder.getQueryParams();
        for (NameValuePair element : params) {
            System.out.printf("%s : %s\n", element.getName(), element.getValue());
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
