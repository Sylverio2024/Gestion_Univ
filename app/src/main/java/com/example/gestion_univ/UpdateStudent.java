package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
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

public class UpdateStudent extends AppCompatActivity {
    Button bntUpdateS;
    EditText updateNumeroID1, updateNum_inscriptionS, updateNomS, updatePrenomS, updateMentionS, updateParcoursS, updateNiveauS, updateDate_naissanceS, updateAdresseS, updateTelephoneS;
    String key1, oldImageURL1, oldNumeroID1, oldNum_inscriptionS, oldNomS, oldPrenomS, oldMentionS, oldParcoursS, oldNiveauS, oldDate_naissanceS, oldAdresseS, oldteTelephoneS;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bntUpdateS = findViewById(R.id.bntUpdateS);
        updateNumeroID1 = findViewById(R.id.updateNumeroID1);
        updateNum_inscriptionS = findViewById(R.id.updateNum_inscriptionS);
        updateNomS = findViewById(R.id.updateNomS);
        updatePrenomS = findViewById(R.id.updatePrenomS);
        updateMentionS = findViewById(R.id.updateMentionS);
        updateParcoursS = findViewById(R.id.updateParcoursS);
        updateNiveauS = findViewById(R.id.updateNiveauS);
        updateDate_naissanceS = findViewById(R.id.updateDate_naissanceS);
        updateAdresseS = findViewById(R.id.updateAdresseS);
        updateTelephoneS = findViewById(R.id.updateTelephoneS);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldNumeroID1 = bundle.getString("NumeroIDE");
            oldNum_inscriptionS = bundle.getString("Num_inscriptionE");
            oldNomS = bundle.getString("NomE");
            oldPrenomS = bundle.getString("PrenomE");
            oldMentionS = bundle.getString("mentionE");
            oldParcoursS = bundle.getString("parcoursE");
            oldNiveauS = bundle.getString("niveauE");
            oldDate_naissanceS = bundle.getString("dateE");
            oldAdresseS = bundle.getString("adresseE");
            oldteTelephoneS = bundle.getString("telephoneE");

            updateNumeroID1.setText(oldNumeroID1);
            updateNum_inscriptionS.setText(oldNum_inscriptionS);
            updateNomS.setText(oldNomS);
            updatePrenomS.setText(oldPrenomS);
            updateMentionS.setText(oldMentionS);
            updateParcoursS.setText(oldParcoursS);
            updateNiveauS.setText(oldNiveauS);
            updateDate_naissanceS.setText(oldDate_naissanceS);
            updateAdresseS.setText(oldAdresseS);
            updateTelephoneS.setText(oldteTelephoneS);

            key1 = bundle.getString("key1");
            oldImageURL1 = bundle.getString("ImageE");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Etudiants").child(key1);

        bntUpdateS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataWithoutImage();
            }
        });
    }

    public void updateDataWithoutImage() {
        String numeroIDE = updateNumeroID1.getText().toString().trim();
        String numInscriptionE = updateNum_inscriptionS.getText().toString().trim();
        String nameE = updateNomS.getText().toString().trim();
        String prenomE = updatePrenomS.getText().toString().trim();
        String dateNaissanceE = updateMentionS.getText().toString().trim();
        String adresseE = updateParcoursS.getText().toString().trim();
        String mentionE = updateNiveauS.getText().toString().trim();
        String parcoursE = updateDate_naissanceS.getText().toString().trim();
        String niveauE = updateAdresseS.getText().toString().trim();
        String telephoneE = updateTelephoneS.getText().toString().trim();

        // Vérification des champs vides
        if (numeroIDE.isEmpty() || numInscriptionE.isEmpty() || nameE.isEmpty() || prenomE.isEmpty() || dateNaissanceE.isEmpty() || adresseE.isEmpty() || mentionE.isEmpty() || parcoursE.isEmpty() || niveauE.isEmpty() || telephoneE.isEmpty()) {
            Toast.makeText(UpdateStudent.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification du numéro de téléphone
        if (!Patterns.PHONE.matcher(telephoneE).matches()) {
            updateTelephoneS.setError("Entrez un bon numéro");
            return;
        }

        // Vérification des modifications
        boolean isModified = !numeroIDE.equals(oldNumeroID1) || !numInscriptionE.equals(oldNum_inscriptionS) || !nameE.equals(oldNomS)
                || !prenomE.equals(oldPrenomS) || !dateNaissanceE.equals(oldDate_naissanceS) || !adresseE.equals(oldAdresseS)
                || !mentionE.equals(oldMentionS) || !parcoursE.equals(oldParcoursS) || !niveauE.equals(oldNiveauS)
                || !telephoneE.equals(oldteTelephoneS);

        if (!isModified) {
            // Aucun changement, retour à l'écran précédent
            Toast.makeText(UpdateStudent.this, "Aucune modification effectuée", Toast.LENGTH_SHORT).show();
            finish(); // Retour à l'écran précédent
            return;
        }

        // Vérification de l'existence des champs avant mise à jour
        if (!numeroIDE.equals(oldNumeroID1)) {
            checkIfNumeroIDExistsAndUpdateData(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE);
        } else if (!numInscriptionE.equals(oldNum_inscriptionS)) {
            checkIfNumInscriptionExistsAndUpdateData(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE);
        } else {
            updateDataInFirebase(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE);
        }
    }

    private void checkIfNumeroIDExistsAndUpdateData(final String numeroIDE, final String numInscriptionE, final String nameE, final String prenomE, final String mentionE, final String parcoursE, final String niveauE, final String dateNaissanceE, final String adresseE, final String telephoneE) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Etudiants");
        ref.orderByChild("numeroIDE").equalTo(numeroIDE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(UpdateStudent.this, "Le numéro ID existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    checkIfNumInscriptionExistsAndUpdateData(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateStudent.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfNumInscriptionExistsAndUpdateData(final String numeroIDE, final String numInscriptionE, final String nameE, final String prenomE, final String mentionE, final String parcoursE, final String niveauE, final String dateNaissanceE, final String adresseE, final String telephoneE) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Etudiants");
        ref.orderByChild("numInscriptionE").equalTo(numInscriptionE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(UpdateStudent.this, "Le numéro d'inscription existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    updateDataInFirebase(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateStudent.this, "Erreur de vérification du numéro d'inscription", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataInFirebase(String numeroIDE, String numInscriptionE, String nameE, String prenomE, String mentionE, String parcoursE, String niveauE, String dateNaissanceE, String adresseE, String telephoneE) {
        DataClass1 dataClass1 = new DataClass1(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE, niveauE, dateNaissanceE, adresseE, telephoneE, oldImageURL1);
        databaseReference.setValue(dataClass1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateStudent.this, "Modification effectuée avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateStudent.this, fnEtudiant.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(UpdateStudent.this, "Echec de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateStudent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}