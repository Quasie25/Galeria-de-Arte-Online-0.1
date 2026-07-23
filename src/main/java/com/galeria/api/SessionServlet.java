package com.galeria.api;

import com.galeria.carrito.CarritoSessionKeys;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/session")
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        String rol = (String) session.getAttribute("rol");
        if (rol == null || rol.isBlank()) {
            rol = "invitado";
            session.setAttribute("rol", rol);
        }

        boolean carritoVacio = true;
        Object items = session.getAttribute(CarritoSessionKeys.ITEMS);
        if (items instanceof Map) {
            carritoVacio = ((Map<?, ?>) items).isEmpty();
        }

        String json = "{\"rol\":\"" + JsonUtils.escape(rol) + "\",\"carritoVacio\":" + carritoVacio + "}";
        JsonUtils.writeJson(response, json);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        JsonUtils.writeJson(response, "{\"ok\":true}");
    }
}