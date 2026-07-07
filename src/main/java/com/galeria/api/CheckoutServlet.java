package com.galeria.api;

import com.galeria.carrito.CarritoCalculadora;
import com.galeria.carrito.CarritoParser;
import com.galeria.carrito.CarritoResumen;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/checkout/validar")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<Integer, Integer> items = CarritoParser.parseItems(request.getParameter("items"));
            int envio = CarritoParser.parseEnvio(request.getParameter("envio"));
            CarritoResumen resumen = CarritoCalculadora.calcular(items, envio);
            boolean valido = !items.isEmpty();

            String json = "{\"valido\":" + valido
                    + ",\"subtotal\":" + resumen.getSubtotal()
                    + ",\"envio\":" + resumen.getEnvio()
                    + ",\"total\":" + resumen.getTotal()
                    + "}";
            JsonUtils.writeJson(response, json);
        } catch (IllegalArgumentException ex) {
            JsonUtils.writeError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }
}
