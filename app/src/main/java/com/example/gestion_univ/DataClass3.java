package com.example.gestion_univ;

public class DataClass3 {
    private String numeroSalle;
    private String nameSalle;
    private String descriptionSalle;
    private String statutSalle;
    private String Key3;
    public void setKey3(String key3) {
        Key3 = key3;
    }
    public String getKey3() {
        return Key3;
    }
    public DataClass3(String numeroSalle, String nameSalle, String descriptionSalle, String statutSalle) {
        this.numeroSalle = numeroSalle;
        this.nameSalle = nameSalle;
        this.descriptionSalle = descriptionSalle;
        this.statutSalle = statutSalle;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public String getNameSalle() {
        return nameSalle;
    }

    public String getDescriptionSalle() {
        return descriptionSalle;
    }

    public String getStatutSalle() {
        return statutSalle;
    }

    public DataClass3(){

    }
}
