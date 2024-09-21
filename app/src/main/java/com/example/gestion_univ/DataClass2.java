package com.example.gestion_univ;

public class DataClass2 {
    private String numeroCours;
    private String nameCours;
    private String salleCours;
    private String parcoursCours;
    private String niveauCours;
    private String descriptionCours;
    private String dateCours; // Anciennement currentDate
    private String timeCours; // Anciennement currentTime
    private String Key2;

    public String getKey2() {
        return Key2;
    }

    public void setKey2(String key2) {
        Key2 = key2;
    }

    public DataClass2(String numeroCours, String nameCours, String salleCours, String parcoursCours, String niveauCours, String descriptionCours, String dateCours, String timeCours) {
        this.numeroCours = numeroCours;
        this.nameCours = nameCours;
        this.salleCours = salleCours;
        this.parcoursCours = parcoursCours;
        this.niveauCours = niveauCours;
        this.descriptionCours = descriptionCours;
        this.dateCours = dateCours;
        this.timeCours = timeCours;
    }

    public String getNumeroCours() {
        return numeroCours;
    }

    public String getNameCours() {
        return nameCours;
    }

    public String getSalleCours() {
        return salleCours;
    }

    public String getParcoursCours() {
        return parcoursCours;
    }

    public String getNiveauCours() {
        return niveauCours;
    }

    public String getDescriptionCours() {
        return descriptionCours;
    }

    public String getDateCours() { // Anciennement getCurrentDate
        return dateCours;
    }

    public String getTimeCours() { // Anciennement getCurrentTime
        return timeCours;
    }

    public DataClass2() {

    }
}