package server.message;

import server.message.Response;

public class Failure implements Response {
    private final String errorMessage;

    public Failure(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
