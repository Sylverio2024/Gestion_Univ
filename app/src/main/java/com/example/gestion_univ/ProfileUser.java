package com.example.gestion_univ;

import android.app.ProgressDialog;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUser extends AppCompatActivity {
    ImageButton buttonRetourE;
    Button resetPassword1, btnEditProfil1;
    private static final int PICK_IMAGE_REQUEST = 1;

    private CircleImageView imageView1;
    private Uri imageUri1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseStorage storage;
    String userId;
    private TextView textname1, textEmail1, textTelephone1;
    ImageView editImage1;
    ProgressDialog progressDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialisation des éléments de l'interface utilisateur
        imageView1 = findViewById(R.id.imageView81);
        textname1 = findViewById(R.id.textName1);
        textEmail1 = findViewById(R.id.textEmail1);
        textTelephone1 = findViewById(R.id.textPhone1);

        // Initialisation des instances Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = mAuth.getCurrentUser();
        userId = mAuth.getCurrentUser().getUid();

        viewprofil1();


        progressDialog1= new ProgressDialog(ProfileUser.this);

        buttonRetourE = findViewById(R.id.BackTeach);
        btnEditProfil1 = findViewById(R.id.btnProfil1);
        resetPassword1 = findViewById(R.id.btnPassword1);
        editImage1 = findViewById(R.id.editIMG1);
        editImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileUser.this, choosePDP1.class);
                startActivity(intent);
                finish();
            }
        });
        resetPassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog1();
            }
        });

        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileUser.this, MainEtudiant.class);
                startActivity(intent);
                finish();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileUser.this, editPDP1.class);
                startActivity(intent);
                finish();
            }
        });
        btnEditProfil1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editProfil1.class);
                intent.putExtra("fullName1", textname1.getText().toString());
                intent.putExtra("Email1", textEmail1.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void viewprofil1() {
        if (user != null) {
            String userId = user.getUid();
            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            textname1.setText(document.getString("NomComplet"));
                            textEmail1.setText(document.getString("Email"));
                            textTelephone1.setText(document.getString("Telephone"));
                            String imageUrl1 = document.getString("imageUrl");
                            if (imageUrl1 != null && !imageUrl1.isEmpty()) {
                                Glide.with(ProfileUser.this).load(imageUrl1).transform(new CircleCrop()).into(imageView1);
                            }
                        }
                    } else {
                        Log.d("ProfileAdmin", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    private void showConfirmationDialog1() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Changer le mot de passe");
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir changer votre mot de passe?");
        alertDialogBuilder.setPositiveButton("Oui", (dialog, which) -> {
            dialog.dismiss();
            showChangePasswordDialog1();
        });
        alertDialogBuilder.setNegativeButton("Non", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void showChangePasswordDialog1() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Entrer le nouveau mot de passe");
        EditText input1 = new EditText(this);
        alertDialogBuilder.setView(input1);
        alertDialogBuilder.setPositiveButton("Changer", (dialog, which) -> {
            String newPassword1 = input1.getText().toString();
            if (newPassword1.length() < 6 && !newPassword1.isEmpty()) {
                Toast.makeText(ProfileUser.this, "Mot de passe trop court", Toast.LENGTH_SHORT).show();
            } else if (!newPassword1.isEmpty()) {
                reauthenticateAndChangePassword1(newPassword1);
            } else if (newPassword1.isEmpty()) {
                Toast.makeText(ProfileUser.this, "S'il vous plaît entrer le nouveau mot de passe", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void reauthenticateAndChangePassword1(String newPassword1) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Réauthentification requise");
        alertDialogBuilder.setMessage("Veuillez entrer votre mot de passe actuel pour continuer.");
        EditText inputPassword1 = new EditText(this);
        alertDialogBuilder.setView(inputPassword1);
        alertDialogBuilder.setPositiveButton("Confirmer", (dialog, which) -> {
            String currentPassword1 = inputPassword1.getText().toString();
            progressDialog1.setMessage("Updating...");
            progressDialog1.setCancelable(false);
            progressDialog1.show();
            if (currentPassword1.isEmpty()) {
                progressDialog1.dismiss();
                Toast.makeText(ProfileUser.this, "Mot de passe actuel requis", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential1 = EmailAuthProvider.getCredential(user.getEmail(), currentPassword1);
            user.reauthenticate(credential1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        changePassword1(newPassword1);
                    } else {
                        progressDialog1.dismiss();
                        Toast.makeText(ProfileUser.this, "Réauthentification échouée", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        alertDialogBuilder.show();
    }

    private void changePassword1(String newPassword1) {
        user.updatePassword(newPassword1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    progressDialog1.dismiss();
                    Toast.makeText(getApplicationContext(), "Mot de passe mis à jour avec succès!", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog1.dismiss();
                    Toast.makeText(getApplicationContext(), "Échec de la mise à jour du mot de passe!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openFileChooser1() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImage1() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            StorageReference storageRef = storage.getReference().child("user_images/" + userId);

            storageRef.putFile(imageUri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl1 = uri.toString();
                                db.collection("Users").document(userId).update("imageUrl", imageUrl1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileUser.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileUser.this, "Error updating image URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(ProfileUser.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainEtudiant.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}