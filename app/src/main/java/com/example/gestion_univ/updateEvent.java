package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class updateEvent extends AppCompatActivity {

    Button btnUpdateEvent;
    EditText UpdateNumeroEvent, UpdateTitreEvent, UpdateDescriptionEvent;
    String key4, oldNumeroEvent, oldTitreEvent,oldDateEvent, oldHeureEvent,oldDescriptionEvent;
    DatabaseReference databaseReference;
    private List<String> oldImageURLS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        btnUpdateEvent = findViewById(R.id.bntSaveEvent);
        UpdateNumeroEvent = findViewById(R.id.UpdateNumeroEvent);
        UpdateTitreEvent = findViewById(R.id.UpdateTitreEvent);
        UpdateDescriptionEvent = findViewById(R.id.UpdateDescriptionEvent);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldNumeroEvent = bundle.getString("numeroEvent");
            oldTitreEvent = bundle.getString("titreEvent");
            oldDateEvent = bundle.getString("dateEvent");
            oldHeureEvent = bundle.getString("heureEvent");
            oldDescriptionEvent = bundle.getString("descriptionEvent");


            UpdateNumeroEvent.setText(oldNumeroEvent);
            UpdateTitreEvent.setText(oldTitreEvent);
            UpdateDescriptionEvent.setText(oldDescriptionEvent);


            key4 = bundle.getString("key4");
            oldImageURLS = bundle.getStringArrayList("imagesEvent");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Evenement").child(key4);

        btnUpdateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    public void updateData() {
        String numeroEvent = UpdateNumeroEvent.getText().toString().trim();
        String titreEvent = UpdateTitreEvent.getText().toString().trim();
        String descriptionEvent = UpdateDescriptionEvent.getText().toString().trim();

        // Vérification des champs vides
        if (numeroEvent.isEmpty() || titreEvent.isEmpty() || descriptionEvent.isEmpty()) {
            Toast.makeText(updateEvent.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification des modifications
        boolean isModified = !numeroEvent.equals(oldNumeroEvent) || !titreEvent.equals(oldTitreEvent) || !descriptionEvent.equals(oldDescriptionEvent);

        if (!isModified) {
            Toast.makeText(updateEvent.this, "Aucune modification effectuée", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Vérification et mise à jour des données
        if (!numeroEvent.equals(oldNumeroEvent)) {
            checkIfNumeroEventExistsAndUpdateData(numeroEvent, titreEvent, descriptionEvent);
        } else {
            updateDataInFirebase(numeroEvent, titreEvent, descriptionEvent);
        }
    }

    private void checkIfNumeroEventExistsAndUpdateData(final String numeroEvent, final String titreEvent, final String descriptionEvent) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Evenement");
        ref.orderByChild("numeroEvent").equalTo(numeroEvent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(updateEvent.this, "Le numéro d'événement existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    updateDataInFirebase(numeroEvent, titreEvent, descriptionEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(updateEvent.this, "Erreur de vérification de l'événement", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataInFirebase(String numeroEvent, String titreEvent, String descriptionEvent) {
        DataClass4 dataClass4 = new DataClass4(numeroEvent, titreEvent, oldDateEvent, oldHeureEvent, descriptionEvent, oldImageURLS);

        databaseReference.setValue(dataClass4).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(updateEvent.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updateEvent.this, Evenement.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(updateEvent.this, "Échec de la mise à jour des données.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateEvent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, DetailActivity4.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}