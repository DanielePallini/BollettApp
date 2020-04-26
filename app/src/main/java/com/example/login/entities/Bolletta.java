package com.example.login.entities;
 public abstract class Bolletta {
    private int id = 0;
    private double costo = 0;
    private String dataScadenza = "";
    private String periodo = "";

     public Bolletta(int id, double costo, String dataScadenza, String periodo) {
         this.id = id;
         this.costo = costo;
         this.dataScadenza = dataScadenza;
         this.periodo = periodo;
     }

     public void setId(int id) {
         this.id = id;
     }

     public void setCosto(double costo) {
         this.costo = costo;
     }

     public void setDataScadenza(String dataScadenza) {
         this.dataScadenza = dataScadenza;
     }

     public void setPeriodo(String periodo) {
         this.periodo = periodo;
     }

     public int getId() {
         return id;
     }

     public double getCosto() {
         return costo;
     }

     public String getDataScadenza() {
         return dataScadenza;
     }

     public String getPeriodo() {
         return periodo;
     }
 }