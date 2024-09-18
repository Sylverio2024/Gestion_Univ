package com.example.gestion_univ;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class photoEtudiant extends AppCompatActivity {
    ImageView btnCamS, btnGalS, imgChooseS;
    ImageButton btnRetourPDP;
    TextView btnsaveS;
    Uri imageUri1;
    AlertDialog loadingDialog1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_photo_etudiant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRetourPDP = findViewById(R.id.BackTeach);
        btnCamS = findViewById(R.id.btnCamS);
        btnGalS = findViewById(R.id.btnGalS);
        imgChooseS = findViewById(R.id.imgChooseS);
        btnsaveS = findViewById(R.id.btnSaveS);

        // Récupérer l'image passée depuis DetailActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imageUrl1 = bundle.getString("ImageE");
            if (imageUrl1 != null) {
                Glide.with(this).load(imageUrl1).into(imgChooseS);
            }
        }

        btnRetourPDP.setOnClickListener(v -> finish());

        btnsaveS.setOnClickListener(v -> {
            if (imageUri1 != null) {
                Intent resultIntent = new Intent();
                resultIntent.setData(imageUri1);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(photoEtudiant.this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
            }
        });

        btnGalS.setOnClickListener(v -> Dexter.withContext(getApplicationContext()).withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Sélectionner une image"), 1);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());

        btnCamS.setOnClickListener(v -> Dexter.withContext(getApplicationContext()).withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 && data != null) {
                imageUri1 = data.getData();
                Glide.with(this).load(imageUri1).into(imgChooseS);
            } else if (requestCode == 2 && data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageUri1 = getImageUri(bitmap);
                Glide.with(this).load(imageUri1).into(imgChooseS);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);

    }
    @Override
    public void onBackPressed() {

        if (isTaskRoot()) {
            Intent intent = new Intent(this, DetailActivity1.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}