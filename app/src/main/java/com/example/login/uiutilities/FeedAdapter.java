
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
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.CViewHolder>  {

    private OnFeedClickListener mOnFeedClickListener;

    class CViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTipo, textCosto, textDataScadenza, textPeriodo, textFine, textConsumo;
        ImageButton btnEdit, btnDelete, btnCalendar;
        OnFeedClickListener onFeedClickListener;

        CViewHolder(@NonNull View itemView, OnFeedClickListener onFeedClickListener) {
            super(itemView);
            textTipo = itemView.findViewById(R.id.text_tipo);
            btnCalendar = itemView.findViewById(R.id.button_calendar);
            btnEdit = itemView.findViewById(R.id.button_edit);
            btnDelete = itemView.findViewById(R.id.button_delete);
            textCosto = itemView.findViewById(R.id.text_costo);
            textDataScadenza = itemView.findViewById(R.id.text_data_scadenza);
            textPeriodo = itemView.findViewById(R.id.text_periodo_riferimento);
            textFine = itemView.findViewById(R.id.text_fine_riferimento);
            textConsumo = itemView.findViewById(R.id.text_consumo);

            this.onFeedClickListener = onFeedClickListener;
            btnDelete.setOnClickListener(this);
            btnCalendar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            switch (v.getId()) {
                case R.id.button_calendar:
                    onFeedClickListener.onCalendarClick(struttura.get(position).getTipo(), struttura.get(position).getDataScadenza());
                    break;
                case R.id.button_delete:
                    onFeedClickListener.onDeleteClick(struttura.get(position).getTipo(), struttura.get(position).getId());
                    break;
            }
        }
    }

    private ArrayList<BollettaLGI> struttura;

    public FeedAdapter(ArrayList<BollettaLGI> struttura, OnFeedClickListener onFeedClickListener){
        this.struttura = struttura;
        this.mOnFeedClickListener = onFeedClickListener;
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new CViewHolder(view, mOnFeedClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        switch(struttura.get(position).getTipo()){
            case "Luce":
                holder.textTipo.setTextColor(ColorTemplate.MATERIAL_COLORS[1]);
                break;
            case "Gas":
                holder.textTipo.setTextColor(ColorTemplate.MATERIAL_COLORS[2]);
                break;
            case "Internet":
                holder.textTipo.setTextColor(ColorTemplate.MATERIAL_COLORS[3]);
                break;

        }
        holder.textTipo.setText(struttura.get(position).getTipo());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    holder.btnEdit.setVisibility(View.GONE);
                    holder.btnDelete.setVisibility(View.VISIBLE);
            }
        });



        holder.textDataScadenza.setText(R.string.data_di_scadenza + struttura.get(position).getDataScadenza());
        holder.textPeriodo.setText(R.string.periodo + struttura.get(position).getPeriodo());
        holder.textFine.setText(" - "+ struttura.get(position).getFinePeriodo());
        switch (struttura.get(position).getTipo()){
            case "Luce":
                holder.textConsumo.setText(R.string.consumo + struttura.get(position).getConsumo() + " kWh");
                break;
            case "Gas":
                holder.textConsumo.setText(R.string.consumo + struttura.get(position).getConsumo() + " m^3");
                break;
            default:
                holder.textConsumo.setVisibility(View.GONE);
                break;
        }
        holder.textCosto.setText(R.string.costo + struttura.get(position).getCosto()+"");
    }

    @Override
    public int getItemCount() {
        return struttura.size();
    }

    public interface OnFeedClickListener {
        void onDeleteClick(String tipo, long codice);
        void onCalendarClick(String tipo, String data);
    }

}
