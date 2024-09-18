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

public class MyAdapter3 extends RecyclerView.Adapter<MyViewHolder3> {

    private Context context3;
    private List<DataClass3> dataList3;

    public MyAdapter3(Context context3, List<DataClass3> dataList3) {
        this.context3 = context3;
        this.dataList3 = dataList3;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view3= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item3, parent, false);
        return new MyViewHolder3(view3);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder3 holder, int position) {
        holder.NumeroIDSalle.setText(dataList3.get(position).getNumeroSalle());
        holder.NomSalle.setText(dataList3.get(position).getNameSalle());
        holder.DescriptionSalle.setText(dataList3.get(position).getDescriptionSalle());
        holder.StatutSalle.setText(dataList3.get(position).getStatutSalle());

        holder.recCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context3, detailActivity3.class);

                intent.putExtra("NumeroSalle",dataList3.get(holder.getAdapterPosition()).getNumeroSalle());
                intent.putExtra("NomSalle",dataList3.get(holder.getAdapterPosition()).getNameSalle());
                intent.putExtra("DescriptionSalle",dataList3.get(holder.getAdapterPosition()).getDescriptionSalle());
                intent.putExtra("StatutSalle",dataList3.get(holder.getAdapterPosition()).getStatutSalle());
                intent.putExtra("key3",dataList3.get(holder.getAdapterPosition()).getKey3());

                context3.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList3.size();
    }
    public void searchDataList3(ArrayList<DataClass3>searchList3){
        dataList3=searchList3;
        notifyDataSetChanged();
    }
}
class MyViewHolder3 extends RecyclerView.ViewHolder{

    TextView NumeroIDSalle,NomSalle,StatutSalle,DescriptionSalle;
    CardView recCard3;
    public MyViewHolder3(@NonNull View itemView){
        super(itemView);

        recCard3=itemView.findViewById(R.id.recCard3);
        NumeroIDSalle=itemView.findViewById(R.id.NumeroIDSalle);
        NomSalle=itemView.findViewById(R.id.NomSalle);
        StatutSalle=itemView.findViewById(R.id.StatutSalle);
        DescriptionSalle=itemView.findViewById(R.id.DescriptionSalle);
    }
}