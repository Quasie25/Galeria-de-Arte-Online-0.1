package com.galeria.modelo;

import java.sql.Timestamp;

public class Favorito {

    private int       id;
    private int       usuarioId;
    private int       obraId;
    private Timestamp creadoEn;

    public Favorito() {}

    public Favorito(int id, int usuarioId, int obraId, Timestamp creadoEn) {
        this.id        = id;
        this.usuarioId = usuarioId;
        this.obraId    = obraId;
        this.creadoEn  = creadoEn;
    }

    public int       getId()        { return id; }
    public int       getUsuarioId() { return usuarioId; }
    public int       getObraId()    { return obraId; }
    public Timestamp getCreadoEn()  { return creadoEn; }

    public void setId(int id)            { this.id = id; }
    public void setUsuarioId(int u)      { this.usuarioId = u; }
    public void setObraId(int o)         { this.obraId = o; }
    public void setCreadoEn(Timestamp t) { this.creadoEn = t; }

    @Override
    public String toString() {
        return "Favorito{id=" + id + ", usuarioId=" + usuarioId + ", obraId=" + obraId + "}";
    }
}
