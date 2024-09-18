package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class detailActivity3 extends AppCompatActivity {
    TextView detailNumeroSalle, detailNomSalle, detailStatutSalle, detailDescriptionSalle;
    FloatingActionButton deleteButton, editButton;
    String key3 = "";
    AlertDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        detailNumeroSalle = findViewById(R.id.detailNumeroSalle);
        detailNomSalle = findViewById(R.id.detailNomSalle);
        detailStatutSalle = findViewById(R.id.detailStatutSalle);
        detailDescriptionSalle = findViewById(R.id.detailDescriptionSalle);

        deleteButton = findViewById(R.id.deleteButtonSalle);
        editButton = findViewById(R.id.editButtonSalle);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailNumeroSalle.setText(bundle.getString("NumeroSalle"));
            detailNomSalle.setText(bundle.getString("NomSalle"));
            detailStatutSalle.setText(bundle.getString("StatutSalle"));
            detailDescriptionSalle.setText(bundle.getString("DescriptionSalle"));
            key3 = bundle.getString("key3");
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailActivity3.this, updateSalle.class)
                        .putExtra("NumeroSalle", detailNumeroSalle.getText().toString())
                        .putExtra("NomSalle", detailNomSalle.getText().toString())
                        .putExtra("StatutSalle", detailStatutSalle.getText().toString())
                        .putExtra("DescriptionSalle", detailDescriptionSalle.getText().toString())
                        .putExtra("key3", key3);
                startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Salle");
                        reference.child(key3).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(detailActivity3.this, "Supprimé", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Salle.class));
                                finish();
                            }
                        });
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