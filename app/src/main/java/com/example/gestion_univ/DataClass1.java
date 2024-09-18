package com.example.gestion_univ;

public class DataClass1{
    private String numeroIDE;
    private String numInscriptionE;
    private String nameE;
    private String prenomE;
    private String dateNaissanceE;
    private String adresseE;
    private String mentionE;
    private String parcoursE;
    private String niveauE;
    private String telephoneE;
    private String imageE;
    private String key1;

    public void setKey1(String key1) {
        this.key1 = key1;
    }
    public String getKey1() {
        return key1;
    }

    public DataClass1(String numeroIDE, String numInscriptionE, String nameE, String prenomE, String mentionE, String parcoursE, String niveauE, String dateNaissanceE, String adresseE, String telephoneE, String imageE) {
        this.numeroIDE = numeroIDE;
        this.numInscriptionE = numInscriptionE;
        this.nameE = nameE;
        this.prenomE = prenomE;
        this.dateNaissanceE = dateNaissanceE;
        this.adresseE = adresseE;
        this.mentionE = mentionE;
        this.parcoursE = parcoursE;
        this.niveauE = niveauE;
        this.telephoneE = telephoneE;
        this.imageE = imageE;
    }

    public String getNumeroIDE() {
        return numeroIDE;
    }

    public String getNumInscriptionE() {
        return numInscriptionE;
    }

    public String getNameE() {
        return nameE;
    }

    public String getPrenomE() {
        return prenomE;
    }

    public String getDateNaissanceE() {
        return dateNaissanceE;
    }

    public String getAdresseE() {
        return adresseE;
    }

    public String getMentionE() {
        return mentionE;
    }

    public String getParcoursE() {
        return parcoursE;
    }

    public String getNiveauE() {
        return niveauE;
    }

    public String getTelephoneE() {
        return telephoneE;
    }

    public String getImageE() {
        return imageE;
    }

    public DataClass1(){

    }

}
