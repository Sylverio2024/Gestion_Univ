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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getImageT()).into(holder.recImage);
        holder.NumeroID.setText(dataList.get(position).getNumeroIDT());
        holder.Nom.setText(dataList.get(position).getNameT());
        holder.Prenom.setText(dataList.get(position).getPrenomT());
        holder.Specialite.setText(dataList.get(position).getSpecialiteT());
        holder.Adresse.setText(dataList.get(position).getAdresseT());
        holder.Categorie.setText(dataList.get(position).getCategorieT());
        holder.Telephone.setText(dataList.get(position).getTelephoneT());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image",dataList.get(holder.getAdapterPosition()).getImageT());
                intent.putExtra("NumeroID",dataList.get(holder.getAdapterPosition()).getNumeroIDT());
                intent.putExtra("Nom",dataList.get(holder.getAdapterPosition()).getNameT());
                intent.putExtra("Prenom",dataList.get(holder.getAdapterPosition()).getPrenomT());
                intent.putExtra("Specialite",dataList.get(holder.getAdapterPosition()).getSpecialiteT());
                intent.putExtra("Adresse",dataList.get(holder.getAdapterPosition()).getAdresseT());
                intent.putExtra("Categorie",dataList.get(holder.getAdapterPosition()).getCategorieT());
                intent.putExtra("Telephone",dataList.get(holder.getAdapterPosition()).getTelephoneT());
                intent.putExtra("key",dataList.get(holder.getAdapterPosition()).getKey());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void  searchDataList(ArrayList<DataClass> searchList){
        dataList=searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView NumeroID,Nom,Prenom,Specialite,Adresse,Categorie,Telephone;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView){
     super(itemView);


     recImage=itemView.findViewById(R.id.recImage);

     recCard=itemView.findViewById(R.id.recCard);

        NumeroID=itemView.findViewById(R.id.NumeroID);
        Nom=itemView.findViewById(R.id.Nom);
        Prenom=itemView.findViewById(R.id.Prenom);
        Specialite=itemView.findViewById(R.id.Specialite);
        Adresse=itemView.findViewById(R.id.Adresse);
        Categorie=itemView.findViewById(R.id.Categorie);
        Telephone=itemView.findViewById(R.id.Telephone);
    }
}