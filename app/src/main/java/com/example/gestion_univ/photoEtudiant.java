package com.example.gestion_univ;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class photoEtudiant extends AppCompatActivity {
    ImageView btnCamS, btnGalS, imgChooseS;
    ImageButton btnRetourPDP;
    TextView btnsaveS;
    Uri imageUri;
    AlertDialog loadingDialog;
    private String imageUrl; // L'URL de l'image dans Firebase Storage
    private String key; // Clé de l'événement dans Realtime Database

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo_etudiant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRetourPDP = findViewById(R.id.BackTeach);
        btnCamS = findViewById(R.id.btnCamS);
        btnGalS = findViewById(R.id.btnGalS);
        imgChooseS = findViewById(R.id.imgChooseS);
        btnsaveS = findViewById(R.id.btnSaveS);

        // Récupérer les données de l'Intent
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("ImageE");
        key = intent.getStringExtra("key1");

        // Charger l'image existante
        //  Glide.with(this).load(imageUrl).into(fullImageView);

        // Charger l'image en plein écran avec Glide
        Glide.with(this).load(imageUrl).into(imgChooseS);
        // Initialiser le dialog de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(photoEtudiant.this);
        builder.setCancelable(false); // Empêche de fermer le dialog en appuyant à l'extérieur
        builder.setView(R.layout.loading_dialog); // Utiliser un layout personnalisé pour le dialog
        loadingDialog = builder.create();

        btnRetourPDP.setOnClickListener(v -> finish());

        btnsaveS.setOnClickListener(v -> {
            if (imageUri != null) {
                loadingDialog.show(); // Afficher le dialog de chargement ici
                // Récupérer l'ancienne URL de l'image de Firebase pour la supprimer
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Etudiants").child(key);
                dbRef.child("imageE").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String oldImageUrl = snapshot.getValue(String.class);

                        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                            // Référence à l'ancienne image dans Firebase Storage
                            StorageReference oldImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);

                            // Supprimer l'ancienne image
                            oldImageRef.delete().addOnSuccessListener(aVoid -> {
                                // L'ancienne image a été supprimée avec succès, télécharger la nouvelle image
                                uploadNewImage(imageUri, key);
                            }).addOnFailureListener(e -> {
                                loadingDialog.dismiss(); // Cacher le dialog en cas d'échec
                                // En cas d'échec de la suppression, afficher un message d'erreur
                                Toast.makeText(photoEtudiant.this, "Échec de la suppression de l'ancienne image", Toast.LENGTH_SHORT).show();

                            });
                        } else {
                            // Aucune ancienne image à supprimer, télécharger directement la nouvelle
                            uploadNewImage(imageUri, key);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingDialog.dismiss(); // Cacher le dialog en cas d'échec
                        Toast.makeText(photoEtudiant.this, "Erreur de récupération de l'image", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(photoEtudiant.this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
            }
        });

        btnGalS.setOnClickListener(v -> Dexter.withContext(getApplicationContext()).withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), 1);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());

        btnCamS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    // Permissions granted, open the camera
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, 2);
                                } else {
                                    Toast.makeText(photoEtudiant.this, "Permissions are required to use the camera", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imgChooseS);
            } else if (requestCode == 2 && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageUri = getImageUri(bitmap);
                Glide.with(this).load(imageUri).into(imgChooseS);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);

    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, DetailActivity1.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
    // Méthode pour télécharger la nouvelle image
    private void uploadNewImage(Uri imageUri, String key) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Android Images/" + key);

        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Etudiants").child(key);
                dbRef.child("imageE").setValue(uri.toString()).addOnCompleteListener(task -> {
                    loadingDialog.dismiss(); // Cacher le dialog après le succès
                    if (task.isSuccessful()) {
                        Toast.makeText(photoEtudiant.this, "Image mise à jour avec succès", Toast.LENGTH_SHORT).show();
                        Intent fnEtudiantIntent = new Intent(photoEtudiant.this, fnEtudiant.class);
                        startActivity(fnEtudiantIntent);
                        finish();
                    } else {
                        Toast.makeText(photoEtudiant.this, "Échec de la mise à jour de l'image", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }).addOnFailureListener(e -> {
            loadingDialog.dismiss(); // Cacher le dialog en cas d'échec
            Toast.makeText(photoEtudiant.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
        });
    }
}