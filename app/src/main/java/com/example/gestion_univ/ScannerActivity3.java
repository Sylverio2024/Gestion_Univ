package com.example.gestion_univ;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity3 extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView2;
    private DatabaseReference dbref;
    private TextView instructionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView2 = new ZXingScannerView(this);

        instructionTextView = new TextView(this);
        instructionTextView.setText("Scanner l'ancien QR code dans la zone de scan");
        instructionTextView.setTextSize(18);
        instructionTextView.setTextColor(Color.WHITE);
        instructionTextView.setBackgroundColor(Color.BLACK);
        instructionTextView.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams textViewParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );
        instructionTextView.setLayoutParams(textViewParams);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(scannerView2);
        frameLayout.addView(instructionTextView);

        setContentView(frameLayout);

        dbref = FirebaseDatabase.getInstance().getReference("qrdata");

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        initializeCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // Handle permission denied case
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void initializeCamera() {
        try {
            scannerView2.setAutoFocus(true);
            scannerView2.setAspectTolerance(0.5f);

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
                scannerView2.startCamera(cameraId);
            } else {
                scannerView2.startCamera();
            }
            scannerView2.setResultHandler(this);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'initialisation de la caméra", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        final String scannedData = rawResult.getText().toString();

        dbref.orderByValue().equalTo(scannedData).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ScannerActivity3.this, "Veuillez scanner le nouveau QR code", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ScannerActivity2.class));
                                    finish();
                                } else {
                                    Toast.makeText(ScannerActivity3.this, "Erreur lors de la suppression du QR code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(ScannerActivity3.this, "Ce QR code n'est pas connu par le système ou n'est pas un ancien code", Toast.LENGTH_SHORT).show();
                    scannerView2.resumeCameraPreview(ScannerActivity3.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScannerActivity3.this, "Erreur de la base de données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView2.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeCamera();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}