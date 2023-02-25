package com.pg05.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static String selected_note_title;
    private TextView[] notes = new TextView[5];

    private void init_db(){
        SQLiteDatabase baza = this.openOrCreateDatabase("Baza.db", MODE_PRIVATE, null);
        baza.execSQL("CREATE TABLE IF NOT EXISTS notes (title TEXT, content TEXT, date TEXT)");
        baza.close();
    }

    // Title of the choosen note is saved in MainActivity class as static, redirect to ShowNoteActivity
    private void open_note(){
        Intent intent = new Intent(this, ShowNoteActivity.class);
        startActivity(intent);
    }

    private void load_notes(){
        notes[0] = findViewById(R.id.note1);
        notes[1] = findViewById(R.id.note2);
        notes[2] = findViewById(R.id.note3);
        notes[3] = findViewById(R.id.note4);
        notes[4] = findViewById(R.id.note5);

        SQLiteDatabase baza = this.openOrCreateDatabase("Baza.db", MODE_PRIVATE, null);
        String statement = "SELECT title FROM notes ORDER BY date ASC LIMIT 5";
        Cursor cursor = baza.rawQuery(statement, null);

        if(cursor.moveToFirst()){
            int index = 0;
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                notes[index].setText(title);
                index++;
            }while(cursor.moveToNext());

            cursor.close();
        }
        baza.close();

        // On Click event for each note
        for(final TextView note : notes){
            note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   // Show note
                    selected_note_title = note.getText().toString();
                    open_note();
                }
            });
        }
    }

    private void search(){
        // Before searching for note, u must clear the list
        for(TextView note : notes){ note.setText(""); }

        SQLiteDatabase baza = this.openOrCreateDatabase("Baza.db", MODE_PRIVATE, null);

        EditText search_text = findViewById(R.id.search_text);
        String search_phrase = search_text.getText().toString();

        String statement = String.format("SELECT title FROM notes WHERE title like '%s%s%s' LIMIT 5", "%", search_phrase, "%");
        Cursor cursor = baza.rawQuery(statement, null);
        if(cursor.moveToFirst()){
            int index = 0;
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                notes[index].setText(title);
                index++;
            }while(cursor.moveToNext());

            cursor.close();
        }
        baza.close();
    }

    private void open_new_note(){
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_db();

        setContentView(R.layout.activity_main);
        load_notes();

        //Search button on click event
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Set titles according to search results
                search();
            }
        });

        //New note button on click event
        findViewById(R.id.add_redirect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Go to new note activity
                open_new_note();
            }
        });
    }

    // After creating new note you must refresh your list
    @Override
    protected void onResume() {
        super.onResume();
        load_notes();
    }
}
