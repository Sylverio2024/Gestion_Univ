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

public class Evenement extends AppCompatActivity {
    ImageButton buttonRetourEvent;
    FloatingActionButton fabEvent;
    RecyclerView recyclerViewEvent;
    List<DataClass4> dataList4;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    MyAdapter4 adapter4;
    SearchView searchViewEvent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evenement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewEvent=findViewById(R.id.recyclerViewEvent);
        buttonRetourEvent=findViewById(R.id.BackTeach);
        fabEvent=findViewById(R.id.fabEvent);
        searchViewEvent=findViewById(R.id.searchEvent);
        searchViewEvent.clearFocus();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Evenement.this,1);
        recyclerViewEvent.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder=new AlertDialog.Builder(Evenement.this);
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
        dataList4=new ArrayList<>();

        adapter4=new MyAdapter4(Evenement.this,dataList4);
        recyclerViewEvent.setAdapter(adapter4);

        databaseReference= FirebaseDatabase.getInstance().getReference("Evenement");
        dialog.show();

        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList4.clear();
                for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClass4 dataClass4=itemSnapshot.getValue(DataClass4.class);
                    dataClass4.setKey4(itemSnapshot.getKey());
                    dataList4.add(dataClass4);
                }
                adapter4.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchViewEvent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList4(newText);
                return true;
            }
        });

        fabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Evenement.this, uploadEvent.class);
                startActivity(intent);
                //finish();
            }
        });
        buttonRetourEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent packageContext;
                Intent intent = new Intent( Evenement.this, fn5.class);
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
    public void searchList4(String text){
        ArrayList<DataClass4> searchList4 = new ArrayList<>();
        for(DataClass4 dataClass4:dataList4){
            if(dataClass4.getNumeroEvent().toLowerCase().contains(text.toLowerCase()) || dataClass4.getTitreEvent().toLowerCase().contains(text.toLowerCase()) || dataClass4.getDateEvent().toLowerCase().contains(text.toLowerCase())){
                searchList4.add(dataClass4);
            }
        }
        adapter4.searchDataList4(searchList4);
    }
}