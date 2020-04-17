package com.example.login.uiutilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.Recensione;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>{

    class CViewHolder extends RecyclerView.ViewHolder {
        TextView textTitolo, textVoto, textTesto;

        CViewHolder(@NonNull View itemView){
            super(itemView);
            textTitolo = itemView.findViewById(R.id.text_titolo);
            textVoto = itemView.findViewById(R.id.text_voto);
            textTesto = itemView.findViewById(R.id.text_testo);
        }
    }

    private ArrayList<Recensione> struttura;

    public FeedAdapter(ArrayList<Recensione> struttura){
        this.struttura = struttura;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.textTitolo.setText(struttura.get(position).getTitolo());
        holder.textVoto.setText(struttura.get(position).getVoto()+"");
        holder.textTesto.setText(struttura.get(position).getTesto());
    }

    @Override
    public int getItemCount() {
        return struttura.size();
    }
}
