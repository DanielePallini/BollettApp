package com.example.login.entities;

import androidx.annotation.NonNull;

public class BollettaLGI extends Bolletta {
    private double consumo = 0;
    private String misura = "kWh";
    private String tipo = "";


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BollettaLGI(long id, double costo, String dataScadenza, String periodo, String finePeriodo, double consumo, String tipo){
        super(id, costo, dataScadenza, periodo, finePeriodo);
        this.consumo = consumo;
        this.tipo = tipo;
    }
    public BollettaLGI(long id, double costo, String dataScadenza, String periodo, String finePeriodo, String tipo){
        super(id, costo, dataScadenza, periodo, finePeriodo);
        this.tipo = tipo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public void setMisura(String misura) {
        this.misura = misura;
    }

    public double getConsumo() {
        return consumo;
    }

    public String getMisura() {
        return misura;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
