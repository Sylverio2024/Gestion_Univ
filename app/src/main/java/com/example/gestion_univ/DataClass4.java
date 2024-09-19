package com.example.gestion_univ;

import java.util.List;

public class DataClass4 {
    private String numeroEvent;
    private String titreEvent;
    private String dateEvent;            // Date de l'événement
    private String timeEvent;
    private String descriptionEvent;
    private List<String> imagesEvent;    // Liste des images
    private String key4;

    // Constructeur avec tous les champs
    public DataClass4(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent, List<String> imagesEvent) {
        this.numeroEvent = numeroEvent;
        this.titreEvent = titreEvent;
        this.dateEvent = dateEvent;
        this.timeEvent = timeEvent;
        this.descriptionEvent = descriptionEvent;
        this.imagesEvent = imagesEvent;
    }

    // Constructeur par défaut
    public DataClass4() {
    }

    // Getters et Setters
    public String getNumeroEvent() {
        return numeroEvent;
    }

    public void setNumeroEvent(String numeroEvent) {
        this.numeroEvent = numeroEvent;
    }

    public String getTitreEvent() {
        return titreEvent;
    }

    public void setTitreEvent(String titreEvent) {
        this.titreEvent = titreEvent;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public String getDescriptionEvent() {
        return descriptionEvent;
    }

    public void setDescriptionEvent(String descriptionEvent) {
        this.descriptionEvent = descriptionEvent;
    }

    public List<String> getImagesEvent() {
        return imagesEvent;
    }

    public void setImagesEvent(List<String> imagesEvent) {
        this.imagesEvent = imagesEvent;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }
}