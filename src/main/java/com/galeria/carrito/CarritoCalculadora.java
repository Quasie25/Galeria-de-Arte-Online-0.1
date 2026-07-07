package com.galeria.carrito;

import java.util.Map;

public final class CarritoCalculadora {
    private CarritoCalculadora() {
    }

    public static CarritoResumen calcular(Map<Integer, Integer> items, int envio) {
        int subtotal = 0;
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            int precio = ProductoCatalogo.precioPorId(entry.getKey());
            subtotal += precio * entry.getValue();
        }
        int total = subtotal + envio;
        return new CarritoResumen(subtotal, envio, total, items.isEmpty());
    }
}
