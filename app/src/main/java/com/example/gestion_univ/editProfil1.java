package com.example.gestion_univ;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class editProfil1 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    String userId;
    private EditText editName1;
    Button saveBtn1;
    ImageButton btnRetour1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profil1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent data1 = getIntent();
        String fullName1=data1.getStringExtra("fullName1");
        String Email1=data1.getStringExtra("Email1");


        editName1=findViewById(R.id.editName1);


        editName1.setText(fullName1);


        // Initialisation des instances Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user=mAuth.getCurrentUser();
        userId=mAuth.getCurrentUser().getUid();
        saveBtn1=findViewById(R.id.btnSave1);
        btnRetour1=findViewById(R.id.BackTeach);
        btnRetour1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( editProfil1.this, ProfileUser.class);
                startActivity(intent);
                finish();
            }
        });
        saveBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(editProfil1.this);
                progressDialog.setMessage("Updating...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if(editName1.getText().toString().isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(editProfil1.this, "le champ est vide", Toast.LENGTH_SHORT).show();
                    return;
                }


                user.updateEmail(Email1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        DocumentReference docRef=db.collection("Users").document(user.getUid());
                        Map<String, Object> edited= new HashMap<>();
                        edited.put("NomComplet",editName1.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                progressDialog.dismiss();
                                Toast.makeText(editProfil1.this, "Modification avec succès", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),ProfileUser.class));
                                finish();

                            }
                        });
                        //Toast.makeText(editProfil.this, "Email est changé", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(editProfil1.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, ProfileUser.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}