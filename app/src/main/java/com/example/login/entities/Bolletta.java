package com.example.login.entities;
 public abstract class Bolletta {
    private long id = 0;
    private double costo = 0;
    private String dataScadenza = "";
    private String periodo = "";
    private String finePeriodo = "";

     public String getFinePeriodo() {
         return finePeriodo;
     }

     public void setFinePeriodo(String finePeriodo) {
         this.finePeriodo = finePeriodo;
     }

     public Bolletta(long id, double costo, String dataScadenza, String periodo, String finePeriodo) {
         this.id = id;
         this.costo = costo;
         this.dataScadenza = dataScadenza;
         this.periodo = periodo;
         this.finePeriodo = finePeriodo;
     }

     public void setId(long id) {
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

     public long getId() {
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
