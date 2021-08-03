package com.info.lab13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.PrecomputedTextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etItem;
    private TextView tvNo;
    private Button buttonMinus;
    private Button buttonPlus;
    private Button buttonAdd;
    private RecyclerView rv;

    private SQLiteDatabase mDatabase;

    private int count=0;

    private ArrayList<Item> itemArrayList;
    private ItemAdapter adapter;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Veritabani vt = new Veritabani(this);
        mDatabase=vt.getWritableDatabase();

        etItem=findViewById(R.id.etItem);
        tvNo=findViewById(R.id.tvNo);
        buttonMinus=findViewById(R.id.buttonMinus);
        buttonPlus=findViewById(R.id.buttonPlus);
        buttonAdd=findViewById(R.id.buttonAdd);


        etItem=findViewById(R.id.etItem);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ItemAdapter(this,getAllItems());
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(rv);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                tvNo.setText(String.valueOf(count));
            }
        });
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>0) {
                    count--;
                    tvNo.setText(String.valueOf(count));
                }
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etItem.getText().toString().trim().length()==0 || count==0){
                    return;
                }
                String name= etItem.getText().toString();

                new MyAsyncTask().execute(name);
            }
        });

    }

    private void removeItem(long id){
        mDatabase.delete("item","item_id="+id,null);
        adapter.swapCursor(getAllItems());
    }
    private Cursor getAllItems(){
        return mDatabase.query(
                "item",null,null,null,null,null,null
        );
    }
    public class MyAsyncTask extends AsyncTask<String, ProgressDialog ,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String name1 = strings[0];

            ContentValues cv = new ContentValues();
            cv.put("item_name",name1);
            cv.put("item_no",count);

            mDatabase.insert("item",null,cv);

            return true;
        }

        @Override
        protected void onProgressUpdate(ProgressDialog... values) {

            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
            ad.setMessage("Please wait...");
            ad.setTitle("Adding");
            ad.create().show();

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                adapter.swapCursor(getAllItems());
                etItem.getText().clear();
            }
        }
    }
}