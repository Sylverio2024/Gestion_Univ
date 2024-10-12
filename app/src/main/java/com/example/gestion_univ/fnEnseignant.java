package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class fnEnseignant extends AppCompatActivity {
    ImageButton buttonRetourE;
    RecyclerView recyclerView;
    List<DataClass> dataList, filteredDataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter adapter;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    AlertDialog dialog;
    FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fn_enseignant);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        buttonRetourE = findViewById(R.id.BackTeach);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        fab = findViewById(R.id.fab);

        // Setup RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(fnEnseignant.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Setup loading dialog
        setupLoadingDialog();

        dataList = new ArrayList<>();
        filteredDataList = new ArrayList<>();
        adapter = new MyAdapter(fnEnseignant.this, filteredDataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Enseignants");

        // Load data initially
        loadData();

        // SwipeRefresh setup
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(fnEnseignant.this, UpdloadTeacher.class);
            startActivity(intent);
        });

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        // Button to go back
        buttonRetourE.setOnClickListener(v -> {
            Intent intent = new Intent(fnEnseignant.this, fn5.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(fnEnseignant.this);
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
                dataList.clear();  // Clear the existing list before updating
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                filteredDataList.clear();  // Update the filtered list
                filteredDataList.addAll(dataList);

                adapter.notifyDataSetChanged();  // Notify adapter about data change
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
        filteredDataList.clear();

        for (DataClass data : dataList) {
            if (data.getNameT().toLowerCase().contains(lowerCaseQuery) ||
                    data.getNumeroIDT().toLowerCase().contains(lowerCaseQuery) ||
                    data.getPrenomT().toLowerCase().contains(lowerCaseQuery)) {
                filteredDataList.add(data);
            }
        }

        adapter.notifyDataSetChanged();  // Update RecyclerView with filtered data
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