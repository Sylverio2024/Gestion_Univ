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

import java.util.ArrayList;
import java.util.List;

public class DetailActivity4 extends AppCompatActivity {

    private RecyclerView imageRecyclerView;
    private List<String> imageUrls;
    private Adapter1 adapter;

    private TextView detailNumero, detailTitre, detailDescription, detailDate, detailTime;
    private FloatingActionButton deleteButton, editButton;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String key4 = "";
    String imageUrl = "";

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

        // Récupérer les données de l'Intent à l'aide d'un Bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailNumero.setText(bundle.getString("numeroEvent"));
            detailTitre.setText(bundle.getString("titreEvent"));
            detailDescription.setText(bundle.getString("descriptionEvent"));
            detailDate.setText(bundle.getString("dateEvent"));
            detailTime.setText(bundle.getString("heureEvent"));
            imageUrls = bundle.getStringArrayList("imagesEvent");
            key4 = bundle.getString("key4");
          //  imageUrl = bundle.getString("Image");
        }

        // Configuration du RecyclerView pour les images
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        imageRecyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter1(this, imageUrls, imageUrl -> {
            // Ouvrir l'image en plein écran
            Intent intent = new Intent(DetailActivity4.this, FullScreenImageActivity.class);
            intent.putExtra("key4", key4);
            intent.putExtra("imageUrl", imageUrl);
            startActivity(intent);
        });
        imageRecyclerView.setAdapter(adapter);

        // Action de suppression
        deleteButton.setOnClickListener(v -> deleteEvent());

        // Action de modification
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity4.this, updateEvent.class);
            intent.putExtra("numeroEvent", detailNumero.getText().toString());
            intent.putExtra("titreEvent", detailTitre.getText().toString());
            intent.putExtra("dateEvent", detailDate.getText().toString());
            intent.putExtra("heureEvent", detailTime.getText().toString());
            intent.putExtra("descriptionEvent", detailDescription.getText().toString());
            intent.putStringArrayListExtra("imagesEvent", (ArrayList<String>) imageUrls);
            intent.putExtra("key4", key4);
            startActivity(intent);
        });
    }

    // Méthode pour supprimer l'événement
    private void deleteEvent() {
        // Vérifier si l'événement existe avant de tenter de le supprimer
        databaseReference.child(key4).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Récupérer les URLs des images avant de supprimer l'événement
                    List<String> imageUrls = (List<String>) dataSnapshot.child("imagesEvent").getValue();

                    // Supprimer l'événement de la Realtime Database
                    databaseReference.child(key4).removeValue()
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

    // Méthode pour supprimer les images associées à l'événement
    private void deleteEventImages(List<String> imageUrls) {
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