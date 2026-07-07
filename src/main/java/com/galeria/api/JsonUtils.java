package com.galeria.api;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static void writeJson(HttpServletResponse response, String json) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
    }

    public static void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        writeJson(response, "{\"error\":\"" + escape(message) + "\"}");
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
