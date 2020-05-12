
package com.example.login.uiutilities;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.entities.BollettaLGI;


import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>  {

    private OnNoteListener mOnNoteListener;

    class CViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textTipo, textCosto, textDataScadenza, textPeriodo, textFine, textConsumo;
        ImageButton btnEdit, btnDelete;
        OnNoteListener onNoteListener;

        CViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textTipo = itemView.findViewById(R.id.text_tipo);
            btnEdit = itemView.findViewById(R.id.button_edit);
            btnDelete = itemView.findViewById(R.id.button_delete);
            textCosto = itemView.findViewById(R.id.text_costo);
            textDataScadenza = itemView.findViewById(R.id.text_data_scadenza);
            textPeriodo = itemView.findViewById(R.id.text_periodo_riferimento);
            textFine = itemView.findViewById(R.id.text_fine_riferimento);
            textConsumo = itemView.findViewById(R.id.text_consumo);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
            btnEdit.setSelected(true);
            //btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<BollettaLGI> struttura;

    public FeedAdapter(ArrayList<BollettaLGI> struttura, OnNoteListener onNoteListener){
        this.struttura = struttura;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new CViewHolder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.textTipo.setText(struttura.get(position).getTipo());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Btn", "onClick: "+position);
                    holder.btnEdit.setVisibility(View.GONE);
                    holder.btnDelete.setVisibility(View.VISIBLE);
                    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Delete", "onClick: Deleted " + position);
                        }
                    });
            }
        });


        holder.textCosto.setText("Costo: € "+ struttura.get(position).getCosto()+"");
        holder.textDataScadenza.setText("Data di Scadenza: " + struttura.get(position).getDataScadenza());
        holder.textPeriodo.setText("Da: " + struttura.get(position).getPeriodo());
        holder.textFine.setText("A: "+ struttura.get(position).getFinePeriodo());
        switch (struttura.get(position).getTipo()){
            case "Luce":
                holder.textConsumo.setText("Consumo: " + struttura.get(position).getConsumo() + " kWh");
                //holder.itemView.setBackgroundColor(Color.CYAN);
                break;
            case "Gas":
                holder.textConsumo.setText("Consumo: " + struttura.get(position).getConsumo() + " m^3");
                break;
        }
        /*
        if (struttura.get(position).getTipo() != "Internet") {
            holder.textConsumo.setText("Consumo: " + struttura.get(position).getConsumo() + " kWh");
        }

         */
    }

    @Override
    public int getItemCount() {
        return struttura.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
