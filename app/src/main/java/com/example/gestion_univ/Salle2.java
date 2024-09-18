package com.example.gestion_univ;

public class Salle2 {
    private String statutSalle;
    private String nameSalle;
    private String numeroSalle;
    private String descriptionSalle;

    // Constructeur sans argument (nécessaire pour Firebase)
    public Salle2() {
        // Constructeur vide requis pour Firebase
    }

    // Constructeur avec arguments
    public Salle2(String statutSalle, String nameSalle, String numeroSalle, String descriptionSalle) {
        this.statutSalle = statutSalle;
        this.nameSalle = nameSalle;
        this.numeroSalle = numeroSalle;
        this.descriptionSalle = descriptionSalle;
    }

    // Getters et Setters
    public String getStatutSalle() { return statutSalle; }
    public void setStatutSalle(String statutSalle) { this.statutSalle = statutSalle; }

    public String getNameSalle() { return nameSalle; }
    public void setNameSalle(String nameSalle) { this.nameSalle = nameSalle; }

    public String getNumeroSalle() { return numeroSalle; }
    public void setNumeroSalle(String numeroSalle) { this.numeroSalle = numeroSalle; }

    public String getDescriptionSalle() { return descriptionSalle; }
    public void setDescriptionSalle(String descriptionSalle) { this.descriptionSalle = descriptionSalle; }
}