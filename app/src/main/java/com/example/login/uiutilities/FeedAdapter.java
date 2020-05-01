
package com.example.login.uiutilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.Bolletta;
import com.example.login.entities.BollettaLuce;


import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>{

    class CViewHolder extends RecyclerView.ViewHolder {
        TextView textTipo, textCosto, textDataScadenza, textPeriodo, textFine, textConsumo;

        CViewHolder(@NonNull View itemView) {
            super(itemView);
            textTipo = itemView.findViewById(R.id.text_tipo);
            textCosto = itemView.findViewById(R.id.text_costo);
            textDataScadenza = itemView.findViewById(R.id.text_data_scadenza);
            textPeriodo = itemView.findViewById(R.id.text_periodo_riferimento);
            textFine = itemView.findViewById(R.id.text_fine_riferimento);
            textConsumo = itemView.findViewById(R.id.text_consumo);

        }
    }

    private ArrayList<BollettaLuce> struttura;

    public FeedAdapter(ArrayList<BollettaLuce> struttura){
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
        holder.textTipo.setText("Luce");
        holder.textCosto.setText("Costo: € "+ struttura.get(position).getCosto()+"");
        holder.textDataScadenza.setText("Data di Scadenza: " + struttura.get(position).getDataScadenza());
        holder.textPeriodo.setText("Da: " + struttura.get(position).getPeriodo());
        holder.textFine.setText("A: "+ struttura.get(position).getFinePeriodo());
        holder.textConsumo.setText("Consumo: " +struttura.get(position).getConsumo()+ " kWh");
    }

    @Override
    public int getItemCount() {
        return struttura.size();
    }
}
