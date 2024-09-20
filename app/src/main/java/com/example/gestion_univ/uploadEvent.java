package com.example.gestion_univ;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class uploadEvent extends AppCompatActivity {

    private EditText txtNumeroEvent, txtTitreEvent, txtDateEvent, txtHeureEvent, txtDescriptionEvent;
    private Button btnSaveEvent;
    private GridLayout gridLayoutImages;
    private List<Uri> imageUris = new ArrayList<>();
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    // Sélection d'images
    ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        if (count > 10) {
                            Toast.makeText(uploadEvent.this, "Maximum de 10 images", Toast.LENGTH_SHORT).show();
                        } else {
                            imageUris.clear();
                            for (int i = 0; i < count; i++) {
                                imageUris.add(data.getClipData().getItemAt(i).getUri());
                            }
                            displaySelectedImages();
                        }
                    } else if (data.getData() != null) {
                        imageUris.clear();
                        imageUris.add(data.getData());
                        displaySelectedImages();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        // Initialisation des vues
        txtNumeroEvent = findViewById(R.id.txtNumeroEvent);
        txtTitreEvent = findViewById(R.id.txtTitreEvent);
       // txtDateEvent = findViewById(R.id.txtDateEvent);
      //  txtHeureEvent = findViewById(R.id.txtHeureEvent);
        txtDescriptionEvent = findViewById(R.id.txtDescriptionEvent);
        gridLayoutImages = findViewById(R.id.imageGrid);
        btnSaveEvent = findViewById(R.id.bntSaveEvent);

        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement");
        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");

        // Ouvrir la galerie pour sélectionner des images
        gridLayoutImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            imagePickerLauncher.launch(Intent.createChooser(intent, "Sélectionner des images"));
        });

        // Enregistrer l'évènement
        btnSaveEvent.setOnClickListener(v -> saveEventData());

        // Désactiver les pickers manuels car nous allons les remplir automatiquement
        // txtDateEvent.setOnClickListener(v -> {...});
        // txtHeureEvent.setOnClickListener(v -> {...});
    }

    // Afficher les images sélectionnées dans GridLayout
    private void displaySelectedImages() {
        gridLayoutImages.removeAllViews(); // Efface les images précédentes
        int numColumns = 3;
        gridLayoutImages.setColumnCount(numColumns);
        gridLayoutImages.post(() -> {
            int totalWidth = gridLayoutImages.getWidth();
            int imageSize = totalWidth / numColumns;
            for (Uri imageUri : imageUris) {
                ImageView imageView = new ImageView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = imageSize;
                params.height = imageSize;
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(imageUri).into(imageView);
                gridLayoutImages.addView(imageView);
            }
        });
    }

    private void saveEventData() {
        String numeroEvent = txtNumeroEvent.getText().toString();
        String titreEvent = txtTitreEvent.getText().toString();
        String descriptionEvent = txtDescriptionEvent.getText().toString();

        // Validation des champs
        if (numeroEvent.isEmpty() || titreEvent.isEmpty() || descriptionEvent.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner des images", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ajout de la date et de l'heure automatiques
        Calendar currentDateTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        String dateEvent = dateFormat.format(currentDateTime.getTime());
        String timeEvent = timeFormat.format(currentDateTime.getTime());

        // Vérification du doublon pour numeroEvent
        databaseReference.orderByChild("numeroEvent").equalTo(numeroEvent).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Toast.makeText(uploadEvent.this, "Numéro d'événement déjà existant", Toast.LENGTH_SHORT).show();
            } else {
                uploadImagesAndSaveEvent(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(uploadEvent.this, "Erreur lors de la vérification du numéro d'événement", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImagesAndSaveEvent(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent) {
        List<String> uploadedImages = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    uploadedImages.add(uri.toString());
                    if (uploadedImages.size() == imageUris.size()) {
                        saveEventToDatabase(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent, uploadedImages);
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(uploadEvent.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveEventToDatabase(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent, List<String> imagesEvent) {
        DataClass4 event = new DataClass4(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent, imagesEvent);
        databaseReference.push().setValue(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(uploadEvent.this, "Évènement enregistré avec succès", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(uploadEvent.this, "Échec de l'enregistrement de l'évènement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}