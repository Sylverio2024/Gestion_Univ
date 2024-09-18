package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter adapter;
    SearchView searchView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn_enseignant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView=findViewById(R.id.recyclerView);
        buttonRetourE=findViewById(R.id.BackTeach);
        fab=findViewById(R.id.fab);
        searchView=findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(fnEnseignant.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(fnEnseignant.this);
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
        dataList=new ArrayList<>();

        adapter=new MyAdapter(fnEnseignant.this,dataList);
        recyclerView.setAdapter(adapter);

        databaseReference= FirebaseDatabase.getInstance().getReference("Enseignants");
        dialog.show();

        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass dataClass=itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( fnEnseignant.this, UpdloadTeacher.class);
                startActivity(intent);
                  //finish();
            }
        });
        buttonRetourE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( fnEnseignant.this, fn5.class);
                startActivity(intent);
                finish();

            }
        });

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
    public void searchList(String text){
        ArrayList<DataClass> searchList = new ArrayList<>();
        for(DataClass dataClass:dataList){
          if(dataClass.getNameT().toLowerCase().contains(text.toLowerCase()) || dataClass.getNumeroIDT().toLowerCase().contains(text.toLowerCase()) || dataClass.getPrenomT().toLowerCase().contains(text.toLowerCase())){
            searchList.add(dataClass);
          }
        }
        adapter.searchDataList(searchList);
    }
}