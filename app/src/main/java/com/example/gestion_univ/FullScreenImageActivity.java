package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FullScreenImageActivity extends AppCompatActivity {

    // Déclaration des éléments
    private ImageView fullImageView;
    private ImageView btnEdit;
    private ImageView btnDelete;
    private  ImageView btnSaveImgEvent;
    ImageButton buttonRetourEvent;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri; // Pour stocker l'URI de l'image sélectionnée
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String imageUrl; // L'URL de l'image dans Firebase Storage
    private String key4; // Clé de l'événement dans Realtime Database

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        // Initialiser les éléments du layout
        fullImageView = findViewById(R.id.fullImageView);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnSaveImgEvent = findViewById(R.id.btnSaveImgEvent);
        buttonRetourEvent = findViewById(R.id.BackTeach);

        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");

        // Récupérer les données de l'Intent
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        key4 = intent.getStringExtra("key4");

        // Charger l'image existante
        Glide.with(this).load(imageUrl).into(fullImageView);

        // Charger l'image en plein écran avec Glide
        Glide.with(this).load(imageUrl).into(fullImageView);

        // Ajouter les actions pour les boutons edit et delete
        btnEdit.setOnClickListener(v -> {
            // Logique pour modifier l'image
            openFileChooser();
        });

        btnDelete.setOnClickListener(v -> {
            // Logique pour supprimer l'image
            // Par exemple : afficher un dialogue de confirmation et supprimer l'image
        });
        btnSaveImgEvent.setOnClickListener(v -> {
            uploadImageToFirebase();
        });
        // Back button action
        buttonRetourEvent.setOnClickListener(v -> {
            finish();
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Afficher l'image sélectionnée dans l'ImageView
            fullImageView.setImageURI(imageUri);
        }
    }
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newImageUrl = uri.toString();
                        // Mettre à jour l'URL dans Firebase Realtime Database
                        updateImageUrlInDatabase(newImageUrl);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(FullScreenImageActivity.this, "Échec de l'upload", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateImageUrlInDatabase(String newImageUrl) {
        // Récupère les images de l'événement dans la base de données
        databaseReference.child(key4).child("imagesEvent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> imageUrls = (List<String>) dataSnapshot.getValue();
                if (imageUrls != null) {
                    // Trouver l'image à modifier
                    int index = imageUrls.indexOf(imageUrl);
                    if (index != -1) {
                        imageUrls.set(index, newImageUrl); // Remplacer l'ancienne URL par la nouvelle
                        databaseReference.child(key4).child("imagesEvent").setValue(imageUrls)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(FullScreenImageActivity.this, "Image mise à jour avec succès", Toast.LENGTH_SHORT).show();
                                    finish(); // Retour à l'activité précédente
                                })
                                .addOnFailureListener(e -> Toast.makeText(FullScreenImageActivity.this, "Erreur lors de la mise à jour de l'image", Toast.LENGTH_SHORT).show());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FullScreenImageActivity.this, "Erreur lors de la récupération des images", Toast.LENGTH_SHORT).show();
            }
        });
    }
}