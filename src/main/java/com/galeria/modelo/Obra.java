package com.galeria.modelo;

import java.math.BigDecimal;

public class Obra {

    private int        id;
    private String     titulo;
    private String     descripcion;
    private BigDecimal precio;
    private String     urlImagen;
    private int        artistaId;
    private String     estado;
    private String     motivoRechazo;

    public Obra() {}

    public Obra(int id, String titulo, String descripcion, BigDecimal precio,
                String urlImagen, int artistaId, String estado, String motivoRechazo) {
        this.id            = id;
        this.titulo        = titulo;
        this.descripcion   = descripcion;
        this.precio        = precio;
        this.urlImagen     = urlImagen;
        this.artistaId     = artistaId;
        this.estado        = estado;
        this.motivoRechazo = motivoRechazo;
    }

    public int        getId()            { return id; }
    public String     getTitulo()        { return titulo; }
    public String     getDescripcion()   { return descripcion; }
    public BigDecimal getPrecio()        { return precio; }
    public String     getUrlImagen()     { return urlImagen; }
    public int        getArtistaId()     { return artistaId; }
    public String     getEstado()        { return estado; }
    public String     getMotivoRechazo() { return motivoRechazo; }

    public void setId(int id)                    { this.id = id; }
    public void setTitulo(String t)              { this.titulo = t; }
    public void setDescripcion(String d)         { this.descripcion = d; }
    public void setPrecio(BigDecimal p)          { this.precio = p; }
    public void setUrlImagen(String u)           { this.urlImagen = u; }
    public void setArtistaId(int a)              { this.artistaId = a; }
    public void setEstado(String e)              { this.estado = e; }
    public void setMotivoRechazo(String m)       { this.motivoRechazo = m; }

    @Override
    public String toString() {
        return "Obra{id=" + id + ", titulo='" + titulo + "', estado='" + estado + "'}";
    }
}
