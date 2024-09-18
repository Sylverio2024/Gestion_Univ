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

public class MyAdapter4 extends RecyclerView.Adapter<MyViewHolder4> {

    private Context context;
    private List<DataClass4> dataList4;

    public MyAdapter4(Context context, List<DataClass4> dataList4) {
        this.context = context;
        this.dataList4 = dataList4;
    }

    @NonNull
    @Override
    public MyViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item4, parent, false);
        return new MyViewHolder4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder4 holder, int position) {
        Glide.with(context).load(dataList4.get(position).getImageEvent()).into(holder.recImage4);
        holder.NumeroIDEvent.setText(dataList4.get(position).getNumeroEvent());
        holder.titreEvent.setText(dataList4.get(position).getTitreEvent());
        holder.dateEvent.setText(dataList4.get(position).getDateEvent());
        holder.heureEvent.setText(dataList4.get(position).getTimeEvent());
        holder.descriptionEvent.setText(dataList4.get(position).getDescriptionEvent());


        holder.recCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity4.class);
                intent.putExtra("Image",dataList4.get(holder.getAdapterPosition()).getImageEvent());
                intent.putExtra("NumeroIDEvent",dataList4.get(holder.getAdapterPosition()).getNumeroEvent());
                intent.putExtra("titreEvent",dataList4.get(holder.getAdapterPosition()).getTitreEvent());
                intent.putExtra("dateEvent",dataList4.get(holder.getAdapterPosition()).getDateEvent());
                intent.putExtra("heureEvent",dataList4.get(holder.getAdapterPosition()).getTimeEvent());
                intent.putExtra("descriptionEvent",dataList4.get(holder.getAdapterPosition()).getDescriptionEvent());
                intent.putExtra("key4",dataList4.get(holder.getAdapterPosition()).getKey4());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList4.size();
    }
    public void  searchDataList4(ArrayList<DataClass4> searchList4){
        dataList4=searchList4;
        notifyDataSetChanged();
    }
}
class MyViewHolder4 extends RecyclerView.ViewHolder{
    ImageView recImage4;
    TextView NumeroIDEvent,titreEvent,dateEvent,heureEvent,descriptionEvent;
    CardView recCard4;
    public MyViewHolder4(@NonNull View itemView){
        super(itemView);


        recImage4=itemView.findViewById(R.id.recImage4);

        recCard4=itemView.findViewById(R.id.recCard4);

        NumeroIDEvent=itemView.findViewById(R.id.NumeroIDEvent);
        titreEvent=itemView.findViewById(R.id.titreEvent);
        dateEvent=itemView.findViewById(R.id.dateEvent);
        heureEvent=itemView.findViewById(R.id.heureEvent);
        descriptionEvent=itemView.findViewById(R.id.descriptionEvent);

    }
}
