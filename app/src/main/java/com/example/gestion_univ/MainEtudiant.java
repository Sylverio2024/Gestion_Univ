package com.example.gestion_univ;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainEtudiant extends AppCompatActivity {
    private DrawerLayout drawerLayout1;
    private ImageButton buttonDrawerToggle1;
    private NavigationView navigationView1;
    private FirebaseAuth mAuth1;
    private FirebaseUser user1;
    private FirebaseFirestore db1;
    private ImageView imageLarge1;
    private ProgressDialog progressDialog1;
    private boolean isDataLoaded1 = false;
    //TextView DateCours, HeureCours;


    //cours
    RecyclerView recyclerViewH1;
    List<DataClass2> dataList2;
    DatabaseReference databaseReference;
    ValueEventListener eventListener2;
    AdapterCours adapter2;
    SearchView searchViewH1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_etudiant);

        // Initialisation des vues
        drawerLayout1 = findViewById(R.id.main1);
        buttonDrawerToggle1 = findViewById(R.id.buttonDrawerToggle1);
        navigationView1 = findViewById(R.id.navigationView1);
        View headerView = navigationView1.getHeaderView(0);
        imageLarge1 = headerView.findViewById(R.id.viewPhoto1);

        // Initialisation Firebase
        mAuth1 = FirebaseAuth.getInstance();
        db1 = FirebaseFirestore.getInstance();
        user1 = mAuth1.getCurrentUser();

        recyclerViewH1=findViewById(R.id.recyclerViewH1);
        searchViewH1=findViewById(R.id.searchH1);
        searchViewH1.clearFocus();

        // ProgressDialog pour indiquer le chargement
      /*  progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Chargement des données...");
        progressDialog1.setCancelable(false);
        progressDialog1.show();*/

        // Mise à jour de l'en-tête de navigation
        updateNavheader1();

        //Affichage du cours avec chargement
        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainEtudiant.this,1);
        recyclerViewH1.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(MainEtudiant.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();
        Button bntQ = dialog.findViewById(R.id.btnQuitterDialog);
        bntQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dataList2=new ArrayList<>();
        adapter2=new AdapterCours(MainEtudiant.this,dataList2);
        recyclerViewH1.setAdapter(adapter2);

        databaseReference= FirebaseDatabase.getInstance().getReference("Cours");
        dialog.show();

        eventListener2=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass2 dataClass2=itemSnapshot.getValue(DataClass2.class);
                    dataClass2.setKey2(itemSnapshot.getKey());
                    dataList2.add(dataClass2);
                }
                adapter2.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchViewH1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList2(newText);
                return true;
            }
        });

        // Gestion du clic sur l'image de profil
        imageLarge1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileImageLarge1();
            }
        });
        // Gestion du bouton de navigation
        buttonDrawerToggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout1.open();
            }
        });
// Gestion des éléments du menu de NavigationView
        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isDataLoaded1) {
                    handleMenuItemClick1(item);
                } else {
                    Toast.makeText(MainEtudiant.this, "Veuillez patienter jusqu'à ce que les données soient chargées.", Toast.LENGTH_SHORT).show();
                }
                drawerLayout1.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void handleMenuItemClick1(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.Cours1) {
            Toast.makeText(MainEtudiant.this, "Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainEtudiant.this, MainEtudiant.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.Event1) {
            Toast.makeText(MainEtudiant.this, "Evenement", Toast.LENGTH_SHORT).show();
        }  else if (itemId == R.id.About1) {
            Toast.makeText(MainEtudiant.this, "A propos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainEtudiant.this, ProfileUser.class);
            startActivity(intent);
            finish();
        }else if (itemId == R.id.Lgout1) {
            showConfirmationDialog1();
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout1.isDrawerOpen(GravityCompat.START)) {
            drawerLayout1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void showConfirmationDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Êtes-vous sûr de vouloir effectuer cette action ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth1.signOut();
                Intent intent = new Intent(MainEtudiant.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Non", null);
        builder.show();
    }
    public void updateNavheader1() {
        View headerView = navigationView1.getHeaderView(0);
        TextView emailView = headerView.findViewById(R.id.viewEmail1);
        ImageView photoView = headerView.findViewById(R.id.viewPhoto1);
        TextView username = headerView.findViewById(R.id.viewUsername1);

        if (user1 != null) {
            String userId = user1.getUid();
            db1.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            emailView.setText(document.getString("Email"));
                            username.setText(document.getString("NomComplet"));
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(MainEtudiant.this).load(imageUrl).transform(new CircleCrop()).into(photoView);
                            }
                            isDataLoaded1 = true;
                            //progressDialog1.dismiss();
                        }
                    } else {
                        Log.d("MainEtudiant", "get failed with ", task.getException());
                      //  progressDialog1.dismiss();
                    }
                }
            });
        } else {
           // progressDialog1.dismiss();
        }
    }
    private void showProfileImageLarge1() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_image);
        ImageView imageViewlarge = dialog.findViewById(R.id.imageViewLarge);

        if (user1 != null) {
            String userId = user1.getUid();
            db1.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String imageUrl = document.getString("imageUrl");
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(MainEtudiant.this).load(imageUrl).into(imageViewlarge);
                            }
                        }
                    } else {
                        Log.d("ProfileAdmin", "get failed with ", task.getException());
                    }
                }
            });
        }

        dialog.show();
    }
    public void searchList2(String text){
        ArrayList<DataClass2> searchList2 = new ArrayList<>();
        for(DataClass2 dataClass2:dataList2){
            if(dataClass2.getNumeroCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getNameCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getParcoursCours().toLowerCase().contains(text.toLowerCase()) || dataClass2.getNiveauCours().toLowerCase().contains(text.toLowerCase())){
                searchList2.add(dataClass2);
            }
        }
        adapter2.searchDataList2(searchList2);
    }
}