package com.galeria.carrito;

import java.util.Map;

public final class ProductoCatalogo {
    private static final Map<Integer, Integer> PRECIOS = Map.of(
            1, 250000,
            2, 180000,
            3, 310000);

    private ProductoCatalogo() {
    }

    public static int precioPorId(int id) {
        Integer precio = PRECIOS.get(id);
        if (precio == null) {
            throw new IllegalArgumentException("Producto no encontrado: " + id);
        }
        return precio;
    }
}
