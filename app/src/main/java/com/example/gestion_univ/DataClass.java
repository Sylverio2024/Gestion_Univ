package com.example.gestion_univ;

public class DataClass {
    private String numeroIDT;
    private String nameT;
    private String prenomT;
    private String specialiteT;
    private String adresseT;
    private String categorieT;
    private String telephoneT;
    private String imageT;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumeroIDT() {
        return numeroIDT;
    }

    public String getNameT() {
        return nameT;
    }

    public String getPrenomT() {
        return prenomT;
    }

    public String getSpecialiteT() {
        return specialiteT;
    }

    public String getAdresseT() {
        return adresseT;
    }

    public String getCategorieT() {
        return categorieT;
    }

    public String getTelephoneT() {
        return telephoneT;
    }

    public String getImageT() {
        return imageT;
    }


    public DataClass(String numeroIDT, String nameT, String prenomT, String specialiteT, String adresseT, String categorieT, String telephoneT, String imageT) {
        this.numeroIDT = numeroIDT;
        this.nameT = nameT;
        this.prenomT = prenomT;
        this.specialiteT = specialiteT;
        this.adresseT = adresseT;
        this.categorieT = categorieT;
        this.telephoneT = telephoneT;
        this.imageT = imageT;
    }

    public DataClass(){

    }

}
