package com.example.login.entities;

public class BollettaLuce extends Bolletta {
    private double consumo = 0;
    private String misura = "kWh";
    private int max = 0;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public BollettaLuce(int id, double costo, String dataScadenza, String periodo, String finePeriodo, double consumo, String misura, int max){
        super(id, costo, dataScadenza, periodo, finePeriodo);
        this.consumo = consumo;
        this.misura = misura;
        this.max = max;
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
}
