package com.example.login.entities;

public class BollettaLuce extends Bolletta {
    private double consumo = 0;
    private String misura = "kWh";
    public BollettaLuce(int id, double costo, String dataScadenza, String periodo, double consumo, String misura){
        super(id, costo, dataScadenza, periodo);
        this.consumo = consumo;
        this.misura = misura;
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
