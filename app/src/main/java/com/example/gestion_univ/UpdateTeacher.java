package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateTeacher extends AppCompatActivity {

    Button bntUpdateT;
    EditText updateNumeroID, updateNameT, updatePrenomT, updateSpecialiteT, updateAdresseT, updateCategorieT, updateTelephoneT;
    String key, oldImageURL, oldNumeroID, oldName, oldPrenom, oldSpecialite, oldAdresse, oldCategorie, oldTelephone;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_teacher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bntUpdateT = findViewById(R.id.bntUpdateT);
        updateNumeroID = findViewById(R.id.updateNumeroID);
        updateNameT = findViewById(R.id.updateNameT);
        updatePrenomT = findViewById(R.id.updatePrenomT);
        updateSpecialiteT = findViewById(R.id.updateSpecialiteT);
        updateAdresseT = findViewById(R.id.updateAdresseT);
        updateCategorieT = findViewById(R.id.updateCategorieT);
        updateTelephoneT = findViewById(R.id.updateTelephoneT);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldNumeroID = bundle.getString("NumeroID");
            oldName = bundle.getString("Nom");
            oldPrenom = bundle.getString("Prenom");
            oldSpecialite = bundle.getString("Specialite");
            oldAdresse = bundle.getString("Adresse");
            oldCategorie = bundle.getString("Categorie");
            oldTelephone = bundle.getString("Telephone");

            updateNumeroID.setText(oldNumeroID);
            updateNameT.setText(oldName);
            updatePrenomT.setText(oldPrenom);
            updateSpecialiteT.setText(oldSpecialite);
            updateAdresseT.setText(oldAdresse);
            updateCategorieT.setText(oldCategorie);
            updateTelephoneT.setText(oldTelephone);

            key = bundle.getString("key");
            oldImageURL = bundle.getString("Image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Enseignants").child(key);

        bntUpdateT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataWithoutImage();
            }
        });
    }

    public void updateDataWithoutImage() {
        String numeroIDT = updateNumeroID.getText().toString().trim();
        String nameT = updateNameT.getText().toString().trim();
        String prenomT = updatePrenomT.getText().toString().trim();
        String specialiteT = updateSpecialiteT.getText().toString().trim();
        String adresseT = updateAdresseT.getText().toString().trim();
        String categorieT = updateCategorieT.getText().toString().trim();
        String telephoneT = updateTelephoneT.getText().toString().trim();

        // Vérification des champs vides
        if (numeroIDT.isEmpty() || nameT.isEmpty() || prenomT.isEmpty() || specialiteT.isEmpty() || adresseT.isEmpty() || categorieT.isEmpty() || telephoneT.isEmpty()) {
            Toast.makeText(UpdateTeacher.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification du numéro de téléphone
        if (!Patterns.PHONE.matcher(telephoneT).matches()) {
            updateTelephoneT.setError("Entrez un bon numéro");
            return;
        }

        // Vérification des modifications
        boolean isModified = !numeroIDT.equals(oldNumeroID) || !nameT.equals(oldName) || !prenomT.equals(oldPrenom)
                || !specialiteT.equals(oldSpecialite) || !adresseT.equals(oldAdresse) || !categorieT.equals(oldCategorie)
                || !telephoneT.equals(oldTelephone);

        if (!isModified) {
            // Aucun changement, retour à l'écran précédent
            Toast.makeText(UpdateTeacher.this, "Aucune modification effectuée", Toast.LENGTH_SHORT).show();
            finish(); // Retour à l'écran précédent
            return;
        }

        // Vérification et mise à jour des données
        if (!numeroIDT.equals(oldNumeroID)) {
            checkIfNumeroIDExistsAndUpdateData(numeroIDT, nameT, prenomT, specialiteT, adresseT, categorieT, telephoneT);
        } else {
            updateDataInFirebase(numeroIDT, nameT, prenomT, specialiteT, adresseT, categorieT, telephoneT);
        }
    }

    private void checkIfNumeroIDExistsAndUpdateData(final String numeroIDT, final String nameT, final String prenomT, final String specialiteT, final String adresseT, final String categorieT, final String telephoneT) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Enseignants");
        ref.orderByChild("numeroIDT").equalTo(numeroIDT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(UpdateTeacher.this, "Le numéro ID existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    updateDataInFirebase(numeroIDT, nameT, prenomT, specialiteT, adresseT, categorieT, telephoneT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateTeacher.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataInFirebase(String numeroIDT, String nameT, String prenomT, String specialiteT, String adresseT, String categorieT, String telephoneT) {
        DataClass dataClass = new DataClass(numeroIDT, nameT, prenomT, specialiteT, adresseT, categorieT, telephoneT, oldImageURL);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateTeacher.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateTeacher.this, fnEnseignant.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateTeacher.this, "Échec de la mise à jour des données.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}