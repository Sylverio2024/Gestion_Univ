package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdmin extends AppCompatActivity {
    ImageButton buttonRetourE;
    Button resetPassword, btnEditProfil;
    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView imageView;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    String userId;
    private TextView textname, textEmail, textTelephone;
    ImageView editImage;
    ProgressDialog progressDialog;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des éléments de l'interface utilisateur
        imageView = findViewById(R.id.imageView8);
        textname = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textTelephone = findViewById(R.id.textPhone);

        // Initialisation des instances Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = mAuth.getCurrentUser();
        userId = mAuth.getCurrentUser().getUid();

        viewprofil();


        progressDialog = new ProgressDialog(ProfileAdmin.this);

        buttonRetourE = findViewById(R.id.BackTeach);
        btnEditProfil = findViewById(R.id.btnProfil);
        resetPassword = findViewById(R.id.btnPassword);
        editImage = findViewById(R.id.editIMG);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileAdmin.this, choosePDP.class);
                startActivity(intent);
                finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileAdmin.this, fn5.class);
                startActivity(intent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileAdmin.this, editPDP.class);
                startActivity(intent);
                finish();
            }
        });
        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editProfil.class);
                intent.putExtra("fullName", textname.getText().toString());
                intent.putExtra("Email", textEmail.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void viewprofil() {
        if (user != null) {
            String userId = user.getUid();
            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            textname.setText(document.getString("NomComplet"));
                            textEmail.setText(document.getString("Email"));
                            textTelephone.setText(document.getString("Telephone"));
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(ProfileAdmin.this).load(imageUrl).transform(new CircleCrop()).into(imageView);
                            }
                        }
                    } else {
                        Log.d("ProfileAdmin", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Changer le mot de passe");
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir changer votre mot de passe?");
        alertDialogBuilder.setPositiveButton("Oui", (dialog, which) -> {
            dialog.dismiss();
            showChangePasswordDialog();
        });
        alertDialogBuilder.setNegativeButton("Non", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Entrer le nouveau mot de passe");
        EditText input = new EditText(this);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton("Changer", (dialog, which) -> {
            String newPassword = input.getText().toString();
            if (newPassword.length() < 6 && !newPassword.isEmpty()) {
                Toast.makeText(ProfileAdmin.this, "Mot de passe trop court", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.isEmpty()) {
                reauthenticateAndChangePassword(newPassword);
            } else if (newPassword.isEmpty()) {
                Toast.makeText(ProfileAdmin.this, "S'il vous plaît entrer le nouveau mot de passe", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void reauthenticateAndChangePassword(String newPassword) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Réauthentification requise");
        alertDialogBuilder.setMessage("Veuillez entrer votre mot de passe actuel pour continuer.");
        EditText inputPassword = new EditText(this);
        alertDialogBuilder.setView(inputPassword);
        alertDialogBuilder.setPositiveButton("Confirmer", (dialog, which) -> {
            String currentPassword = inputPassword.getText().toString();
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (currentPassword.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(ProfileAdmin.this, "Mot de passe actuel requis", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        changePassword(newPassword);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileAdmin.this, "Réauthentification échouée", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void changePassword(String newPassword) {
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Mot de passe mis à jour avec succès!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Échec de la mise à jour du mot de passe!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            StorageReference storageRef = storage.getReference().child("user_images/" + userId);

            storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                db.collection("Users").document(userId).update("imageUrl", imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileAdmin.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileAdmin.this, "Error updating image URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(ProfileAdmin.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, fn5.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}