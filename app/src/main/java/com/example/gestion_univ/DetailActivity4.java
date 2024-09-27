package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DetailActivity4 extends AppCompatActivity {

    private RecyclerView imageRecyclerView;
    private List<String> imageUrls;
    private Adapter1 adapter;
    FloatingActionButton deleteButton, editButton;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String numeroEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail4);

        // Initialisation des vues
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Références Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");
        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");

        // Récupérer les données de l'intent
        numeroEvent = getIntent().getStringExtra("NumeroIDEvent");
        imageUrls = getIntent().getStringArrayListExtra("imagesEvent");

        // Configuration du RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter1(this, imageUrls, imageUrl -> {
            // Ouvrir l'image en plein écran (si nécessaire)
            Intent intent = new Intent(DetailActivity4.this, FullScreenImageActivity.class);
            intent.putExtra("imageUrl", imageUrl);
            startActivity(intent);
        });
        imageRecyclerView.setAdapter(adapter);

        // Action de suppression
        deleteButton.setOnClickListener(v -> deleteEvent());
        // Action de modification
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity4.this, updateEvent.class);
            intent.putExtra("NumeroIDEvent", numeroEvent);
            intent.putExtra("TitreEvent", getIntent().getStringExtra("titreEvent")); // Passe le titre
            intent.putExtra("DescriptionEvent", getIntent().getStringExtra("descriptionEvent")); // Passe la description
            startActivity(intent);
        });
    }


    private void deleteEvent() {
        // Supprimer l'événement de la Realtime Database
        databaseReference.child(numeroEvent).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Suppression réussie dans la base de données, maintenant supprimer les images
                    deleteEventImages();
                })
                .addOnFailureListener(e -> {
                    // Afficher une erreur en cas d'échec
                    Toast.makeText(DetailActivity4.this, "Erreur lors de la suppression de l'événement", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteEventImages() {
        // Parcourir les URL des images et les supprimer du Storage
        for (String imageUrl : imageUrls) {
            // Extraire le nom du fichier depuis l'URL
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            imageRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Image supprimée avec succès
                        Toast.makeText(DetailActivity4.this, "Images supprimées avec succès", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // En cas d'erreur lors de la suppression de l'image
                        Toast.makeText(DetailActivity4.this, "Erreur lors de la suppression des images", Toast.LENGTH_SHORT).show();
                    });
        }

        // Retour à la liste des événements ou à une autre activité
        finish();
    }
}