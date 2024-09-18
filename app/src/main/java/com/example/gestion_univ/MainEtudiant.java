package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainEtudiant extends AppCompatActivity {
    private DrawerLayout drawerLayout1;
    private ImageButton buttonDrawerToggle1;
    private NavigationView navigationView1;
    private FirebaseAuth mAuth1;
    private FirebaseUser user1;
    private FirebaseFirestore db1;
    private ImageView imageLarge1;
    private ProgressDialog progressDialog1;
    private boolean isDataLoaded1 = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_etudiant);

        // Initialisation des vues
        drawerLayout1 = findViewById(R.id.main1);
        buttonDrawerToggle1 = findViewById(R.id.buttonDrawerToggle1);
        navigationView1 = findViewById(R.id.navigationView1);
        View headerView = navigationView1.getHeaderView(0);
        imageLarge1 = headerView.findViewById(R.id.viewPhoto1);

        // Initialisation Firebase
        mAuth1 = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        user1 = mAuth1.getCurrentUser();

        // ProgressDialog pour indiquer le chargement
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Chargement des données...");
        progressDialog1.setCancelable(false);
        progressDialog1.show();

        // Mise à jour de l'en-tête de navigation
        updateNavheader1();

        // Gestion du clic sur l'image de profil
        imageLarge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageLarge1();
            }
        });
        // Gestion du bouton de navigation
        buttonDrawerToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout1.open();
            }
        });
// Gestion des éléments du menu de NavigationView
        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isDataLoaded1) {
                    handleMenuItemClick1(item);
                } else {
                    Toast.makeText(MainEtudiant.this, "Veuillez patienter jusqu'à ce que les données soient chargées.", Toast.LENGTH_SHORT).show();
                }
                drawerLayout1.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void handleMenuItemClick1(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.Cours1) {
            Toast.makeText(MainEtudiant.this, "Home", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.Event1) {
            Toast.makeText(MainEtudiant.this, "Evenement", Toast.LENGTH_SHORT).show();
        }  else if (itemId == R.id.About1) {
            Toast.makeText(MainEtudiant.this, "A propos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainEtudiant.this, ProfileUser.class);
            startActivity(intent);
            finish();
        }else if (itemId == R.id.Lgout1) {
            showConfirmationDialog1();
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout1.isDrawerOpen(GravityCompat.START)) {
            drawerLayout1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void showConfirmationDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir effectuer cette action ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth1.signOut();
                Intent intent = new Intent(MainEtudiant.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Non", null);
        builder.show();
    }
    public void updateNavheader1() {
        View headerView = navigationView1.getHeaderView(0);
        TextView emailView = headerView.findViewById(R.id.viewEmail1);
        ImageView photoView = headerView.findViewById(R.id.viewPhoto1);
        TextView username = headerView.findViewById(R.id.viewUsername1);

        if (user1 != null) {
            String userId = user1.getUid();
            db1.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            emailView.setText(document.getString("Email"));
                            username.setText(document.getString("NomComplet"));
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(MainEtudiant.this).load(imageUrl).transform(new CircleCrop()).into(photoView);
                            }
                            isDataLoaded1 = true;
                            progressDialog1.dismiss();
                        }
                    } else {
                        Log.d("MainEtudiant", "get failed with ", task.getException());
                        progressDialog1.dismiss();
                    }
                }
            });
        } else {
            progressDialog1.dismiss();
        }
    }
    private void showProfileImageLarge1() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageViewlarge = dialog.findViewById(R.id.imageViewLarge);

        if (user1 != null) {
            String userId = user1.getUid();
            db1.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(MainEtudiant.this).load(imageUrl).into(imageViewlarge);
                            }
                        }
                    } else {
                        Log.d("ProfileAdmin", "get failed with ", task.getException());
                    }
                }
            });
        }

        dialog.show();
    }
}