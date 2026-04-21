package com.auth.web;

import com.auth.service.AuthService;
import com.auth.model.AuthResult;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler implements HttpHandler {

    private final AuthService authService;

    public ApiHandler() {
        this.authService = new AuthService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendJsonResponse(exchange, 405, false, "Method Not Allowed");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        
        Map<String, String> data = parseSimpleJson(body);
        
        String username = data.get("username");
        String password = data.get("password");
        
        if (username == null || password == null) {
            sendJsonResponse(exchange, 400, false, "Missing credentials");
            return;
        }

        AuthResult result;
        if ("/api/login".equals(path)) {
            result = authService.loginUser(username, password);
        } else if ("/api/register".equals(path)) {
            String email = data.get("email");
            if (email == null) {
                sendJsonResponse(exchange, 400, false, "Missing email");
                return;
            }
            result = authService.registerUser(username, email, password);
        } else {
            sendJsonResponse(exchange, 404, false, "Not found");
            return;
        }

        sendJsonResponse(exchange, result.isSuccess() ? 200 : 401, result.isSuccess(), result.getMessage());
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, boolean success, String message) throws IOException {
        String safeMessage = message.replace("\"", "\\\"");
        String response = String.format("{\"success\": %b, \"message\": \"%s\"}", success, safeMessage);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) return map;

        json = json.trim().replaceAll("^\\{|\\}$", "").trim();
        
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replaceAll("^\"|\"$", "");
                String value = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, value);
            }
        }
        return map;
    }
}
