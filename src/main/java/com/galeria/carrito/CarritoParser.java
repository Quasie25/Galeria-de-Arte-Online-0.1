package com.galeria.carrito;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CarritoParser {
    private CarritoParser() {
    }

    public static Map<Integer, Integer> parseItems(String itemsParam) {
        Map<Integer, Integer> items = new LinkedHashMap<>();
        if (itemsParam == null || itemsParam.isBlank()) {
            return items;
        }
        String[] partes = itemsParam.split(",");
        for (String parte : partes) {
            if (parte.isBlank()) {
                continue;
            }
            String[] datos = parte.split(":");
            if (datos.length != 2) {
                throw new IllegalArgumentException("Formato de items invalido.");
            }
            int id = parsePositive(datos[0], "id");
            int cantidad = parsePositive(datos[1], "cantidad");
            items.merge(id, cantidad, Integer::sum);
        }
        return items;
    }

    public static int parseEnvio(String envioParam) {
        if (envioParam == null || envioParam.isBlank()) {
            return 0;
        }
        try {
            int envio = Integer.parseInt(envioParam.trim());
            if (envio < 0) {
                throw new IllegalArgumentException("El envio no puede ser negativo.");
            }
            return envio;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El envio es invalido.");
        }
    }

    private static int parsePositive(String valor, String nombreCampo) {
        try {
            int parsed = Integer.parseInt(valor.trim());
            if (parsed < 1) {
                throw new IllegalArgumentException("El campo " + nombreCampo + " es invalido.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es invalido.");
        }
    }
}
