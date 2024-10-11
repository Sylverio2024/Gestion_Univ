package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utilisateur extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User1> userList, filteredUserList;
    private FirebaseFirestore db;
    private ImageButton buttonRetourE;
    private FirebaseAuth auth;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayoutU;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur);

        recyclerView = findViewById(R.id.recyclerViewU);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        buttonRetourE = findViewById(R.id.BackTeach);
        searchView = findViewById(R.id.searchU);
        swipeRefreshLayoutU = findViewById(R.id.swipeRefreshLayoutU);
        String currentUserEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "";

        userAdapter = new UserAdapter(filteredUserList, this, currentUserEmail);
        recyclerView.setAdapter(userAdapter);

        fetchUsers();

        swipeRefreshLayoutU.setOnRefreshListener(() -> {
            // Recharger les données lors du balayage
            fetchUsers();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        buttonRetourE.setOnClickListener(v -> {
            Intent intent = new Intent(Utilisateur.this, fn5.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchUsers() {
        // Vider les listes avant de charger les nouvelles données
        userList.clear();
        filteredUserList.clear();

        db.collection("Users")
                .whereEqualTo("role", "utilisateur")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            User1 user1 = documentSnapshot.toObject(User1.class);
                            if (user1 != null) {
                                userList.add(user1);
                            }
                        }
                        filteredUserList.addAll(userList);  // Copier la liste des utilisateurs dans filteredUserList
                        userAdapter.notifyDataSetChanged();
                    }
                    swipeRefreshLayoutU.setRefreshing(false);  // Arrêter l'animation de rafraîchissement
                })
                .addOnFailureListener(e -> {
                    Log.e("Utilisateur", "Erreur lors de la récupération des utilisateurs", e);
                    swipeRefreshLayoutU.setRefreshing(false);  // Arrêter l'animation même en cas d'échec
                });
    }

    private void filterUsers(String query) {
        String lowerCaseQuery = query.toLowerCase(Locale.ROOT);
        filteredUserList.clear();

        for (User1 user : userList) {
            if (user.getNomComplet().toLowerCase(Locale.ROOT).contains(lowerCaseQuery) ||
                    user.getEmail().toLowerCase(Locale.ROOT).contains(lowerCaseQuery) ||
                    user.getTelephone().contains(lowerCaseQuery)) {
                filteredUserList.add(user);
            }
        }

        userAdapter.notifyDataSetChanged();
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