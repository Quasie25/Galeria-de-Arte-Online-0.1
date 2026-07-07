package com.galeria.carrito;

public class CarritoResumen {
    private final int subtotal;
    private final int envio;
    private final int total;
    private final boolean carritoVacio;

    public CarritoResumen(int subtotal, int envio, int total, boolean carritoVacio) {
        this.subtotal = subtotal;
        this.envio = envio;
        this.total = total;
        this.carritoVacio = carritoVacio;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public int getEnvio() {
        return envio;
    }

    public int getTotal() {
        return total;
    }

    public boolean isCarritoVacio() {
        return carritoVacio;
    }

    public String toJson() {
        return "{\"subtotal\":" + subtotal
                + ",\"envio\":" + envio
                + ",\"total\":" + total
                + ",\"carritoVacio\":" + carritoVacio + "}";
    }
}
