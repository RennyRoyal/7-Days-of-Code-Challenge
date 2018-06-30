package com.example.rennymark.smartjournal;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JournalDb dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new JournalDb(this);

        lstItem = (ListView)findViewById(R.id.lstView);

        loadItemList();
    }

    private void loadItemList() {
        ArrayList<String> itemList = dbHelper.getItemList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.items,R.id.item_title,itemList);
            lstItem.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(itemList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        //Change menu icon color
        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_item:
                final EditText itemEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add New Story")
                        .setMessage("What is your new story?")
                        .setView(itemEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String item = String.valueOf(itemEditText.getText());
                                dbHelper.insertNewItem(item);
                                loadItemList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteItem(View view){
        View parent = (View)view.getParent();
        TextView itemTextView = (TextView)parent.findViewById(R.id.item_title);
        Log.e("String", (String) itemTextView.getText());
        String item = String.valueOf(itemTextView.getText());
        dbHelper.deleteItem(item);
        loadItemList();
    }
}