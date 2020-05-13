
package com.example.login.uiutilities;

import android.util.Log;
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

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>  {

    private OnDeleteListener mOnDeleteListener;

    class CViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textTipo, textCosto, textDataScadenza, textPeriodo, textFine, textConsumo;
        ImageButton btnEdit, btnDelete;
        OnDeleteListener onDeleteListener;

        CViewHolder(@NonNull View itemView, OnDeleteListener onDeleteListener) {
            super(itemView);
            textTipo = itemView.findViewById(R.id.text_tipo);
            btnEdit = itemView.findViewById(R.id.button_edit);
            btnDelete = itemView.findViewById(R.id.button_delete);
            textCosto = itemView.findViewById(R.id.text_costo);
            textDataScadenza = itemView.findViewById(R.id.text_data_scadenza);
            textPeriodo = itemView.findViewById(R.id.text_periodo_riferimento);
            textFine = itemView.findViewById(R.id.text_fine_riferimento);
            textConsumo = itemView.findViewById(R.id.text_consumo);

            this.onDeleteListener = onDeleteListener;
            //itemView.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onDeleteListener.onDeleteClick(struttura.get(position).getTipo(), struttura.get(position).getId());
            //btnEdit.setSelected(true);
            //btnDelete.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<BollettaLGI> struttura;

    public FeedAdapter(ArrayList<BollettaLGI> struttura, OnDeleteListener onDeleteListener){
        this.struttura = struttura;
        this.mOnDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new CViewHolder(view, mOnDeleteListener);
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
                    /*
                    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Delete", "onClick: Deleted " + position);

                        }
                    });

                     */
            }
        });



        holder.textDataScadenza.setText("Data di Scadenza: " + struttura.get(position).getDataScadenza());
        holder.textPeriodo.setText("Periodo: " + struttura.get(position).getPeriodo());
        holder.textFine.setText(" - "+ struttura.get(position).getFinePeriodo());
        switch (struttura.get(position).getTipo()){
            case "Luce":
                holder.textConsumo.setText("Consumo: " + struttura.get(position).getConsumo() + " kWh");
                //holder.itemView.setBackgroundColor(Color.CYAN);
                break;
            case "Gas":
                holder.textConsumo.setText("Consumo: " + struttura.get(position).getConsumo() + " m^3");
                break;
            default:
                holder.textConsumo.setVisibility(View.GONE);
                break;
        }
        holder.textCosto.setText("Costo: € "+ struttura.get(position).getCosto()+"");
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

    public interface OnDeleteListener {
        void onDeleteClick(String tipo, long codice);
    }
    public void deleteBolletta(){

    }
}
