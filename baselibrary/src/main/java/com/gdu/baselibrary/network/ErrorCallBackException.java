package com.gdu.baselibrary.network;

import java.io.IOException;

/**
 * @author wixche
 */
public class ErrorCallBackException extends IOException {
    private String jsonContent;

    public ErrorCallBackException() {
        super();
    }

    public ErrorCallBackException(String message) {
        super(message);
    }

    public ErrorCallBackException(String message, String content) {
        super(message);
        jsonContent = content;
    }


    public ErrorCallBackException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
