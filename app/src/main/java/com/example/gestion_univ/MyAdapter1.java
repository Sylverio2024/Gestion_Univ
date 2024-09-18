package com.example.gestion_univ;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter1 extends RecyclerView.Adapter<MyViewHolder1> {

    private Context context1;
    private List<DataClass1> dataList1;

    public MyAdapter1(Context context1, List<DataClass1> dataList1) {
        this.context1 = context1;
        this.dataList1 = dataList1;
    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item1, parent, false);
        return new MyViewHolder1(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        Glide.with(context1).load(dataList1.get(position).getImageE()).into(holder.recImage1);
        holder.NumeroIDE.setText(dataList1.get(position).getNumeroIDE());
        holder.num_inscriptionE.setText(dataList1.get(position).getNumInscriptionE());
        holder.nomE.setText(dataList1.get(position).getNameE());
        holder.prenomE.setText(dataList1.get(position).getPrenomE());
        holder.mentionE.setText(dataList1.get(position).getMentionE());
        holder.parcoursE.setText(dataList1.get(position).getParcoursE());
        holder.niveauE.setText(dataList1.get(position).getNiveauE());
        holder.dateE.setText(dataList1.get(position).getDateNaissanceE());
        holder.adresseE.setText(dataList1.get(position).getAdresseE());
        holder.telephoneE.setText(dataList1.get(position).getTelephoneE());
        holder.recCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, DetailActivity1.class);
                intent.putExtra("ImageE",dataList1.get(holder.getAdapterPosition()).getImageE());
                intent.putExtra("NumeroIDE",dataList1.get(holder.getAdapterPosition()).getNumeroIDE());
                intent.putExtra("Num_inscriptionE",dataList1.get(holder.getAdapterPosition()).getNumInscriptionE());
                intent.putExtra("NomE",dataList1.get(holder.getAdapterPosition()).getNameE());
                intent.putExtra("PrenomE",dataList1.get(holder.getAdapterPosition()).getPrenomE());
                intent.putExtra("mentionE",dataList1.get(holder.getAdapterPosition()).getMentionE());
                intent.putExtra("parcoursE",dataList1.get(holder.getAdapterPosition()).getParcoursE());
                intent.putExtra("niveauE",dataList1.get(holder.getAdapterPosition()).getNiveauE());
                intent.putExtra("dateE",dataList1.get(holder.getAdapterPosition()).getDateNaissanceE());
                intent.putExtra("adresseE",dataList1.get(holder.getAdapterPosition()).getAdresseE());
                intent.putExtra("telephoneE",dataList1.get(holder.getAdapterPosition()).getTelephoneE());
                intent.putExtra("key1",dataList1.get(holder.getAdapterPosition()).getKey1());

                context1.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList1.size();
    }
    public void  searchDataList1(ArrayList<DataClass1>searchList1){
        dataList1=searchList1;
        notifyDataSetChanged();
    }
}
class MyViewHolder1 extends RecyclerView.ViewHolder{
    ImageView recImage1;
    TextView NumeroIDE,num_inscriptionE,nomE,prenomE,mentionE,parcoursE,niveauE,dateE,adresseE,telephoneE;
    CardView recCard1;
    public MyViewHolder1(@NonNull View itemView){
        super(itemView);


        recImage1=itemView.findViewById(R.id.recImage1);

        recCard1=itemView.findViewById(R.id.recCard1);

        NumeroIDE=itemView.findViewById(R.id.NumeroIDE);
        num_inscriptionE=itemView.findViewById(R.id.num_inscriptionE);
        nomE=itemView.findViewById(R.id.nomE);
        prenomE=itemView.findViewById(R.id.prenomE);
        mentionE=itemView.findViewById(R.id.mentionE);
        parcoursE=itemView.findViewById(R.id.parcoursE);
        niveauE=itemView.findViewById(R.id.niveauE);
        dateE=itemView.findViewById(R.id.dateE);
        adresseE=itemView.findViewById(R.id.adresseE);
        telephoneE=itemView.findViewById(R.id.telephoneE);

    }
}