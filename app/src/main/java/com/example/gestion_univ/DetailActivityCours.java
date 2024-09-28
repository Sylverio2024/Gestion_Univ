package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivityCours extends AppCompatActivity {
    TextView detailNumeroCours, detailNomCours, detailSalleCours, detailParcoursCours, detailNiveauCours, detailDescriptionCours;
    TextView detailDate, detailHeure;  // Nouveaux TextViews pour la date et l'heure
    FloatingActionButton deleteButton, editButton;
    String key2 = "";
    AlertDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_cours);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        detailNumeroCours = findViewById(R.id.detailNumeroCours);
        detailNomCours = findViewById(R.id.detailNomCours);
        detailSalleCours = findViewById(R.id.detailSalleCours);
        detailParcoursCours = findViewById(R.id.detailParcoursCours);
        detailNiveauCours = findViewById(R.id.detailNiveauCours);
        detailDescriptionCours = findViewById(R.id.detailDescriptionCours);
        detailDate = findViewById(R.id.detailDate);  // Initialisation du TextView pour la date
        detailHeure = findViewById(R.id.detailHeure); // Initialisation du TextView pour l'heure

        deleteButton = findViewById(R.id.deleteButtonCours);
        editButton = findViewById(R.id.editButtonCours);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailNumeroCours.setText(bundle.getString("numeroCours"));
            detailNomCours.setText(bundle.getString("nameCours"));
            detailSalleCours.setText(bundle.getString("salleCours"));
            detailParcoursCours.setText(bundle.getString("parcoursCours"));
            detailNiveauCours.setText(bundle.getString("niveauCours"));
            detailDescriptionCours.setText(bundle.getString("descriptionCours"));
            detailDate.setText(bundle.getString("dateCours"));  // Récupérer la date
            detailHeure.setText(bundle.getString("heureCours")); // Récupérer l'heure
            key2 = bundle.getString("key2");
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivityCours.this, updateCours.class)
                        .putExtra("numeroCours", detailNumeroCours.getText().toString())
                        .putExtra("nameCours", detailNomCours.getText().toString())
                        .putExtra("salleCours", detailSalleCours.getText().toString())
                        .putExtra("parcoursCours", detailParcoursCours.getText().toString())
                        .putExtra("niveauCours", detailNiveauCours.getText().toString())
                        .putExtra("descriptionCours", detailDescriptionCours.getText().toString())
                        .putExtra("dateCours", detailDate.getText().toString())  // Passer la date
                        .putExtra("heureCours", detailHeure.getText().toString()) // Passer l'heure
                        .putExtra("key2", key2);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cours");
                final DatabaseReference salleReference = FirebaseDatabase.getInstance().getReference("Salle");

                // Suppression du cours
                reference.child(key2).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Récupération du nom de la salle à partir du détail du cours
                        String salleName = detailSalleCours.getText().toString();

                        // Mise à jour du statut de la salle à "Disponible"
                        salleReference.orderByChild("nameSalle").equalTo(salleName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot salleSnapshot : dataSnapshot.getChildren()) {
                                        salleSnapshot.getRef().child("statutSalle").setValue("Disponible").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(DetailActivityCours.this, "Cours supprimé et salle mise à jour avec succès", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Cours.class));
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(DetailActivityCours.this, "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, Cours.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}