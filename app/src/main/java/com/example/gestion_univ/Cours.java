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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cours extends AppCompatActivity {

    ImageButton buttonRetourE;
    FloatingActionButton fabCours;
    RecyclerView recyclerView2;
    List<DataClass2> dataList2,filteredDataList2;
    DatabaseReference databaseReference;
    ValueEventListener eventListener2;
    MyAdapter2 adapter2;
    SearchView searchView2;
    AlertDialog dialog;
    SwipeRefreshLayout swipeRefreshLayoutCours; // SwipeRefreshLayout ajouté


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cours);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonRetourE = findViewById(R.id.BackTeach);
        fabCours = findViewById(R.id.fabCours);
        recyclerView2 = findViewById(R.id.recyclerViewCours);
        swipeRefreshLayoutCours = findViewById(R.id.swipeRefreshLayoutCours); // Initialisation du SwipeRefreshLayout
        searchView2 = findViewById(R.id.searchCours);
        searchView2.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Cours.this, 1);
        recyclerView2.setLayoutManager(gridLayoutManager);

        // Setup loading dialog
        setupLoadingDialog();

        dataList2 = new ArrayList<>();
        filteredDataList2 = new ArrayList<>();
        adapter2 = new MyAdapter2(Cours.this, filteredDataList2);
        recyclerView2.setAdapter(adapter2);

        databaseReference = FirebaseDatabase.getInstance().getReference("Cours");
        // Load data initially
        loadData();

        // SwipeRefresh setup
        swipeRefreshLayoutCours.setOnRefreshListener(this::loadData);

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cours.this, fn5.class);
                startActivity(intent);
                finish();
            }
        });

        fabCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cours.this, UploadCours.class);
                startActivity(intent);
            }
        });
    }
    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Cours.this);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();

        Button buttonQuitter = dialogView.findViewById(R.id.btnQuitterDialog);
        buttonQuitter.setOnClickListener(v -> dialog.dismiss());
    }

    private void filterData(String query) {
        String lowerCaseQuery = query.toLowerCase();
        filteredDataList2.clear();

        for (DataClass2 dataClass2 : dataList2) {
            if (dataClass2.getNumeroCours().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass2.getNameCours().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass2.getParcoursCours().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass2.getNiveauCours().toLowerCase().contains(lowerCaseQuery) ||
                    dataClass2.getDescriptionCours().toLowerCase().contains(lowerCaseQuery)) {
                filteredDataList2.add(dataClass2);
            }
        }
        adapter2.notifyDataSetChanged();  // Update RecyclerView with filtered data
    }

    private void loadData() {
        dialog.show();  // Show the loading dialog when fetching data

        eventListener2 = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();  // Clear the existing list before updating
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass2 dataClass2 = itemSnapshot.getValue(DataClass2.class);
                    if (dataClass2 != null) {
                        dataClass2.setKey2(itemSnapshot.getKey());
                        dataList2.add(dataClass2);
                    }
                }
                filteredDataList2.clear();  // Update the filtered list
                filteredDataList2.addAll(dataList2);

                adapter2.notifyDataSetChanged();  // Notify adapter about data change
                dialog.dismiss();
                swipeRefreshLayoutCours.setRefreshing(false);  // Stop refreshing animation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fnEnseignant", "Error fetching data", error.toException());
                dialog.dismiss();
                swipeRefreshLayoutCours.setRefreshing(false);  // Stop refreshing even in case of error
            }
        });
    }
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