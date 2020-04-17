package com.example.login.entities;

public class Recensione {
    private String titolo;
    private int voto;
    private String testo;

    public Recensione(String titolo, int voto, String testo){
        this.titolo = titolo;
        this.voto = voto;
        this.testo = testo;
    }

    public int getVoto() {
        return voto;
    }

    public String getTesto() {
        return testo;
    }

    public String getTitolo() {
        return titolo;
    }
}
