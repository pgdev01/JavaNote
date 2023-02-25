package com.pg05.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    private void add_note(){
        SQLiteDatabase baza = this.openOrCreateDatabase("Baza.db", MODE_PRIVATE, null);

        EditText title_view = findViewById(R.id.title);
        EditText content_view = findViewById(R.id.content);

        String title = title_view.getText().toString();
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String content = content_view.getText().toString();

        String statement = String.format("INSERT INTO notes (title, content, date) VALUES ('%s', '%s', '%s')", title, content, date);
        baza.execSQL(statement);
        baza.close();

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Submit button listener
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Add note
                add_note();
            }
        });

        //Back button listener
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Back to main activity
                finish();
            }
        });
    }
}
