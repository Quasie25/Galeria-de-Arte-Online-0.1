package com.galeria.api;

import com.galeria.carrito.CarritoCalculadora;
import com.galeria.carrito.CarritoParser;
import com.galeria.carrito.CarritoResumen;
import com.galeria.carrito.CarritoSessionKeys;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/carrito")
public class CarritoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Map<Integer, Integer> items = new LinkedHashMap<>();
        int envio = 0;
        if (session != null) {
            items = getItems(session);
            envio = getEnvio(session);
        }
        CarritoResumen resumen = CarritoCalculadora.calcular(items, envio);
        JsonUtils.writeJson(response, resumen.toJson());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<Integer, Integer> items = CarritoParser.parseItems(request.getParameter("items"));
            int envio = CarritoParser.parseEnvio(request.getParameter("envio"));

            HttpSession session = request.getSession(true);
            session.setAttribute(CarritoSessionKeys.ITEMS, items);
            session.setAttribute(CarritoSessionKeys.ENVIO, envio);

            CarritoResumen resumen = CarritoCalculadora.calcular(items, envio);
            JsonUtils.writeJson(response, resumen.toJson());
        } catch (IllegalArgumentException ex) {
            JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getItems(HttpSession session) {
        Object valor = session.getAttribute(CarritoSessionKeys.ITEMS);
        if (valor instanceof Map) {
            return (Map<Integer, Integer>) valor;
        }
        Map<Integer, Integer> items = new LinkedHashMap<>();
        session.setAttribute(CarritoSessionKeys.ITEMS, items);
        return items;
    }

    private int getEnvio(HttpSession session) {
        Object valor = session.getAttribute(CarritoSessionKeys.ENVIO);
        if (valor instanceof Integer) {
            return (Integer) valor;
        }
        return 0;
    }
}
