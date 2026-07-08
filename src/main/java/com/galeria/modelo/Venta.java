package com.galeria.modelo;

import java.sql.Timestamp;

public class Venta {

    private int       id;
    private int       usuarioId;
    private int       obraId;
    private Timestamp fechaVenta;

    public Venta() {}

    public Venta(int id, int usuarioId, int obraId, Timestamp fechaVenta) {
        this.id        = id;
        this.usuarioId = usuarioId;
        this.obraId    = obraId;
        this.fechaVenta = fechaVenta;
    }

    public int       getId()        { return id; }
    public int       getUsuarioId() { return usuarioId; }
    public int       getObraId()    { return obraId; }
    public Timestamp getFechaVenta(){ return fechaVenta; }

    public void setId(int id)              { this.id = id; }
    public void setUsuarioId(int u)        { this.usuarioId = u; }
    public void setObraId(int o)           { this.obraId = o; }
    public void setFechaVenta(Timestamp f) { this.fechaVenta = f; }

    @Override
    public String toString() {
        return "Venta{id=" + id + ", usuarioId=" + usuarioId + ", obraId=" + obraId + "}";
    }
}
