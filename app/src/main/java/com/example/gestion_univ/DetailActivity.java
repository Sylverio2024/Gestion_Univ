package com.example.gestion_univ;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
    TextView detailNumeroID, detailNom, detailPrenom, detailSpecialite, detailAdresse, detailCategorie, detailTelephone;
    ImageView detailImage, petiteImageView;
    Bitmap qrCodeBitmap;
    FloatingActionButton deleteButton, editButton, printButton;
    String key = "";
    String imageUrl = "";
    Uri newImageUri;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        detailNumeroID = findViewById(R.id.detailNumeroID);
        detailNom = findViewById(R.id.detailNom);
        detailPrenom = findViewById(R.id.detailPrenom);
        detailSpecialite = findViewById(R.id.detailSpecialite);
        detailAdresse = findViewById(R.id.detailAdresse);
        detailCategorie = findViewById(R.id.detailCategorie);
        detailTelephone = findViewById(R.id.detailTelephone);

        printButton = findViewById(R.id.printButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailImage = findViewById(R.id.detailImage);
        petiteImageView = findViewById(R.id.petiteImageView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailNumeroID.setText(bundle.getString("NumeroID"));
            detailNom.setText(bundle.getString("Nom"));
            detailPrenom.setText(bundle.getString("Prenom"));
            detailSpecialite.setText(bundle.getString("Specialite"));
            detailAdresse.setText(bundle.getString("Adresse"));
            detailCategorie.setText(bundle.getString("Categorie"));
            detailTelephone.setText(bundle.getString("Telephone"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);

            String qrContent = bundle.getString("NumeroID") + "\n" +
                    bundle.getString("Nom") + "\n" +
                    bundle.getString("Prenom") + "\n" +
                    bundle.getString("Specialite") + "\n" +
                    bundle.getString("Adresse") + "\n" +
                    bundle.getString("Categorie") + "\n" +
                    bundle.getString("Telephone");

            qrCodeBitmap = generateQRCode(qrContent);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Enseignants");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this, "Supprimé", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), fnEnseignant.class));
                        finish();
                    }
                });
            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPdf();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateTeacher.class)
                        .putExtra("NumeroID", detailNumeroID.getText().toString())
                        .putExtra("Nom", detailNom.getText().toString())
                        .putExtra("Prenom", detailPrenom.getText().toString())
                        .putExtra("Specialite", detailSpecialite.getText().toString())
                        .putExtra("Adresse", detailAdresse.getText().toString())
                        .putExtra("Categorie", detailCategorie.getText().toString())
                        .putExtra("Telephone", detailTelephone.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("key", key);
                startActivity(intent);
            }
        });
        petiteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, photoEnseignant.class)
                        .putExtra("Image", imageUrl)
                        .putExtra("key", key);
                startActivity(intent);
            }
        });
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            newImageUri = data.getData();
            if (newImageUri != null) {
                // Mettre à jour l'image dans l'ImageView
                Glide.with(this).load(newImageUri).into(detailImage);
                // Supprimer l'ancienne image et télécharger la nouvelle
                deleteAndUploadNewImage(newImageUri);
            }
        }
    }*/

  /*  private void deleteAndUploadNewImage(Uri imageUri) {
        // Afficher le dialog de chargement
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

        Button btnQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        btnQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(DetailActivity.this, "Chargement annulé", Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference oldImageRef = storage.getReferenceFromUrl(imageUrl);

        oldImageRef.delete().addOnSuccessListener(aVoid -> {
            StorageReference newImageRef = FirebaseStorage.getInstance().getReference().child("Android Images").child(imageUri.getLastPathSegment());
            newImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> newImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Enseignants").child(key);
                databaseReference.child("imageT").setValue(uri.toString());
                imageUrl = uri.toString(); // Mettre à jour l'URL de l'image
                Toast.makeText(DetailActivity.this, "Image mise à jour avec succès", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetailActivity.this, fnEnseignant.class);
                startActivity(intent);
                finish();
                dialog.dismiss(); // Cacher le dialog de chargement
            }).addOnFailureListener(e -> {
                Toast.makeText(DetailActivity.this, "Échec de la mise à jour de l'image", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Cacher le dialog de chargement
            })).addOnFailureListener(e -> {
                Toast.makeText(DetailActivity.this, "Échec du téléchargement de la nouvelle image", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Cacher le dialog de chargement
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(DetailActivity.this, "Échec de la suppression de l'ancienne image", Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Cacher le dialog de chargement
        });
    }*/


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

        // Dessin du logo
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.isstm_logo);
        float logoWidth = 100;
        float logoHeight = logoBitmap.getHeight() * (logoWidth / logoBitmap.getWidth());
        float logoX = pageWidth - padding - logoWidth;
        float logoY = padding;

        // Dessiner le logo
        Rect logoDestRect = new Rect((int) logoX, (int) logoY, (int) (logoX + logoWidth), (int) (logoY + logoHeight));
        canvas.drawBitmap(logoBitmap, null, logoDestRect, paint);

        // Dessin de l'image principale
        Bitmap bitmap = ((BitmapDrawable) detailImage.getDrawable()).getBitmap();
        float imageWidth = Math.min(200, pageWidth - 2 * padding);
        float imageHeight = bitmap.getHeight() * (imageWidth / bitmap.getWidth());

        // Redimensionnement si l'image est trop grande
        if (imageHeight > pageHeight / 3) {
            imageHeight = pageHeight / 3;
            imageWidth = bitmap.getWidth() * (imageHeight / bitmap.getHeight());
        }

        Rect destRect = new Rect(padding, (int) (logoY + logoHeight + padding), (int) (padding + imageWidth),
                (int) (logoY + logoHeight + imageHeight + padding));
        canvas.drawBitmap(bitmap, null, destRect, paint);

        // Positionnement du texte
        int y = (int) (logoY + logoHeight + imageHeight + 2 * padding); // Ajouter une marge supplémentaire

        // Dessin des informations
        String[] labels = {"NuméroID:", "Nom:", "Prénom:", "Spécialité:", "Adresse:", "Catégorie:", "Téléphone:"};
        String[] values = {
                detailNumeroID.getText().toString(),
                detailNom.getText().toString(),
                detailPrenom.getText().toString(),
                detailSpecialite.getText().toString(),
                detailAdresse.getText().toString(),
                detailCategorie.getText().toString(),
                detailTelephone.getText().toString()
        };

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        for (int i = 0; i < labels.length; i++) {
            canvas.drawText(labels[i], padding, y, paint);
            canvas.drawText(values[i], padding + 150, y, paint);
            canvas.drawLine(padding, y + 5, pageWidth - padding, y + 5, linePaint);
            y += paint.descent() - paint.ascent() + padding;
        }

        // Ajouter une marge pour le QR code
        y += padding;

        // Dessin du QR code
        float qrSize = 200;
        if (y + qrSize > pageHeight - padding - 30) {
            qrSize = pageHeight - padding - 30 - y;
        }

        if (qrCodeBitmap != null) {
            int qrX = (pageWidth - (int) qrSize) / 2;
            int qrY = y;
            Rect qrDestRect = new Rect(qrX, qrY, qrX + (int) qrSize, qrY + (int) qrSize);
            canvas.drawBitmap(qrCodeBitmap, null, qrDestRect, paint);
        }

        // Année universitaire
        paint.setTextSize(14);
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

        // Démarrer l'activité d'impression
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
            Intent intent = new Intent(this, fnEnseignant.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}