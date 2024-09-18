package com.example.gestion_univ;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class fn5 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageButton buttonDrawerToggle;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ImageView imageLarge, salle,notification,cours,evenement;
    private ProgressDialog progressDialog;
    private boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues
        drawerLayout = findViewById(R.id.main);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imageLarge = headerView.findViewById(R.id.viewPhoto);
        salle=findViewById(R.id.salle);
        cours=findViewById(R.id.cours);
        notification=findViewById(R.id.notification);
        evenement=findViewById(R.id.evenement);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        // ProgressDialog pour indiquer le chargement
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement des données...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Mise à jour de l'en-tête de navigation
        updateNavheader();

        // Gestion du clic sur l'image de profil
        imageLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageLarge();
            }
        });
        cours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fn5.this, Cours.class);
                startActivity(intent);
                finish();
            }
        });
        salle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fn5.this, Salle.class);
                startActivity(intent);
                finish();
            }
        });
        evenement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fn5.this, Evenement.class);
                startActivity(intent);
                finish();
            }
        });

        // Gestion du bouton de navigation
        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        // Gestion des éléments du menu de NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isDataLoaded) {
                    handleMenuItemClick(item);
                } else {
                    Toast.makeText(fn5.this, "Veuillez patienter jusqu'à ce que les données soient chargées.", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void handleMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            Toast.makeText(fn5.this, "Home", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.Teach) {
            Intent intent = new Intent(fn5.this, fnEnseignant.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.Stud) {
            Intent intent = new Intent(fn5.this, fnEtudiant.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.Cours) {
            Toast.makeText(fn5.this, "Cours", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(fn5.this, Cours.class);
            startActivity(intent);
            finish();
        }
        else if (itemId == R.id.Event) {
            Toast.makeText(fn5.this, "Evenement", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(fn5.this, Evenement.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.User) {
            Toast.makeText(fn5.this, "Utilisateur", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(fn5.this, Utilisateur.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.About) {
            Intent intent = new Intent(fn5.this, ProfileAdmin.class);
            startActivity(intent);
            finish();
        }else if (itemId == R.id.qrdata) {
            Intent intent = new Intent(fn5.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }
        else if (itemId == R.id.Lgout) {
            showConfirmationDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir effectuer cette action ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(fn5.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Non", null);
        builder.show();
    }

    public void updateNavheader() {
        View headerView = navigationView.getHeaderView(0);
        TextView emailView = headerView.findViewById(R.id.viewEmail);
        ImageView photoView = headerView.findViewById(R.id.viewPhoto);
        TextView username = headerView.findViewById(R.id.viewUsername);

        if (user != null) {
            String userId = user.getUid();
            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            emailView.setText(document.getString("Email"));
                            username.setText(document.getString("NomComplet"));
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(fn5.this).load(imageUrl).transform(new CircleCrop()).into(photoView);
                            }
                            isDataLoaded = true;
                            progressDialog.dismiss();
                        }
                    } else {
                        Log.d("fn5", "get failed with ", task.getException());
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
        }
    }

    private void showProfileImageLarge() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageViewlarge = dialog.findViewById(R.id.imageViewLarge);

        if (user != null) {
            String userId = user.getUid();
            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(fn5.this).load(imageUrl).into(imageViewlarge);
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