package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class fn4 extends AppCompatActivity {
    private final int SPLASH_SCREEN_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Rediriger vers la page principale "MainActivity" après 3 secondes.
        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference df = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(userId);

                df.get().addOnSuccessListener(documentSnapshot -> {
                    String role = documentSnapshot.getString("role");
                    String compte = documentSnapshot.getString("compte");

                    if (role != null) {
                        if ("Active".equals(compte)) {
                            if ("administrateur".equals(role)) {
                                startActivity(new Intent(getApplicationContext(), fn5.class));
                            } else if ("utilisateur".equals(role)) {
                                startActivity(new Intent(getApplicationContext(), MainEtudiant.class));
                            } else {
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getApplicationContext(), login.class));
                            }
                        } else {
                            // Redirect to login page if the account is inactive
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), login.class));
                        }
                        finish();
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), login.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                });
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}