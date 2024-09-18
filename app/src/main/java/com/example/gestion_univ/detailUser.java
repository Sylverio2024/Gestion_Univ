package com.example.gestion_univ;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserRecord;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class detailUser extends AppCompatActivity {
    TextView NomUser, EmailUser, TelephoneUser, RoleUser;
    ImageView detailImageU;
    Switch accountSwitch;
    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    String userEmail;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        // Initialisation des vues
        NomUser = findViewById(R.id.NomUser);
        EmailUser = findViewById(R.id.EmailUser);
        TelephoneUser = findViewById(R.id.TelephoneUser);
        RoleUser = findViewById(R.id.RoleUser);
        detailImageU = findViewById(R.id.detailImageU);
        accountSwitch = findViewById(R.id.switchActivateAccount);

        // Initialisation de FirebaseFirestore et FirebaseAuth
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Récupération des données passées par l'activité précédente
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Mise à jour des vues avec les données de l'utilisateur
            NomUser.setText(bundle.getString("nomComplet"));
            EmailUser.setText(bundle.getString("email"));
            TelephoneUser.setText(bundle.getString("telephone"));
            RoleUser.setText(bundle.getString("role"));
            String imageUrl = bundle.getString("imageUrl");
            Glide.with(this).load(imageUrl).into(detailImageU);

            // Récupérer l'email de l'utilisateur
            userEmail = bundle.getString("email");

            // Rechercher l'utilisateur dans Firestore en utilisant l'email
            fstore.collection("Users")
                    .whereEqualTo("Email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userId = document.getId();  // ID du document (ID utilisateur)

                                // Récupération de l'état du compte depuis Firestore
                                String compte = document.getString("compte");
                                if (compte != null) {
                                    // Initialiser l'état du Switch en fonction du champ "compte"
                                    accountSwitch.setChecked("Desactive".equals(compte));
                                }

                                // Gérer les changements d'état du Switch
                                accountSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    String newStatus = isChecked ? "Desactive" : "Active";
                                    fstore.collection("Users").document(userId)
                                            .update("compte", newStatus)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(detailUser.this, "Le compte a été " + (isChecked ? "désactivé" : "activé"), Toast.LENGTH_SHORT).show())
                                            .addOnFailureListener(e -> Toast.makeText(detailUser.this, "Erreur de mise à jour du compte", Toast.LENGTH_SHORT).show());
                                });
                            }
                        } else {
                            Toast.makeText(detailUser.this, "Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(detailUser.this, "Erreur de recherche : " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        // Suppression de l'utilisateur lors du clic sur le bouton de suppression
        findViewById(R.id.deleteButton).setOnClickListener(v -> {
            if (userEmail != null) {
               // deleteUserAccount(userEmail);
            }
        });
    }

    // Méthode pour supprimer l'utilisateur de Firebase Authentication et Firestore
    private void deleteUserAccount(String email) {
        fstore.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Récupérer l'utilisateur dans Firebase Authentication
                    mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().getSignInMethods() != null && !task.getResult().getSignInMethods().isEmpty()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.delete()
                                        .addOnSuccessListener(aVoid1 -> {
                                            Toast.makeText(detailUser.this, "Utilisateur supprimé avec succès", Toast.LENGTH_SHORT).show();
                                            redirectToPreviousScreen();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(detailUser.this, "Erreur lors de la suppression de l'utilisateur", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(detailUser.this, "Utilisateur introuvable pour suppression", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(detailUser.this, "Aucun compte Firebase Authentication associé à cet email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(detailUser.this, "Erreur de récupération du compte : " + e.getMessage(), Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e -> Toast.makeText(detailUser.this, "Erreur lors de la suppression de l'utilisateur dans Firestore", Toast.LENGTH_SHORT).show());
    }

    // Redirection vers l'écran précédent
    private void redirectToPreviousScreen() {
        Intent intent = new Intent(this, Utilisateur.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            redirectToPreviousScreen();
        } else {
            super.onBackPressed();
        }
    }
}