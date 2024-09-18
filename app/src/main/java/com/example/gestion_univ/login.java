package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    ImageView viewMain;
    TextView txtRegister, txtdialog;
    Button btnconnexion;
    private EditText emailLogin, passwordLogin;
    boolean valid = true;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    ProgressBar pgBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        viewMain = findViewById(R.id.bckLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtdialog = findViewById(R.id.txtdialog);

        txtdialog.setOnClickListener(view -> showDialog());

        emailLogin = findViewById(R.id.txtLemail);
        passwordLogin = findViewById(R.id.txtLmdp);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        fstore = FirebaseFirestore.getInstance();

        btnconnexion = findViewById(R.id.btnConnexion);
        pgBar = findViewById(R.id.progressBar2);

        btnconnexion.setOnClickListener(v -> {
            checkField(emailLogin);
            checkField(passwordLogin);
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLogin.setError("Ecrire le bon email");
            } else if (password.length() < 6) {
                passwordLogin.setError("Le mot de passe doit avoir au moins 6 caractères");
            } else {
                if (valid) {
                    pgBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null && !currentUser.isEmailVerified()) {
                            // Rediriger vers l'activité de vérification de l'email
                            Intent intent = new Intent(login.this, verificationEmail.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Continuer avec la vérification du niveau de l'utilisateur
                            checkUserLevel(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        pgBar.setVisibility(View.GONE);
                    });
                }
            }
        });

        txtRegister.setOnClickListener(v -> {
            // Redirection vers l'activité d'inscription
            Intent intent = new Intent(login.this, Register.class);
            startActivity(intent);
            finish();
        });

        viewMain.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserLevel(String uid) {
        DocumentReference df = fstore.collection("Users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
            String role = documentSnapshot.getString("role");
            String compte = documentSnapshot.getString("compte");

            if (role != null) {
                if ("administrateur".equals(role)) {
                    Toast.makeText(login.this, "Connexion réussie en tant qu'administrateur", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), fn5.class));
                    finish();
                } else if ("utilisateur".equals(role)) {
                    if ("Active".equals(compte)) {
                        Toast.makeText(login.this, "Connexion réussie en tant qu'utilisateur", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
                        finish();
                    } else {
                        Toast.makeText(login.this, "Votre compte est désactivé. Veuillez contacter l'administrateur.", Toast.LENGTH_SHORT).show();
                        pgBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(login.this, "Rôle inconnu", Toast.LENGTH_SHORT).show();
                    pgBar.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(login.this, "Aucun rôle attribué", Toast.LENGTH_SHORT).show();
                pgBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(login.this, "Erreur lors de la récupération des données utilisateur", Toast.LENGTH_SHORT).show();
            pgBar.setVisibility(View.GONE);
        });
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Erreur");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(view -> {
            dialog.dismiss();
            ProgressDialog progressDialog = new ProgressDialog(login.this);
            progressDialog.setMessage("Veuillez patienter...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String userEmail = emailBox.getText().toString();

            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                progressDialog.dismiss();
                Toast.makeText(login.this, "Entrez votre identifiant email enregistré", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Vérifiez votre email", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Impossible d'envoyer, échec", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialogView.findViewById(R.id.BtnCancel).setOnClickListener(view -> dialog.dismiss());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }
}