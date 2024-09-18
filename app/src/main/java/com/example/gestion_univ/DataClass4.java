package com.example.gestion_univ;


public class DataClass4 {
    private String numeroEvent;
    private String titreEvent;
    private String dateEvent;            // Date de l'événement
    private String timeEvent;
    private String descriptionEvent;
    private String imageEvent;
    private String key4;

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

    public DataClass4(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent, String imageEvent) {
        this.numeroEvent = numeroEvent;
        this.titreEvent = titreEvent;
        this.dateEvent = dateEvent;
        this.timeEvent = timeEvent;
        this.descriptionEvent = descriptionEvent;
        this.imageEvent = imageEvent;
    }
    public String getNumeroEvent() {
        return numeroEvent;
    }

    public String getTitreEvent() {
        return titreEvent;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public String getDescriptionEvent() {
        return descriptionEvent;
    }

    public String getImageEvent() {
        return imageEvent;
    }
    public DataClass4(){

    }
}
