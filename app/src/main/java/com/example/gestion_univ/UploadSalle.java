package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
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

public class UploadSalle extends AppCompatActivity {
    private ScrollView scrollView3;
    Button bntSaveSalle;
    AlertDialog dialog;
    DatabaseReference databaseReference;
    EditText txtNumeroSalle, txtNameSalle, txtDescriptionSalle;
    String statutSalle;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_salle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bntSaveSalle = findViewById(R.id.bntSaveSalle);
        txtNumeroSalle = findViewById(R.id.txtNumeroSalle);
        txtNameSalle = findViewById(R.id.txtNameSalle);
        txtDescriptionSalle = findViewById(R.id.txtDescriptionSalle);
        scrollView3 = findViewById(R.id.scrollView3);

        databaseReference = FirebaseDatabase.getInstance().getReference("Salle");
        scrollView3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r= new Rect();
                scrollView3.getWindowVisibleDisplayFrame(r);
                int screenHeight=scrollView3.getRootView().getHeight();
                int keypadHeight=screenHeight-r.bottom;
                if(keypadHeight>screenHeight*0.15){
                    scrollView3.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView3.smoothScrollTo(0, txtDescriptionSalle.getBottom());
                        }
                    });
                }
            }
        });
        bntSaveSalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NumeroSalle = txtNumeroSalle.getText().toString();
                String NameSalle = txtNameSalle.getText().toString();
                String DescriptionSalle = txtDescriptionSalle.getText().toString();
                if (NumeroSalle.isEmpty() || NameSalle.isEmpty() || DescriptionSalle.isEmpty()) {
                    Toast.makeText(UploadSalle.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    checkIfNumeroIDExistsAndSaveData(NumeroSalle);
                }
            }
        });
    }
    private void checkIfNumeroIDExistsAndSaveData(final String numeroSalle) {
        databaseReference.orderByChild("numeroSalle").equalTo(numeroSalle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseCheck", "Checking for numeroID: " + numeroSalle);
                if (dataSnapshot.exists()) {
                    Log.d("FirebaseCheck", "NumeroID exists: " + numeroSalle);
                    Toast.makeText(UploadSalle.this, "Le numéro  existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("FirebaseCheck", "NumeroID does not exist, proceeding to save data");
                    uploadData();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseCheck", "Error checking numeroID", databaseError.toException());
                Toast.makeText(UploadSalle.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void uploadData() {
        String numeroSalle = txtNumeroSalle.getText().toString();
        String nameSalle = txtNameSalle.getText().toString();
        String DescriptionSalle = txtDescriptionSalle.getText().toString();
        statutSalle="Disponible";
        DataClass3 dataClass3 = new DataClass3(numeroSalle, nameSalle, DescriptionSalle,statutSalle);
        String currentDate = String.valueOf(System.currentTimeMillis());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadSalle.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button bntQ = dialogView.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        databaseReference.child(currentDate).setValue(dataClass3).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadSalle.this, "Enregistrement effectué", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UploadSalle.this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadSalle.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, Salle.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}