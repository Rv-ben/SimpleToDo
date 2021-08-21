package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    public static final String EDITED_TEXT = "edited_text";
    EditText editText;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.edit_text);
        editButton = findViewById(R.id.edit_button);

        getSupportActionBar().setTitle("Edit ToDo Item");


        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        editButton.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.putExtra(MainActivity.KEY_ITEM_TEXT, editText.getText().toString());
            intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

            setResult(RESULT_OK, intent);

            finish();
        });
    }
}