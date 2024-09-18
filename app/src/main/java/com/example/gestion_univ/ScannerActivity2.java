package com.example.gestion_univ;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class ScannerActivity2 extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView1;
    private DatabaseReference dbref;
    public static TextView instructionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ZXingScannerView
        scannerView1 = new ZXingScannerView(this);

        // Initialize TextView
        instructionTextView = new TextView(this);
        instructionTextView.setText("Scanner le QR code dans la zone de scan");
        instructionTextView.setTextSize(18);
        instructionTextView.setTextColor(Color.WHITE);
        instructionTextView.setBackgroundColor(Color.BLACK);
        instructionTextView.setGravity(Gravity.CENTER);

        // Create layout params for TextView to position it at the bottom
        FrameLayout.LayoutParams textViewParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );
        instructionTextView.setLayoutParams(textViewParams);

        // Create a FrameLayout and add both views
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.addView(scannerView1);
        frameLayout.addView(instructionTextView);

        // Set the FrameLayout as the content view
        setContentView(frameLayout);
        dbref = FirebaseDatabase.getInstance().getReference("qrdata");

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView1.startCamera();
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

    @Override
    public void handleResult(Result rawResult) {
        final String data1 = rawResult.getText().toString();

        dbref.orderByValue().equalTo(data1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // QR code already exists
                    showAlertDialog("QR code déjà existé", false);
                    scannerView1.resumeCameraPreview(ScannerActivity2.this);
                } else {
                    // QR code does not exist, save it
                    dbref.push().setValue(data1)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        showAlertDialog("QR code enregistré avec succès", true);


                                    } else {
                                        Toast.makeText(ScannerActivity2.this, "Erreur lors de l'enregistrement des données", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScannerActivity2.this, "Erreur de la base de données", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(String message, boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (success) {
                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    finish();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView1.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView1.setResultHandler(this);
        scannerView1.startCamera();
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