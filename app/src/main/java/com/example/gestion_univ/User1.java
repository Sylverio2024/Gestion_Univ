package com.example.gestion_univ;

public class User1 {
    private String NomComplet;
    private String Email;
    private String Telephone;
    private String imageUrl;
    private String role;


    public User1(String NomComplet, String Email, String Telephone, String imageUrl, String role) {
        this.NomComplet = NomComplet;
        this.Email = Email;
        this.Telephone = Telephone;
        this.imageUrl = imageUrl;
        this.role = role;
       // this.status=status;
    }


    public String getTelephone() {
        return Telephone;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getNomComplet() {
        return NomComplet;
    }
    public String getEmail() {
        return Email;
    }
    public String getRole() {
        return role;
    }
   /* public String getStatus() {
        return status;
    }*/

    // Constructeur vide requis pour Firestore
    public User1() {

    }
}