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

public class updateSalle extends AppCompatActivity {
    Button bntUpdateSalle;
    EditText updateNumeroIDSalle, updateNameSalle, updateStatutSalle, updateDescriptionSalle;
    String key3, oldNumeroIDSalle, oldNameSalle, oldStatutSalle, oldDescriptionSalle;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_salle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bntUpdateSalle = findViewById(R.id.bntUpdateSalle);
        updateNumeroIDSalle = findViewById(R.id.updateNumeroIDSalle);
        updateNameSalle = findViewById(R.id.updateNameSalle);
        updateStatutSalle = findViewById(R.id.updateStatutSalle);
        updateDescriptionSalle = findViewById(R.id.updateDescriptionSalle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            oldNumeroIDSalle = bundle.getString("NumeroSalle");
            oldNameSalle = bundle.getString("NomSalle");
            oldStatutSalle = bundle.getString("StatutSalle");
            oldDescriptionSalle = bundle.getString("DescriptionSalle");

            updateNumeroIDSalle.setText(oldNumeroIDSalle);
            updateNameSalle.setText(oldNameSalle);
            updateStatutSalle.setText(oldStatutSalle);
            updateDescriptionSalle.setText(oldDescriptionSalle);
            key3 = bundle.getString("key3");
            databaseReference = FirebaseDatabase.getInstance().getReference("Salle").child(key3);

            bntUpdateSalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDataWithoutImage();
                }
            });
        }
    }
    public void updateDataWithoutImage() {
        String numeroSalle = updateNumeroIDSalle.getText().toString().trim();
        String nameSalle = updateNameSalle.getText().toString().trim();
        String statutSalle = updateStatutSalle.getText().toString().trim();
        String descriptionSalle = updateDescriptionSalle.getText().toString().trim();

        // Vérification des champs vides
        if (numeroSalle.isEmpty() || nameSalle.isEmpty() || statutSalle.isEmpty() || descriptionSalle.isEmpty()) {
            Toast.makeText(updateSalle.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérification des modifications
        boolean isModified = !numeroSalle.equals(oldNumeroIDSalle) || !nameSalle.equals(oldNameSalle) || !statutSalle.equals(oldStatutSalle)
                || !descriptionSalle.equals(oldDescriptionSalle);

        if (!isModified) {
            // Aucun changement, retour à l'écran précédent
            Toast.makeText(updateSalle.this, "Aucune modification effectuée", Toast.LENGTH_SHORT).show();
            finish(); // Retour à l'écran précédent
            return;
        }

        // Vérification et mise à jour des données
        if (!numeroSalle.equals(oldNumeroIDSalle)) {
            checkIfNumeroIDExistsAndUpdateData(numeroSalle, nameSalle,descriptionSalle,statutSalle);
        } else {
            updateDataInFirebase(numeroSalle, nameSalle,descriptionSalle,statutSalle);
        }
    }

    private void checkIfNumeroIDExistsAndUpdateData(final String numeroSalle, final String nameSalle, final String descriptionSalle, final String statutSalle) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Salle");
        ref.orderByChild("numeroSalle").equalTo(numeroSalle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(updateSalle.this, "Le numéro ID existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    updateDataInFirebase(numeroSalle,nameSalle,descriptionSalle,statutSalle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(updateSalle.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataInFirebase(String numeroSalle, String nameSalle, String descriptionSalle, String statutSalle) {
        DataClass3 dataClass3 = new DataClass3(numeroSalle, nameSalle,descriptionSalle,statutSalle);

        databaseReference.setValue(dataClass3).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(updateSalle.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updateSalle.this, Salle.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(updateSalle.this, "Échec de la mise à jour des données.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateSalle.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, detailActivity3.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}