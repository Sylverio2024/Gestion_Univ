package com.example.gestion_univ;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class uploadEvent extends AppCompatActivity {

    private EditText txtNumeroEvent, txtTitreEvent, txtDateEvent, txtHeureEvent, txtDescriptionEvent;
    private ImageView updloadImageEvent;
    private Button btnSaveEvent;

    private List<Uri> imageUris = new ArrayList<>();
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    // Sélection d'images
    ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            uris -> {
                if (uris.size() > 10) {
                    Toast.makeText(uploadEvent.this, "Maximum de 10 images", Toast.LENGTH_SHORT).show();
                } else {
                    imageUris = uris;
                    // Afficher la première image sélectionnée
                    if (!uris.isEmpty()) {
                        updloadImageEvent.setImageURI(uris.get(0));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        txtNumeroEvent = findViewById(R.id.txtNumeroEvent);
        txtTitreEvent = findViewById(R.id.txtTitreEvent);
        txtDateEvent = findViewById(R.id.txtDateEvent);
        txtHeureEvent = findViewById(R.id.txtHeureEvent);
        txtDescriptionEvent = findViewById(R.id.txtDescriptionEvent);
        updloadImageEvent = findViewById(R.id.updloadImageEvent);
        btnSaveEvent = findViewById(R.id.bntSaveEvent);

        databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        storageReference = FirebaseStorage.getInstance().getReference("EventsImages");

        updloadImageEvent.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnSaveEvent.setOnClickListener(v -> saveEventData());
        txtDateEvent.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(uploadEvent.this,
                    (view, year1, monthOfYear, dayOfMonth) -> txtDateEvent.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        txtHeureEvent.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(uploadEvent.this,
                    (view, hourOfDay, minute1) -> txtHeureEvent.setText(hourOfDay + ":" + minute1), hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void saveEventData() {
        String numeroEvent = txtNumeroEvent.getText().toString();
        String titreEvent = txtTitreEvent.getText().toString();
        String dateEvent = txtDateEvent.getText().toString();
        String timeEvent = txtHeureEvent.getText().toString();
        String descriptionEvent = txtDescriptionEvent.getText().toString();

        if (numeroEvent.isEmpty() || titreEvent.isEmpty() || dateEvent.isEmpty() || timeEvent.isEmpty() || descriptionEvent.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Veuillez sélectionner des images", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload des images et enregistrement de l'événement dans Firebase
        List<String> uploadedImages = new ArrayList<>();
        for (Uri imageUri : imageUris) {
            StorageReference imageRef = storageReference.child(System.currentTimeMillis() + ".jpg");
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    uploadedImages.add(uri.toString());
                    if (uploadedImages.size() == imageUris.size()) {
                        // Toutes les images ont été téléchargées
                        saveEventToDatabase(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent, uploadedImages);
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(uploadEvent.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show());
        }
    }

    private void saveEventToDatabase(String numeroEvent, String titreEvent, String dateEvent, String timeEvent, String descriptionEvent, List<String> images) {
        DataClass4 event = new DataClass4(numeroEvent, titreEvent, dateEvent, timeEvent, descriptionEvent, images.toString());
        String eventId = databaseReference.push().getKey();
        databaseReference.child(eventId).setValue(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(uploadEvent.this, "Événement enregistré avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(uploadEvent.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}