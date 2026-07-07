package com.galeria.filtros;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class FiltroAutenticacion implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);

        if (session.getAttribute("rol") == null) {
            session.setAttribute("rol", "invitado");
        }

        String path = req.getServletPath();
        String rol  = (String) session.getAttribute("rol");

        // Rutas exclusivas para admin
        if (path.startsWith("/api/admin") || path.contains("admin-panel")) {
            if (!"admin".equals(rol)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
