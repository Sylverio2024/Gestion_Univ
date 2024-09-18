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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class UpdloadTeacher extends AppCompatActivity {
    ImageView updloadImage;
    Button saveButton;

    EditText txtNumeroIDT, txtNameT, txtPrenomT, txtSpecialiteT, txtAdresseT, txtCategorieT, txtTelephoneT;
    private ScrollView scrollView;
    Uri uri;
    String imageUrl;
    AlertDialog dialog;
    DatabaseReference enseignantReference;
    DatabaseReference qrdataReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updload_teacher);

        updloadImage = findViewById(R.id.updloadImage);
        saveButton = findViewById(R.id.bntSaveT);
        txtNumeroIDT = findViewById(R.id.txtNumeroID);
        txtNameT = findViewById(R.id.txtNameT);
        txtPrenomT = findViewById(R.id.txtPrenomT);
        txtSpecialiteT = findViewById(R.id.txtSpecialiteT);
        txtAdresseT = findViewById(R.id.txtAdresseT);
        txtCategorieT = findViewById(R.id.txtCategorieT);
        txtTelephoneT = findViewById(R.id.txtTelephoneT);
        scrollView = findViewById(R.id.scrollView);
        enseignantReference = FirebaseDatabase.getInstance().getReference("Enseignants");
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
                                uri = data.getData();
                                if (uri != null) {
                                    Log.d("ImageURI", uri.toString());
                                    Glide.with(UpdloadTeacher.this).load(uri).into(updloadImage);
                                } else {
                                    Log.d("ImageURI", "URI is null");
                                    Toast.makeText(UpdloadTeacher.this, "URI is null", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("ImageURI", "Intent data is null");
                                Toast.makeText(UpdloadTeacher.this, "Intent data is null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("ImageURI", "Result code not OK");
                            Toast.makeText(UpdloadTeacher.this, "Aucun image sélectionée", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                scrollView.getWindowVisibleDisplayFrame(r);
                int screenHeight = scrollView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.smoothScrollTo(0, txtTelephoneT.getBottom());
                        }
                    });
                }
            }
        });
        updloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NumeroIDT = txtNumeroIDT.getText().toString();
                String NameT = txtNameT.getText().toString();
                String PrenomT = txtPrenomT.getText().toString();
                String SpecialisteT = txtSpecialiteT.getText().toString();
                String AdresseT = txtAdresseT.getText().toString();
                String CategorieT = txtCategorieT.getText().toString();
                String TelephoneT = txtTelephoneT.getText().toString();

                if (uri == null) {
                    Toast.makeText(UpdloadTeacher.this, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
                } else if (NumeroIDT.isEmpty() || NameT.isEmpty() || PrenomT.isEmpty() || SpecialisteT.isEmpty() || AdresseT.isEmpty() || CategorieT.isEmpty() || TelephoneT.isEmpty()) {
                    Toast.makeText(UpdloadTeacher.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.PHONE.matcher(TelephoneT).matches()) {
                    txtTelephoneT.setError("Entrez un bon numéro");
                } else {
                    checkIfNumeroIDExistsAndSaveData(NumeroIDT);
                }
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    private void checkIfNumeroIDExistsAndSaveData(final String numeroID) {
        enseignantReference.orderByChild("numeroIDT").equalTo(numeroID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FirebaseCheck", "Checking for numeroID: " + numeroID);
                if (dataSnapshot.exists()) {
                    Log.d("FirebaseCheck", "NumeroID exists: " + numeroID);
                    Toast.makeText(UpdloadTeacher.this, "Le numéro ID existe déjà", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("FirebaseCheck", "NumeroID does not exist, proceeding to save data");
                    saveData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseCheck", "Error checking numeroID", databaseError.toException());
                Toast.makeText(UpdloadTeacher.this, "Erreur de vérification de l'ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdloadTeacher.this);
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

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage = task.getResult();
                            imageUrl = urlImage.toString();
                            saveDataToFirebase();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(UpdloadTeacher.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(UpdloadTeacher.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveDataToFirebase() {
        String numeroIDT = txtNumeroIDT.getText().toString();
        String nameT = txtNameT.getText().toString();
        String prenomT = txtPrenomT.getText().toString();
        String specialisteT = txtSpecialiteT.getText().toString();
        String adresseT = txtAdresseT.getText().toString();
        String categorieT = txtCategorieT.getText().toString();
        String telephoneT = txtTelephoneT.getText().toString();

            DataClass dataClass = new DataClass(numeroIDT, nameT, prenomT, specialisteT, adresseT, categorieT, telephoneT, imageUrl);

            String currentDate = String.valueOf(System.currentTimeMillis());
            enseignantReference.child(currentDate).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(UpdloadTeacher.this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdloadTeacher.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(this, fnEnseignant.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}