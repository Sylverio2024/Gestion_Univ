package com.example.gestion_univ;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_images extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;

    ImageView btnCamS, btnGalS, imgChooseS;
    TextView btnsaveS;
    Uri imageUri;  // URI for the selected image
    private String key4; // Key of the event from the intent
    private StorageReference storageReference; // Firebase Storage reference
    private DatabaseReference databaseReference; // Firebase Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);

        // Initialize Firebase references
        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");
        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");

        // Retrieve event key from intent
        key4 = getIntent().getStringExtra("key4");

        btnCamS = findViewById(R.id.btnCamS);
        btnGalS = findViewById(R.id.btnGalS);
        imgChooseS = findViewById(R.id.imgChooseS);
        btnsaveS = findViewById(R.id.btnSaveS);

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
                                   Toast.makeText(add_images.this, "Permissions are required to use the camera", Toast.LENGTH_SHORT).show();
                               }
                           }

                           @Override
                           public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                               permissionToken.continuePermissionRequest();
                           }
                       }).check();
           }
       });


        // Handle Save button click (upload image)
        btnsaveS.setOnClickListener(v -> uploadImageToFirebase());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                imageUri = data.getData();
                if (imageUri != null) {
                    Glide.with(this).load(imageUri).into(imgChooseS);
                } else {
                    Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2 && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    imageUri = getImageUri(bitmap);
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(imgChooseS);
                    } else {
                        Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Upload image to Firebase Storage and save URL in Firebase Realtime Database with ProgressDialog
    private void uploadImageToFirebase() {
        // Créer un ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);  // Empêche de fermer le dialog tant que l'opération n'est pas terminée
        progressDialog.show();  // Afficher le dialog

        if (imageUri != null) {
            // Créer un nom unique pour l'image
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Récupérer l'URL de téléchargement de l'image
                        String downloadUrl = uri.toString();

                        // Vérifier que la clé de l'événement est correcte
                        if (key4 != null && !key4.isEmpty()) {
                            // Récupérer la liste actuelle des URLs d'images sous la clé "imagesEvent" de l'événement existant
                            databaseReference.child(key4).child("imagesEvent").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    List<String> imageList = new ArrayList<>();

                                    // Si la clé existe, récupérer la liste actuelle des images
                                    if (snapshot.exists()) {
                                        for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                                            String existingUrl = imageSnapshot.getValue(String.class);
                                            if (existingUrl != null) {
                                                imageList.add(existingUrl);
                                            }
                                        }
                                    }

                                    // Ajouter la nouvelle URL d'image à la liste
                                    imageList.add(downloadUrl);

                                    // Mettre à jour uniquement le champ imagesEvent de l'événement existant
                                    databaseReference.child(key4).child("imagesEvent").setValue(imageList)
                                            .addOnSuccessListener(aVoid -> {
                                                progressDialog.dismiss();  // Fermer le dialog une fois l'opération terminée
                                                Toast.makeText(add_images.this, "Image uploaded and added to the event successfully", Toast.LENGTH_SHORT).show();

                                                // Revenir à la fenêtre précédente
                                                Intent intent = new Intent(add_images.this, Evenement.class);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();  // Fermer le dialog en cas d'échec
                                                Toast.makeText(add_images.this, "Failed to update the event with the new image URL", Toast.LENGTH_SHORT).show();
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();  // Fermer le dialog en cas d'échec
                                    Toast.makeText(add_images.this, "Failed to retrieve existing images", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressDialog.dismiss();  // Fermer le dialog en cas d'échec
                            Toast.makeText(add_images.this, "Invalid event key", Toast.LENGTH_SHORT).show();
                        }
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();  // Fermer le dialog en cas d'échec
                        Toast.makeText(add_images.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            progressDialog.dismiss();  // Fermer le dialog si aucune image n'est sélectionnée
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Captured Image", null);
        if (path != null) {
            return Uri.parse(path);
        } else {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
            return null; // Handle case where image insertion fails
        }
    }
}