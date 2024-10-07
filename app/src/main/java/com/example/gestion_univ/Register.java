package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText fullName, email, password, phone;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private ProgressBar pgBar;
    private TextView txtLogin;
    private ImageView backRegistre;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Modifier la couleur de la barre d'état
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.ic_launcher_background)); // Remplacez par la couleur désirée
        }

        fullName = findViewById(R.id.txtName);
        email = findViewById(R.id.eTextEmail);
        password = findViewById(R.id.eTextPassword);
        phone = findViewById(R.id.txtPhone);
        registerBtn = findViewById(R.id.btnRegistre);
        pgBar = findViewById(R.id.progressBar);
        txtLogin = findViewById(R.id.txtlogin); // TextView pour retourner à l'écran de login
        backRegistre = findViewById(R.id.bckRegister); // ImageView pour retourner à l'écran de login

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        registerBtn.setOnClickListener(v -> {
            checkField(fullName);
            checkField(email);
            checkField(password);
            checkField(phone);

            if (isValidEmail(email.getText().toString().trim()) && isValidPassword(password.getText().toString().trim())) {
                pgBar.setVisibility(View.VISIBLE);
                mAuth.fetchSignInMethodsForEmail(email.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (isNewUser) {
                                createFirebaseAccount();
                            } else {
                                pgBar.setVisibility(View.GONE);
                                email.setError("Email déjà utilisé. Veuillez en choisir un autre.");
                                Toast.makeText(Register.this, "Email déjà utilisé", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Retourner à l'écran de login via le TextView
        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, login.class);
            startActivity(intent);
            finish();
        });

        // Retourner à l'écran de login via l'ImageView
        backRegistre.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, login.class);
            startActivity(intent);
            finish();
        });
    }

    private void createFirebaseAccount() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnSuccessListener(authResult -> {
                    String userID = mAuth.getCurrentUser().getUid();
                    Toast.makeText(Register.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                    // Sauvegarder les informations de l'utilisateur dans Firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("NomComplet", fullName.getText().toString());
                    user.put("Email", email.getText().toString().trim());
                    user.put("Telephone", phone.getText().toString().trim());
                    user.put("role", "utilisateur");
                    user.put("compte", "Active");
                    fstore.collection("Users").document(userID).set(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(Register.this, "Utilisateur enregistré dans Firestore", Toast.LENGTH_SHORT).show();
                                pgBar.setVisibility(View.GONE);
                                Intent intent = new Intent(Register.this, verificationEmail.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Register.this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                                pgBar.setVisibility(View.GONE);
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Register.this, "Erreur lors de l'inscription: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    pgBar.setVisibility(View.GONE);
                });

    }

    private boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Erreur");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Email non valide");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            this.password.setError("Le mot de passe doit contenir au moins 6 caractères");
            return false;
        }
        return true;
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