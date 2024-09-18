package com.example.gestion_univ;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.google.zxing.client.android.BeepManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private DatabaseReference dbref;
    private FirebaseAuth mAuth;
    private BeepManager beepManager;  // Pour émettre un bip après un scan réussi
    private boolean isDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        dbref = FirebaseDatabase.getInstance().getReference("qrdata");
        mAuth = FirebaseAuth.getInstance();
        beepManager = new BeepManager(this); // Initialiser le beep manager
        isDataFound = false;

        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        initializeCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        } else {
                            Toast.makeText(ScannerActivity.this, "La permission de la caméra est requise.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void initializeCamera() {
        try {
            scannerView.setAutoFocus(true); // Activer l'autofocus si possible
            scannerView.setAspectTolerance(0.5f); // Ajuster la tolérance de l'aspect pour améliorer la compatibilité

            int cameraId = -1;
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraId = i;
                    break;
                }
            }
            if (cameraId != -1) {
                scannerView.startCamera(cameraId);
            } else {
                scannerView.startCamera();
            }
            scannerView.setResultHandler(this);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'initialisation de la caméra", Toast.LENGTH_LONG).show();
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
        builder.setTitle("Permission requise")
                .setMessage("Vous devez autoriser l'accès à la caméra dans les paramètres.")
                .setPositiveButton("Paramètres", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void handleResult(Result rawResult) {
        beepManager.playBeepSoundAndVibrate(); // émettre un bip et vibrer après le scan
        String data = rawResult.getText();
        if (data != null && !data.isEmpty()) {
            checkDataInFirebase(data);
        } else {
            Toast.makeText(this, "QR Code invalide", Toast.LENGTH_SHORT).show();
            scannerView.resumeCameraPreview(this);
        }
    }

    private void checkDataInFirebase(String scannedData) {
        dbref.orderByValue().equalTo(scannedData).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isDataFound = true;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String data = snapshot.getValue(String.class);
                        showAlertDialog(data);
                    }
                } else {
                    Toast.makeText(ScannerActivity.this, "QR Code non trouvé dans la base de données", Toast.LENGTH_SHORT).show();
                    restartLoginActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScannerActivity.this, "Erreur de la base de données : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                restartLoginActivity();
            }
        });
    }

    private void showAlertDialog(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BIENVENU,ceci est votre information")
                .setMessage(data.replace(",", "\n"))
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(ScannerActivity.this, MainEtudiant.class);
                    intent.putExtra("qrData", data);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            scannerView.stopCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isDataFound) {
            mAuth.signOut();
            restartLoginActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initializeCamera();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'initialisation de la caméra", Toast.LENGTH_SHORT).show();
        }
    }

    private void restartLoginActivity() {
        Intent intent = new Intent(ScannerActivity.this, login.class);
        startActivity(intent);
        finish();
    }
}