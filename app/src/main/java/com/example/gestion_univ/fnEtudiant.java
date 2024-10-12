package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout; // Import du SwipeRefreshLayout

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fnEtudiant extends AppCompatActivity {
    ImageButton buttonRetourET;
    FloatingActionButton fab1;
    RecyclerView recyclerView1;
    List<DataClass1> dataList1, filteredDataList1;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter1 adapter1;
    SearchView searchView1;
    AlertDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout; // Ajout du SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn_etudiant);

        // Initialisation du SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout1); // Assurez-vous que le layout est ajouté dans le fichier XML

        recyclerView1 = findViewById(R.id.recyclerView1);
        fab1 = findViewById(R.id.fab1);
        searchView1 = findViewById(R.id.search1);
        searchView1.clearFocus();
        buttonRetourET = findViewById(R.id.BackTeach);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(fnEtudiant.this, 1);
        recyclerView1.setLayoutManager(gridLayoutManager);

        // Setup loading dialog
        setupLoadingDialog();

        dataList1 = new ArrayList<>();
        filteredDataList1 = new ArrayList<>();
        adapter1 = new MyAdapter1(fnEtudiant.this, filteredDataList1);
        recyclerView1.setAdapter(adapter1);

        databaseReference = FirebaseDatabase.getInstance().getReference("Etudiants");

        // Load data initially
        loadData();

        // SwipeRefresh setup
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        fab1.setOnClickListener(v -> {
            Intent intent = new Intent(fnEtudiant.this, UploadStudent.class);
            startActivity(intent);
        });

        buttonRetourET.setOnClickListener(v -> {
            Intent intent = new Intent(fnEtudiant.this, fn5.class);
            startActivity(intent);
            finish();
        });

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });
    }
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fnEtudiant.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();

        Button buttonQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        buttonQuitter.setOnClickListener(v -> dialog.dismiss());
    }
    private void loadData() {
        dialog.show();  // Show the loading dialog when fetching data

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList1.clear();  // Clear the existing list before updating
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass1 dataClass1 = itemSnapshot.getValue(DataClass1.class);
                    if (dataClass1 != null) {
                        dataClass1.setKey1(itemSnapshot.getKey());
                        dataList1.add(dataClass1);
                    }
                }
                filteredDataList1.clear();  // Update the filtered list
                filteredDataList1.addAll(dataList1);

                adapter1.notifyDataSetChanged();  // Notify adapter about data change
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);  // Stop refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fnEnseignant", "Error fetching data", error.toException());
                dialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);  // Stop refreshing even in case of error
            }
        });
    }

    private void filterData(String query) {
        String lowerCaseQuery = query.toLowerCase();
        filteredDataList1.clear();

        for (DataClass1 dataClass1 : dataList1) {
            if (dataClass1.getNumeroIDE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getNumInscriptionE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getNameE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getPrenomE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getMentionE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getParcoursE().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass1.getNiveauE().toLowerCase().contains(lowerCaseQuery)) {
                filteredDataList1.add(dataClass1);
            }
        }
        adapter1.notifyDataSetChanged();  // Update RecyclerView with filtered data
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(this, fn5.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}