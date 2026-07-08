package com.galeria.modelo;

public class Usuario {

    private int    id;
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private String estado;

    public Usuario() {}

    public Usuario(int id, String nombre, String email,
                   String password, String rol, String estado) {
        this.id       = id;
        this.nombre   = nombre;
        this.email    = email;
        this.password = password;
        this.rol      = rol;
        this.estado   = estado;
    }

    public int    getId()       { return id; }
    public String getNombre()   { return nombre; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getRol()      { return rol; }
    public String getEstado()   { return estado; }

    public void setId(int id)           { this.id = id; }
    public void setNombre(String n)     { this.nombre = n; }
    public void setEmail(String e)      { this.email = e; }
    public void setPassword(String p)   { this.password = p; }
    public void setRol(String r)        { this.rol = r; }
    public void setEstado(String e)     { this.estado = e; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + nombre + "', rol='" + rol + "'}";
    }
}
