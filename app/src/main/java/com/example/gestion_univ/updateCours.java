package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
public class updateCours extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOIX_SALLE1 = 1;
    private ScrollView scrollView21;
    String key2, oldupdateNumeroCours, oldupdateNameCours, oldupdateSalleCours, oldupdateParcoursCours, oldupdateNiveauCours, oldupdateDescriptionCours;
    Button saveButton21;
    DatabaseReference databaseReference, salleReference;
    EditText updateNumeroCours, updateNameCours, updateSalleCours, updateParcoursCours, updateNiveauCours, updateDescriptionCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_cours);

        saveButton21 = findViewById(R.id.bntupdateCours);
        updateNumeroCours = findViewById(R.id.updateNumeroCours);
        updateNameCours = findViewById(R.id.updateNameCours);
        updateSalleCours = findViewById(R.id.updateSalleCours);
        updateParcoursCours = findViewById(R.id.updateParcoursCours);
        updateNiveauCours = findViewById(R.id.updateNiveauCours);
        updateDescriptionCours = findViewById(R.id.updateDescriptionCours);
        scrollView21 = findViewById(R.id.scrollView21);

        // Récupération des données depuis l'intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldupdateNumeroCours = bundle.getString("numeroCours");
            oldupdateNameCours = bundle.getString("nameCours");
            oldupdateSalleCours = bundle.getString("salleCours");
            oldupdateParcoursCours = bundle.getString("parcoursCours");
            oldupdateNiveauCours = bundle.getString("niveauCours");
            oldupdateDescriptionCours = bundle.getString("descriptionCours");

            updateNumeroCours.setText(oldupdateNumeroCours);
            updateNameCours.setText(oldupdateNameCours);
            updateSalleCours.setText(oldupdateSalleCours);
            updateParcoursCours.setText(oldupdateParcoursCours);
            updateNiveauCours.setText(oldupdateNiveauCours);
            updateDescriptionCours.setText(oldupdateDescriptionCours);
            key2 = bundle.getString("key2");
            databaseReference = FirebaseDatabase.getInstance().getReference("Cours").child(key2);

            saveButton21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCoursInFirebase();
                }
            });

            updateSalleCours.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (updateSalleCours.getRight() - updateSalleCours.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // Ouvrir l'activité choixSalle ici
                            Intent intent = new Intent(updateCours.this, choixSalle1.class);
                            startActivityForResult(intent, REQUEST_CODE_CHOIX_SALLE1);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOIX_SALLE1 && resultCode == RESULT_OK && data != null) {
            String selectedSalle = data.getStringExtra("salle_nom");
            updateSalleCours.setText(selectedSalle);
        }
    }

    private void updateCoursInFirebase() {
        final String numeroCours = updateNumeroCours.getText().toString().trim();
        final String nameCours = updateNameCours.getText().toString().trim();
        final String salleCours = updateSalleCours.getText().toString().trim();
        final String parcoursCours = updateParcoursCours.getText().toString().trim();
        final String niveauCours = updateNiveauCours.getText().toString().trim();
        final String descriptionCours = updateDescriptionCours.getText().toString().trim();

        if (numeroCours.isEmpty() || nameCours.isEmpty() || salleCours.isEmpty() ||
                parcoursCours.isEmpty() || niveauCours.isEmpty() || descriptionCours.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification si la salle de cours a été modifiée
        if (!salleCours.equals(oldupdateSalleCours)) {
            // Marquer l'ancienne salle comme disponible
            updateSalleAvailability(oldupdateSalleCours, "Disponible");

            // Marquer la nouvelle salle comme utilisée
            updateSalleAvailability(salleCours, "Utilisé");
        }

        // Mise à jour du cours dans Firebase
        databaseReference.child("numeroCours").setValue(numeroCours);
        databaseReference.child("nameCours").setValue(nameCours);
        databaseReference.child("salleCours").setValue(salleCours);
        databaseReference.child("parcoursCours").setValue(parcoursCours);
        databaseReference.child("niveauCours").setValue(niveauCours);
        databaseReference.child("descriptionCours").setValue(descriptionCours)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(updateCours.this, "Mise à jour réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(updateCours.this, Cours.class);
                            startActivity(intent);
                            finish(); // Retour à l'écran précédent
                        } else {
                            Toast.makeText(updateCours.this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(updateCours.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateSalleAvailability(String salleNom, String status) {
        // Référence à la base de données pour la salle
        salleReference = FirebaseDatabase.getInstance().getReference("Salle");

        salleReference.orderByChild("nameSalle").equalTo(salleNom).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot salleSnapshot : dataSnapshot.getChildren()) {
                    // Mettre à jour le champ 'statut' de la salle correspondante
                    salleSnapshot.getRef().child("statutSalle").setValue(status);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(updateCours.this, "Erreur de mise à jour de la salle : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}