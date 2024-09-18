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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class fn3 extends AppCompatActivity {
    private TextView txtForgot;
    private EditText logEmail, logPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ImageView backfn2;

    TextInputLayout Lpassword;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtForgot = findViewById(R.id.textForgot);
        logEmail = findViewById(R.id.eTextEmail);
        logPassword = findViewById(R.id.eTextPassword);
        btnLogin = findViewById(R.id.btnSingin);
        auth = FirebaseAuth.getInstance();
        backfn2 = findViewById(R.id.back_fn2);
        Lpassword = findViewById(R.id.layoutPassword);
        progressBar=findViewById(R.id.proBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = logEmail.getText().toString();
                String Password = logPassword.getText().toString();
                if (!Email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    if (!Password.isEmpty()) {
                        auth.signInWithEmailAndPassword(Email, Password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        Toast.makeText(fn3.this, "connexion avec succes", Toast.LENGTH_SHORT).show();
                                        Intent packageContext;
                                        Intent intent = new Intent( fn3.this, fn5.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(fn3.this, "Echèc de connexion", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        logPassword.setError("Empty fields are not allowed");

                    }
                } else if (Email.isEmpty()) {
                    logEmail.setError("Empty fields are not allowed");
                }else if(logPassword.length()<=6){
                    logPassword.setError("Mot de passe doit avoir au moins 6 caractères");
                }
                else {
                    logEmail.setError("Please enter the correct email");
                }
            }
        });
        backfn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent(fn3.this, fen2.class);
                startActivity(intent);
                finish();
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_blue));
        }

    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            // Si c'est la première activité, retournez à l'écran d'accueil ou quittez l'application
            Intent intent = new Intent(this, fen2.class);
            startActivity(intent);
            finish();
        } else {
            // Si ce n'est pas la première activité, poursuivez avec le comportement par défaut du bouton de retour
            super.onBackPressed();
        }

    }
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fn3.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString();

                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(fn3.this, "Entrez votre identifiant email enregistré", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(fn3.this, "Vérifiez votre email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(fn3.this, "Impossible d'envoyer, échec", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialogView.findViewById(R.id.BtnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }
}