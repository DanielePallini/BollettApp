package com.example.login.entities;

import androidx.annotation.NonNull;

public class BollettaLuce extends Bolletta {
    private double consumo = 0;
    private String misura = "kWh";



    public BollettaLuce(int id, double costo, String dataScadenza, String periodo, String finePeriodo, double consumo){
        super(id, costo, dataScadenza, periodo, finePeriodo);
        this.consumo = consumo;
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
