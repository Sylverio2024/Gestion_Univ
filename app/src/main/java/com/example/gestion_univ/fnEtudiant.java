package com.example.gestion_univ;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    List<DataClass1> dataList1;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter1 adapter1;
    SearchView searchView1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fn_etudiant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView1=findViewById(R.id.recyclerView1);
        fab1=findViewById(R.id.fab1);
        searchView1=findViewById(R.id.search1);
        searchView1.clearFocus();
        buttonRetourET=findViewById(R.id.BackTeach);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(fnEtudiant.this,1);
        recyclerView1.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(fnEtudiant.this);
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
        dataList1=new ArrayList<>();

        adapter1= new MyAdapter1(fnEtudiant.this,dataList1);
        recyclerView1.setAdapter(adapter1);

        databaseReference= FirebaseDatabase.getInstance().getReference("Etudiants");
        dialog.show();

        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList1.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass1 dataClass1=itemSnapshot.getValue(DataClass1.class);
                    dataClass1.setKey1(itemSnapshot.getKey());
                    dataList1.add(dataClass1);
                }
                adapter1.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList1(newText);
                return true;
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( fnEtudiant.this, UploadStudent.class);
                startActivity(intent);
                //finish();
            }
        });
        buttonRetourET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( fnEtudiant.this, fn5.class);
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
    public void searchList1(String text){
        ArrayList<DataClass1> searchList1 = new ArrayList<>();
        for(DataClass1 dataClass1:dataList1){
            if(dataClass1.getNumeroIDE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getNumInscriptionE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getNameE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getPrenomE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getMentionE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getParcoursE().toLowerCase().contains(text.toLowerCase()) || dataClass1.getNiveauE().toLowerCase().contains(text.toLowerCase())){
                searchList1.add(dataClass1);
            }
        }
        adapter1.searchDataList1(searchList1);
    }
}