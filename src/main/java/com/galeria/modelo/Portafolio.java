package com.galeria.modelo;

public class Portafolio {

    private int    id;
    private int    artistaId;
    private String biografia;
    private String linkPortafolio;

    public Portafolio() {}

    public Portafolio(int id, int artistaId, String biografia, String linkPortafolio) {
        this.id             = id;
        this.artistaId      = artistaId;
        this.biografia      = biografia;
        this.linkPortafolio = linkPortafolio;
    }

    public int    getId()             { return id; }
    public int    getArtistaId()      { return artistaId; }
    public String getBiografia()      { return biografia; }
    public String getLinkPortafolio() { return linkPortafolio; }

    public void setId(int id)                  { this.id = id; }
    public void setArtistaId(int a)            { this.artistaId = a; }
    public void setBiografia(String b)         { this.biografia = b; }
    public void setLinkPortafolio(String l)    { this.linkPortafolio = l; }

    @Override
    public String toString() {
        return "Portafolio{id=" + id + ", artistaId=" + artistaId + "}";
    }
}
