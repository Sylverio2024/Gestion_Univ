package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verificationEmail extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Handler handler = new Handler();
    private Runnable emailCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_email);

        // Configuration pour l'affichage en bord à bord
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Configurer le bouton de renvoi de l'email de vérification
        Button btnResendVerification = findViewById(R.id.btnResendVerification);
        btnResendVerification.setOnClickListener(v -> {
            if (currentUser != null) {
                currentUser.sendEmailVerification()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(verificationEmail.this, "Email de vérification envoyé.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(verificationEmail.this, "Échec de l'envoi de l'email de vérification.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Configurer le bouton de retour
        ImageView backEmail = findViewById(R.id.bckEmail);
        backEmail.setOnClickListener(v -> {
            // Retourner à l'écran de connexion
            Intent intent = new Intent(verificationEmail.this, login.class);
            startActivity(intent);
            finish();
        });

        // Démarrer la vérification périodique de l'email
        emailCheckRunnable = new Runnable() {
            @Override
            public void run() {
                currentUser.reload().addOnSuccessListener(aVoid -> {
                    if (currentUser.isEmailVerified()) {
                        // Email vérifié, rediriger vers ScannerActivity
                        Toast.makeText(verificationEmail.this, "Email vérifié avec succès.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(verificationEmail.this, ScannerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Email non vérifié, continuer à vérifier
                        handler.postDelayed(this, 3000);
                    }
                });
            }
        };
        handler.post(emailCheckRunnable);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mAuth.signOut();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(emailCheckRunnable);
        mAuth.signOut();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}