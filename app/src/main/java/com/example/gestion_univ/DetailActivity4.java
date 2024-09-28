package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DetailActivity4 extends AppCompatActivity {

    private RecyclerView imageRecyclerView;
    private List<String> imageUrls;
    private Adapter1 adapter;

    private TextView detailNumero, detailTitre, detailDescription, detailDate, detailTime;
    private FloatingActionButton deleteButton, editButton;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String numeroEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail4);

        // Initialisation des vues
        imageRecyclerView = findViewById(R.id.imageRecyclerView);
        detailNumero = findViewById(R.id.detailNumero);
        detailTitre = findViewById(R.id.detailTitre);
        detailDescription = findViewById(R.id.detailDescription);
        detailDate = findViewById(R.id.detailDate);
        detailTime = findViewById(R.id.detailTime);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        // Références Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");
        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");

        // Récupérer les données de l'intent
        numeroEvent = getIntent().getStringExtra("numeroEvent");
        String titreEvent = getIntent().getStringExtra("titreEvent");
        String descriptionEvent = getIntent().getStringExtra("descriptionEvent");
        String dateEvent = getIntent().getStringExtra("dateEvent");
        String heureEvent = getIntent().getStringExtra("heureEvent");
        imageUrls = getIntent().getStringArrayListExtra("imagesEvent");
        // Afficher les données dans les TextView correspondants
        detailNumero.setText(numeroEvent);
        detailTitre.setText(titreEvent);
        detailDescription.setText(descriptionEvent);
        detailDate.setText(dateEvent);
        detailTime.setText(heureEvent);
        // Configuration du RecyclerView pour les images
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter1(this, imageUrls, imageUrl -> {
            // Ouvrir l'image en plein écran
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
            intent.putExtra("numeroEvent", numeroEvent);
            intent.putExtra("TitreEvent", titreEvent);
            intent.putExtra("DescriptionEvent", descriptionEvent);
            startActivity(intent);
        });
    }
    private void deleteEvent() {
        // Vérifier si l'événement existe avant de tenter de le supprimer
        databaseReference.child(numeroEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // L'événement existe, récupérer les URLs des images avant de supprimer l'événement
                    List<String> imageUrls = (List<String>) dataSnapshot.child("imagesEvent").getValue();
                    // Supprimer l'événement de la Realtime Database
                    databaseReference.child(numeroEvent).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Si des images existent, les supprimer de Firebase Storage
                                if (imageUrls != null && !imageUrls.isEmpty()) {
                                    deleteEventImages(imageUrls);
                                } else {
                                    Toast.makeText(DetailActivity4.this, "Événement supprimé avec succès", Toast.LENGTH_SHORT).show();
                                    finish();  // Retour à la liste des événements
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(DetailActivity4.this, "Erreur lors de la suppression de l'événement", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // L'événement n'existe pas dans la base de données
                    Toast.makeText(DetailActivity4.this, "L'événement n'existe pas ou a déjà été supprimé", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity4.this, "Erreur lors de la récupération de l'événement", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteEventImages(List<String> imageUrls) {
        // Parcourir les URL des images et les supprimer du Storage
        for (String imageUrl : imageUrls) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            imageRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Image supprimée avec succès
                    })
                    .addOnFailureListener(e -> Toast.makeText(DetailActivity4.this, "Erreur lors de la suppression des images", Toast.LENGTH_SHORT).show());
        }
        // Retour à la liste des événements après la suppression des images
        Toast.makeText(DetailActivity4.this, "Événement et images supprimés avec succès", Toast.LENGTH_SHORT).show();
        finish();  // Retour à la liste des événements
    }
}