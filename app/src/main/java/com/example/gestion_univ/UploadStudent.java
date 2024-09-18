package com.example.gestion_univ;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;

public class UploadStudent extends AppCompatActivity {
    ImageView updloadImage1;
    Button bntSaveS;

    EditText txtNumeroID1, txtNum_inscriptionS, txtNomS,txtPrenomS, txtDate_naissanceS, txtAdresseS, txtMentionS, txtParcoursS,txtNiveauS,txtTelephoneS;
    private ScrollView scrollView1;
    Uri uri1;
    String imageUrl1;
    AlertDialog dialog;
    DatabaseReference etudiantReference;
    DatabaseReference qrdataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        updloadImage1 = findViewById(R.id.updloadImage1);
        bntSaveS = findViewById(R.id.bntSaveS);
        txtNumeroID1 = findViewById(R.id.txtNumeroID1);
        txtNum_inscriptionS = findViewById(R.id.txtNum_inscriptionS);
        txtNomS = findViewById(R.id.txtNomS);
        txtPrenomS = findViewById(R.id.txtPrenomS);
        txtDate_naissanceS = findViewById(R.id.txtDate_naissanceS);
        txtAdresseS = findViewById(R.id.txtAdresseS);
        txtMentionS = findViewById(R.id.txtMentionS);
        txtParcoursS = findViewById(R.id.txtParcoursS);
        txtNiveauS = findViewById(R.id.txtNiveauS);
        txtTelephoneS = findViewById(R.id.txtTelephoneS);
        scrollView1 = findViewById(R.id.scrollView1);

        etudiantReference = FirebaseDatabase.getInstance().getReference("Etudiants");
        qrdataReference = FirebaseDatabase.getInstance().getReference("qrdata");

        checkPermission();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri1 = data.getData();
                                if (uri1 != null) {
                                    Log.d("ImageURI", uri1.toString());
                                    Glide.with(UploadStudent.this).load(uri1).into(updloadImage1);
                                } else {
                                    Log.d("ImageURI", "URI is null");
                                    Toast.makeText(UploadStudent.this, "URI is null", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("ImageURI", "Intent data is null");
                                Toast.makeText(UploadStudent.this, "Intent data is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("ImageURI", "Result code not OK");
                            Toast.makeText(UploadStudent.this, "Aucun image sélectionée", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        scrollView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                scrollView1.getWindowVisibleDisplayFrame(r);
                int screenHeight = scrollView1.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    scrollView1.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView1.smoothScrollTo(0, txtTelephoneS.getBottom());
                        }
                    });
                }
            }
        });
        updloadImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        bntSaveS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NumeroIDE = txtNumeroID1.getText().toString();
                String Num_inscriptionS = txtNum_inscriptionS.getText().toString();
                String NomS = txtNomS.getText().toString();
                String PrenomS = txtPrenomS.getText().toString();
                String Date_naissanceS = txtDate_naissanceS.getText().toString();
                String AdresseS = txtAdresseS.getText().toString();
                String MentionS = txtMentionS.getText().toString();
                String ParcoursS = txtParcoursS.getText().toString();
                String NiveauS = txtNiveauS.getText().toString();
                String TelephoneS = txtTelephoneS.getText().toString();

                if (uri1 == null) {
                    Toast.makeText(UploadStudent.this, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
                } else if (NumeroIDE.isEmpty() || Num_inscriptionS.isEmpty() || NomS.isEmpty() || PrenomS.isEmpty() || Date_naissanceS.isEmpty() || AdresseS.isEmpty() || MentionS.isEmpty() || ParcoursS.isEmpty()|| NiveauS.isEmpty()|| TelephoneS.isEmpty()) {
                    Toast.makeText(UploadStudent.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.PHONE.matcher(TelephoneS).matches()) {
                    txtTelephoneS.setError("Entrez un bon numéro");
                } else {
                    // Vérifier si le numéro d'inscription existe déjà
                    checkIfNumeroIDOrNumInscriptionExists(NumeroIDE, Num_inscriptionS);
                }
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Nouvelle méthode pour vérifier à la fois le numeroID et le numInscription
    private void checkIfNumeroIDOrNumInscriptionExists(final String numeroIDE, final String numInscription) {
        etudiantReference.orderByChild("numeroIDE").equalTo(numeroIDE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(UploadStudent.this, "Le numéro ID existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    // Si le numeroID n'existe pas, on vérifie le numInscription
                    etudiantReference.orderByChild("numInscriptionE").equalTo(numInscription).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(UploadStudent.this, "Le numéro d'inscription existe déjà", Toast.LENGTH_SHORT).show();
                            } else {
                                // Si ni le numeroID ni le numInscription n'existent, on peut enregistrer
                                saveData();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(UploadStudent.this, "Erreur de vérification du numéro d'inscription", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadStudent.this, "Erreur de vérification du numéro ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri1.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadStudent.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button bntQ = dialogView.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        storageReference.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask1 = taskSnapshot.getStorage().getDownloadUrl();
                uriTask1.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage1 = task.getResult();
                            imageUrl1 = urlImage1.toString();
                            saveDataToFirebase();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(UploadStudent.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(UploadStudent.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveDataToFirebase() {
        String numeroIDE = txtNumeroID1.getText().toString();
        String numInscriptionE = txtNum_inscriptionS.getText().toString();
        String nameE = txtNomS.getText().toString();
        String prenomE = txtPrenomS.getText().toString();
        String dateNaissanceE = txtDate_naissanceS.getText().toString();
        String adresseE = txtAdresseS.getText().toString();
        String mentionE = txtMentionS.getText().toString();
        String parcoursE = txtParcoursS.getText().toString();
        String niveauE = txtNiveauS.getText().toString();
        String telephoneE = txtTelephoneS.getText().toString();

        DataClass1 dataClass1 = new DataClass1(numeroIDE, numInscriptionE, nameE, prenomE, mentionE, parcoursE , niveauE, dateNaissanceE, adresseE, telephoneE, imageUrl1);

        String currentDate = String.valueOf(System.currentTimeMillis());
        etudiantReference.child(currentDate).setValue(dataClass1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(UploadStudent.this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadStudent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    private String generateQRCode(String data) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, fnEtudiant.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}