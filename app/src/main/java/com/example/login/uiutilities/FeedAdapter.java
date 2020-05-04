
package com.example.login.uiutilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.BollettaLGI;


import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>{

    class CViewHolder extends RecyclerView.ViewHolder {
        TextView textTipo, textCosto, textDataScadenza, textPeriodo, textFine, textConsumo;
        ImageButton btnEdit, btnDelete;

        CViewHolder(@NonNull View itemView) {
            super(itemView);
            textTipo = itemView.findViewById(R.id.text_tipo);
            btnEdit = itemView.findViewById(R.id.button_edit);
            btnDelete = itemView.findViewById(R.id.button_delete);
            textCosto = itemView.findViewById(R.id.text_costo);
            textDataScadenza = itemView.findViewById(R.id.text_data_scadenza);
            textPeriodo = itemView.findViewById(R.id.text_periodo_riferimento);
            textFine = itemView.findViewById(R.id.text_fine_riferimento);
            textConsumo = itemView.findViewById(R.id.text_consumo);

        }
    }

    private ArrayList<BollettaLGI> struttura;

    public FeedAdapter(ArrayList<BollettaLGI> struttura){
        this.struttura = struttura;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CViewHolder holder, int position) {
        holder.textTipo.setText(struttura.get(position).getTipo());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.VISIBLE);
            }
        });
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
