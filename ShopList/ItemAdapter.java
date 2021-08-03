package com.info.lab13;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CardTasarimTutucu> {
    private Context mContext;
    private Cursor cursor;

    public ItemAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_tasarim,parent,false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {
        if(!cursor.moveToPosition(position)){
            return;
        }
        String name= cursor.getString(cursor.getColumnIndex("item_name"));
        int count = cursor.getInt(cursor.getColumnIndex("item_no"));
        long id = cursor.getLong(cursor.getColumnIndex("item_id"));

        holder.tvItemNo.setText(name);
        holder.tvItemName.setText(String.valueOf(count));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView tvItemNo;
        private TextView tvItemName;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            tvItemNo=itemView.findViewById(R.id.tvItemNo);
            tvItemName=itemView.findViewById(R.id.tvItemName);
        }
    }
    public void swapCursor(Cursor newCursor){
        if(cursor != null){
            cursor.close();
        }
        cursor=newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
}
