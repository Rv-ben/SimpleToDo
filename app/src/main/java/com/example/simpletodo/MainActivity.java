package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    private static final int EDIT_TEXT_CODE = 10 ;

    ArrayList<String> items;
    Button addBtn;
    EditText textField;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.add_button);
        textField = findViewById(R.id.add_item_input);
        recyclerView = findViewById(R.id.itemView);

        loadItems();

        ItemsAdapter.OnLongClickListener longClickListener = position -> {
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            Toast.makeText(getApplicationContext(), "Item Removed!", Toast.LENGTH_SHORT).show();
        };

        ItemsAdapter.OnClickListener clickListener = position -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra(KEY_ITEM_TEXT, items.get(position));
            intent.putExtra(KEY_ITEM_POSITION, position);

            startActivityForResult(intent, EDIT_TEXT_CODE);
        };


        itemsAdapter = new ItemsAdapter(items, longClickListener, clickListener);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = textField.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);

                textField.setText("");

                Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position, data.getStringExtra(KEY_ITEM_TEXT));

            itemsAdapter.notifyItemChanged(position);
            writeItems();
            Toast.makeText(getApplicationContext(), "Item updated!", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("MainActivity", "Error editing text.");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        writeItems();
        super.onPause();
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            if (new File(getFilesDir(), "data.txt").exists()) {
                items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            } else {
                items = new ArrayList<String>();
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error loading items.", e);
        }
    }

    private void writeItems() {
        Log.println(Log.DEBUG, "w", "writing" + items.toString());
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}