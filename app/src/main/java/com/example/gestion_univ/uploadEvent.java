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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
                        // Si une seule image est sélectionnée
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
        txtDateEvent = findViewById(R.id.txtDateEvent);
        txtHeureEvent = findViewById(R.id.txtHeureEvent);
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

        // Date picker
        txtDateEvent.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(uploadEvent.this,
                    (view, year1, monthOfYear, dayOfMonth) -> txtDateEvent.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        // Time picker
        txtHeureEvent.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(uploadEvent.this,
                    (view, hourOfDay, minute1) -> txtHeureEvent.setText(hourOfDay + ":" + minute1),
                    hour, minute, true);
            timePickerDialog.show();
        });
    }

    // Afficher les images sélectionnées dans GridLayout
    private void displaySelectedImages() {
        gridLayoutImages.removeAllViews(); // Clear previous images

        // Définir le nombre de colonnes souhaité
        int numColumns = 3;
        gridLayoutImages.setColumnCount(numColumns); // Assure que GridLayout a le bon nombre de colonnes

        // Obtenir la largeur du GridLayout pour calculer la taille des images
        gridLayoutImages.post(() -> {
            int totalWidth = gridLayoutImages.getWidth();
            int imageSize = totalWidth / numColumns; // Taille de l'image en fonction du nombre de colonnes

            // Parcourir les images sélectionnées
            for (Uri imageUri : imageUris) {
                ImageView imageView = new ImageView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = imageSize;
                params.height = imageSize;
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // Charger l'image avec Glide
                Glide.with(this).load(imageUri).into(imageView);
                gridLayoutImages.addView(imageView);
            }
        });
    }

    private void saveEventData() {
        String numeroEvent = txtNumeroEvent.getText().toString();
        String titreEvent = txtTitreEvent.getText().toString();
        String dateEvent = txtDateEvent.getText().toString();
        String timeEvent = txtHeureEvent.getText().toString();
        String descriptionEvent = txtDescriptionEvent.getText().toString(); // Ajout de la description

        // Validation des champs
        if (numeroEvent.isEmpty() || titreEvent.isEmpty() || dateEvent.isEmpty() || timeEvent.isEmpty() || descriptionEvent.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner des images", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification du doublon pour numeroEvent
        databaseReference.orderByChild("numeroEvent").equalTo(numeroEvent).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                // Si un évènement avec ce numéro existe déjà
                Toast.makeText(uploadEvent.this, "Numéro d'événement déjà existant", Toast.LENGTH_SHORT).show();
            } else {
                // Aucun doublon, on continue avec l'enregistrement
                uploadImagesAndSaveEvent(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(uploadEvent.this, "Erreur lors de la vérification du numéro d'événement", Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImagesAndSaveEvent(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent) {
        // Liste pour stocker les URL des images téléchargées
        List<String> uploadedImages = new ArrayList<>();

        // Télécharge les images une par une
        for (Uri imageUri : imageUris) {
            // Référence de stockage avec nom unique basé sur le timestamp
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");

            // Téléchargement du fichier vers Firebase Storage
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Récupérer l'URL de téléchargement une fois l'image téléchargée
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Ajouter l'URL à la liste des images téléchargées
                    uploadedImages.add(uri.toString());

                    // Lorsque toutes les images sont téléchargées, sauvegarder l'événement dans la base de données
                    if (uploadedImages.size() == imageUris.size()) {
                        saveEventToDatabase(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent, uploadedImages);
                    }
                });
            }).addOnFailureListener(e -> {
                // Gestion de l'erreur de téléchargement
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