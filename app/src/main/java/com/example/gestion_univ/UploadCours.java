package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class UploadCours extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOIX_SALLE = 1;

    private ScrollView scrollView2;
    Button saveButton2;
    AlertDialog dialog;
    DatabaseReference databaseReference, salleReference;
    EditText txtNumeroCours, txtNameCours, txtSalleCours, txtParcoursCours, txtNiveauCours, txtDescriptionCours;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_cours);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveButton2 = findViewById(R.id.bntSaveCours);
        txtNumeroCours = findViewById(R.id.txtNumeroCours);
        txtNameCours = findViewById(R.id.txtNameCours);
        txtSalleCours = findViewById(R.id.txtSalleCours);
        txtParcoursCours = findViewById(R.id.txtParcoursCours);
        txtNiveauCours = findViewById(R.id.txtNiveauCours);
        txtDescriptionCours = findViewById(R.id.txtDescriptionCours);
        scrollView2 = findViewById(R.id.scrollView2);

        txtSalleCours.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (txtSalleCours.getRight() - txtSalleCours.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Ouvrir l'activité choixSalle ici
                        Intent intent = new Intent(UploadCours.this, choixSalle.class);
                        startActivityForResult(intent, REQUEST_CODE_CHOIX_SALLE);
                        return true;
                    }
                }
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Cours");
        salleReference = FirebaseDatabase.getInstance().getReference("Salle");

        scrollView2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                scrollView2.getWindowVisibleDisplayFrame(r);
                int screenHeight = scrollView2.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    scrollView2.post(() -> scrollView2.smoothScrollTo(0, txtDescriptionCours.getBottom()));
                }
            }
        });

        saveButton2.setOnClickListener(v -> {
            String NumeroCours = txtNumeroCours.getText().toString();
            String NameCours = txtNameCours.getText().toString();
            String SalleCours = txtSalleCours.getText().toString();
            String ParcoursCours = txtParcoursCours.getText().toString();
            String NiveauCours = txtNiveauCours.getText().toString();
            String DescriptionCours = txtDescriptionCours.getText().toString();
            if (NumeroCours.isEmpty() || NameCours.isEmpty() || SalleCours.isEmpty() || ParcoursCours.isEmpty() || NiveauCours.isEmpty() || DescriptionCours.isEmpty()) {
                Toast.makeText(UploadCours.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                checkIfNumeroIDExistsAndSaveData(NumeroCours);
            }
        });
    }

    private void checkIfNumeroIDExistsAndSaveData(final String numeroCours) {
        databaseReference.orderByChild("numeroCours").equalTo(numeroCours).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseCheck", "Checking for numeroID: " + numeroCours);
                if (dataSnapshot.exists()) {
                    Log.d("FirebaseCheck", "NumeroID exists: " + numeroCours);
                    Toast.makeText(UploadCours.this, "Le numéro existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("FirebaseCheck", "NumeroID does not exist, proceeding to save data");
                    uploadData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseCheck", "Error checking numeroID", databaseError.toException());
                Toast.makeText(UploadCours.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadData() {
        String numeroCours = txtNumeroCours.getText().toString();
        String nameCours = txtNameCours.getText().toString();
        String salleCours = txtSalleCours.getText().toString();
        String parcoursCours = txtParcoursCours.getText().toString();
        String niveauCours = txtNiveauCours.getText().toString();
        String descriptionCours = txtDescriptionCours.getText().toString();

        DataClass2 dataClass2 = new DataClass2(numeroCours, nameCours, salleCours, parcoursCours, niveauCours, descriptionCours);
        String currentDate = String.valueOf(System.currentTimeMillis());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadCours.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button bntQ = dialogView.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(v -> dialog.dismiss());

        databaseReference.child(currentDate).setValue(dataClass2).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateSalleStatus(salleCours); // Mise à jour du statut de la salle
            } else {
                Toast.makeText(UploadCours.this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(UploadCours.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void updateSalleStatus(String salleCours) {
        salleReference.orderByChild("nameSalle").equalTo(salleCours).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("statutSalle").setValue("Utilisé").addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UploadCours.this, "Enregistrement effectué", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UploadCours.this, "Erreur de mise à jour du statut de la salle", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(UploadCours.this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                } else {
                    Toast.makeText(UploadCours.this, "Salle non trouvée", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadCours.this, "Erreur de lecture de la base de données", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOIX_SALLE && resultCode == RESULT_OK && data != null) {
            String selectedSalle = data.getStringExtra("salle_nom");
            txtSalleCours.setText(selectedSalle);
        }
    }
}