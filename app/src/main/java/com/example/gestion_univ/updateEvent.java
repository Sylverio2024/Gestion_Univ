package com.example.gestion_univ;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateEvent extends AppCompatActivity {

    private EditText editNumeroEvent, editTitreEvent, editDescriptionEvent;
    private Button btnSaveEvent;
    private DatabaseReference eventReference;
    private String oldNumeroEvent; // Pour garder une trace de l'ancien numéro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event); // Utilisez votre propre layout

        // Initialisation des vues
        editNumeroEvent = findViewById(R.id.UpdateNumeroEvent);
        editTitreEvent = findViewById(R.id.UpdateTitreEvent);
        editDescriptionEvent = findViewById(R.id.UpdateDescriptionEvent);
        btnSaveEvent = findViewById(R.id.bntSaveEvent);

        // Référence à Firebase pour les événements
        eventReference = FirebaseDatabase.getInstance().getReference("Evenement");

        // Supposons que vous ayez passé le numeroEvent actuel via une Intent
        oldNumeroEvent = getIntent().getStringExtra("numeroEvent");

        // Charger les données actuelles dans les EditTexts
        loadEventData();

        // Action du bouton "Modifier"
        btnSaveEvent.setOnClickListener(v -> {
            String newNumeroEvent = editNumeroEvent.getText().toString().trim();
            String titreEvent = editTitreEvent.getText().toString().trim();
            String descriptionEvent = editDescriptionEvent.getText().toString().trim();

            // Vérifier si les champs sont vides
            if (newNumeroEvent.isEmpty() || titreEvent.isEmpty() || descriptionEvent.isEmpty()) {
                Toast.makeText(updateEvent.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier si le numéro a changé et s'il existe déjà dans la base de données
            if (!newNumeroEvent.equals(oldNumeroEvent)) {
                checkForDuplicateNumeroEvent(newNumeroEvent, titreEvent, descriptionEvent);
            } else {
                // Si le numéro n'a pas changé, mettre à jour directement
                updateEventInDatabase(oldNumeroEvent, titreEvent, descriptionEvent);
            }
        });
    }

    // Fonction pour charger les données existantes de l'événement
    private void loadEventData() {
        eventReference.child(oldNumeroEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editNumeroEvent.setText(snapshot.child("numeroEvent").getValue(String.class));
                    editTitreEvent.setText(snapshot.child("titreEvent").getValue(String.class));
                    editDescriptionEvent.setText(snapshot.child("descriptionEvent").getValue(String.class));
                } else {
                    Toast.makeText(updateEvent.this, "Données introuvables", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(updateEvent.this, "Erreur lors du chargement des données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Vérifier si le numéro d'événement est un doublon avant de procéder à la mise à jour
    private void checkForDuplicateNumeroEvent(String newNumeroEvent, String titreEvent, String descriptionEvent) {
        eventReference.child(newNumeroEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Le nouveau numéro existe déjà, affichage d'une erreur
                    Toast.makeText(updateEvent.this, "Numéro d'événement déjà utilisé", Toast.LENGTH_SHORT).show();
                } else {
                    // Pas de doublon, procéder à la mise à jour
                    updateEventInDatabase(oldNumeroEvent, titreEvent, descriptionEvent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(updateEvent.this, "Erreur lors de la vérification du numéro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fonction pour mettre à jour les données de l'événement
    private void updateEventInDatabase(String numeroEvent, String titreEvent, String descriptionEvent) {
        // Vérifier s'il y a des modifications réelles
        if (titreEvent.equals(editTitreEvent.getText().toString()) &&
                descriptionEvent.equals(editDescriptionEvent.getText().toString())) {
            Toast.makeText(updateEvent.this, "Aucune modification détectée", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mise à jour des champs de l'événement dans la base de données
        eventReference.child(numeroEvent).child("titreEvent").setValue(titreEvent);
        eventReference.child(numeroEvent).child("descriptionEvent").setValue(descriptionEvent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(updateEvent.this, "Événement mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Retour à l'écran précédent
                })
                .addOnFailureListener(e -> Toast.makeText(updateEvent.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show());
    }
}