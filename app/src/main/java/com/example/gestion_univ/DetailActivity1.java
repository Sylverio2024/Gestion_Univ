package com.example.gestion_univ;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity1 extends AppCompatActivity {
    TextView detailNumeroID1, detailNumero_inscription, detailNom1, detailPrenom1, detailMention, detailParcours, detailNiveau, detailDate, detailAdresse1, detailTelephone1;
    ImageView detailImage1, petiteImageView1;
    Bitmap qrCodeBitmap1;
    FloatingActionButton deleteButton1, editButton1, printButton1;
    String key1 = "";
    String imageUrl1 = "";
    Uri newImageUri;
    AlertDialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        detailNumeroID1 = findViewById(R.id.detailNumeroID1);
        detailNumero_inscription = findViewById(R.id.detailNumero_inscription);
        detailNom1 = findViewById(R.id.detailNom1);
        detailPrenom1 = findViewById(R.id.detailPrenom1);
        detailMention = findViewById(R.id.detailMention);
        detailParcours = findViewById(R.id.detailParcours);
        detailNiveau = findViewById(R.id.detailNiveau);
        detailDate = findViewById(R.id.detailDate);
        detailAdresse1 = findViewById(R.id.detailAdresse1);
        detailDate = findViewById(R.id.detailDate);
        detailTelephone1 = findViewById(R.id.detailTelephone1);

        printButton1 = findViewById(R.id.printButton1);
        deleteButton1 = findViewById(R.id.deleteButton1);
        editButton1 = findViewById(R.id.editButton1);
        detailImage1 = findViewById(R.id.detailImage1);
        petiteImageView1 = findViewById(R.id.petiteImageView1);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailNumeroID1.setText(bundle.getString("NumeroIDE"));
            detailNumero_inscription.setText(bundle.getString("Num_inscriptionE"));
            detailNom1.setText(bundle.getString("NomE"));
            detailPrenom1.setText(bundle.getString("PrenomE"));
            detailMention.setText(bundle.getString("mentionE"));
            detailParcours.setText(bundle.getString("parcoursE"));
            detailNiveau.setText(bundle.getString("niveauE"));
            detailDate.setText(bundle.getString("dateE"));
            detailAdresse1.setText(bundle.getString("adresseE"));
            detailTelephone1.setText(bundle.getString("telephoneE"));
            key1 = bundle.getString("key1");
            imageUrl1 = bundle.getString("ImageE");
            Glide.with(this).load(bundle.getString("ImageE")).into(detailImage1);

            String qrContent1 = bundle.getString("NumeroIDE") + "\n" +
                    bundle.getString("Num_inscriptionE") + "\n" +
                    bundle.getString("NomE") + "\n" +
                    bundle.getString("PrenomE") + "\n" +
                    bundle.getString("mentionE") + "\n" +
                    bundle.getString("parcoursE") + "\n" +
                    bundle.getString("niveauE") + "\n" +
                    bundle.getString("dateE") + "\n" +
                    bundle.getString("adresseE") + "\n" +
                    bundle.getString("telephoneE");

             qrCodeBitmap1 = generateQRCode(qrContent1);
        }
        deleteButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Etudiants");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl1);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key1).removeValue();
                        Toast.makeText(DetailActivity1.this, "Supprimé", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), fnEtudiant.class));
                        finish();
                    }
                });
            }
        });

        printButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
        editButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity1.this, UpdateStudent.class)
                        .putExtra("NumeroIDE", detailNumeroID1.getText().toString())
                        .putExtra("Num_inscriptionE", detailNumero_inscription.getText().toString())
                        .putExtra("NomE", detailNom1.getText().toString())
                        .putExtra("PrenomE", detailPrenom1.getText().toString())
                        .putExtra("mentionE", detailMention.getText().toString())
                        .putExtra("parcoursE", detailParcours.getText().toString())
                        .putExtra("niveauE", detailNiveau.getText().toString())
                        .putExtra("dateE", detailDate.getText().toString())
                        .putExtra("adresseE", detailAdresse1.getText().toString())
                        .putExtra("telephoneE", detailTelephone1.getText().toString())
                        .putExtra("ImageE", imageUrl1)
                        .putExtra("key1", key1);
                startActivity(intent);
            }
        });
        petiteImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity1.this, photoEtudiant.class)
                        .putExtra("ImageE", imageUrl1);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            newImageUri = data.getData();
            if (newImageUri != null) {
                // Mettre à jour l'image dans l'ImageView
                Glide.with(this).load(newImageUri).into(detailImage1);
                // Supprimer l'ancienne image et télécharger la nouvelle
                deleteAndUploadNewImage(newImageUri);
            }
        }
    }

    private void deleteAndUploadNewImage(Uri imageUri1) {
        // Afficher le dialog de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity1.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog1 = builder.create();
        dialog1.show();

        Button btnQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        btnQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Toast.makeText(DetailActivity1.this, "Chargement annulé", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference oldImageRef = storage.getReferenceFromUrl(imageUrl1);

        oldImageRef.delete().addOnSuccessListener(aVoid -> {
            StorageReference newImageRef = FirebaseStorage.getInstance().getReference().child("Android Images").child(imageUri1.getLastPathSegment());
            newImageRef.putFile(imageUri1).addOnSuccessListener(taskSnapshot -> newImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Etudiants").child(key1);
                databaseReference.child("imageE").setValue(uri.toString());
                imageUrl1 = uri.toString(); // Mettre à jour l'URL de l'image
                Toast.makeText(DetailActivity1.this, "Image mise à jour avec succès", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailActivity1.this, fnEtudiant.class);
                startActivity(intent);
                finish();
                dialog1.dismiss(); // Cacher le dialog de chargement
            }).addOnFailureListener(e -> {
                Toast.makeText(DetailActivity1.this, "Échec de la mise à jour de l'image", Toast.LENGTH_SHORT).show();
                dialog1.dismiss(); // Cacher le dialog de chargement
            })).addOnFailureListener(e -> {
                Toast.makeText(DetailActivity1.this, "Échec du téléchargement de la nouvelle image", Toast.LENGTH_SHORT).show();
                dialog1.dismiss(); // Cacher le dialog de chargement
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(DetailActivity1.this, "Échec de la suppression de l'ancienne image", Toast.LENGTH_SHORT).show();
            dialog1.dismiss(); // Cacher le dialog de chargement
        });
    }


    private Bitmap generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);
            com.google.zxing.common.BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400);
            for (int x = 0; x < 400; x++) {
                for (int y = 0; y < 400; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createPdf() {
        PdfDocument document = new PdfDocument();

        // Dimensions de la page
        int pageWidth = 595;
        int pageHeight = 842;
        int padding = 16;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Paint linePaint = new Paint();
        linePaint.setColor(android.graphics.Color.BLACK);
        linePaint.setStrokeWidth(2);

        // Chargement et dessin du logo en haut à droite
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.isstm_logo); // Assurez-vous que le logo est dans le dossier drawable
        float logoWidth = 100; // Ajustez la taille selon vos besoins
        float logoHeight = logoBitmap.getHeight() * (logoWidth / logoBitmap.getWidth());

        // Positionnement du logo en haut à droite
        float logoX = pageWidth - padding - logoWidth;
        float logoY = padding;
        Rect logoSrcRect = new Rect(0, 0, logoBitmap.getWidth(), logoBitmap.getHeight());
        Rect logoDestRect = new Rect((int) logoX, (int) logoY, (int) (logoX + logoWidth), (int) (logoY + logoHeight));
        canvas.drawBitmap(logoBitmap, logoSrcRect, logoDestRect, paint);

        // Dessin de l'image en haut
        Bitmap bitmap = ((BitmapDrawable) detailImage1.getDrawable()).getBitmap();
        float imageWidth = 200;
        float imageHeight = bitmap.getHeight() * (imageWidth / bitmap.getWidth());

        // Redimensionner l'image si nécessaire
        if (imageHeight > pageHeight / 3) {
            imageHeight = pageHeight / 3;
            imageWidth = bitmap.getWidth() * (imageHeight / bitmap.getHeight());
        }

        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect destRect = new Rect(padding, padding, padding + (int) imageWidth, padding + (int) imageHeight);
        canvas.drawBitmap(bitmap, srcRect, destRect, paint);

        // Déplacement après l'image
        int y = padding + (int) imageHeight + padding;

        // Dessin des informations dans un tableau
        String[] labels = {"NuméroID:", "N° Inscription:", "Nom:", "Prénom:", "Mention:", "Parcours:", "Niveau:", "Date naissance:", "Adresse:", "Téléphone:"};
        String[] values = {detailNumeroID1.getText().toString(), detailNumero_inscription.getText().toString(), detailNom1.getText().toString(),
                detailPrenom1.getText().toString(), detailMention.getText().toString(), detailParcours.getText().toString(), detailNiveau.getText().toString(),
                detailDate.getText().toString(), detailAdresse1.getText().toString(), detailTelephone1.getText().toString()};

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        for (int i = 0; i < labels.length; i++) {
            canvas.drawText(labels[i], padding, y, paint);
            canvas.drawText(values[i], padding + 150, y, paint);
            canvas.drawLine(padding, y + 5, pageWidth - padding, y + 5, linePaint);
            y += paint.descent() - paint.ascent() + padding;
        }

        // Ajout d'une marge après le tableau pour éviter la confusion avec le QR code
        y += padding;

        // Vérification si le QR code tient encore dans la page
        float qrSize = 200;
        if (y + qrSize > pageHeight - padding - 30) { // 30 est une marge pour l'année universitaire
            qrSize = pageHeight - padding - 30 - y;
        }

        // Dessin du QR code en bas, après le tableau
        if (qrCodeBitmap1 != null) {
            int qrX = (pageWidth - (int) qrSize) / 2;
            int qrY = y;
            Rect qrDestRect = new Rect(qrX, qrY, qrX + (int) qrSize, qrY + (int) qrSize);
            canvas.drawBitmap(qrCodeBitmap1, null, qrDestRect, paint);
        }

        // Ajout de la date universitaire dans le pied de page
        paint.setTextSize(14);
        paint.setColor(Color.BLACK);
        String anneeUniversitaire = "Année universitaire : " + getAnneeUniversitaire();
        float textWidth = paint.measureText(anneeUniversitaire);
        canvas.drawText(anneeUniversitaire, pageWidth - textWidth - padding, pageHeight - padding, paint);

        document.finishPage(page);

        // Enregistrement du document PDF
        String filePath = getExternalFilesDir(null) + "/details.pdf";
        try {
            document.writeTo(new FileOutputStream(filePath));
            Log.d("PDF", "PDF créé avec succès à " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        // Démarrage de l'activité d'impression
        printPdf(filePath);
    }

    // Méthode pour obtenir l'année universitaire actuelle
    private String getAnneeUniversitaire() {
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return year + "-" + (year + 1);
    }

    private void printPdf(String filePath) {
        PrintManager printManager = (PrintManager) this.getSystemService(PRINT_SERVICE);
        try {
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(this, filePath);
            printManager.print("Document", printAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            e.printStackTrace();
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