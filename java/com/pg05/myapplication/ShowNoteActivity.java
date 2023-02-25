package com.pg05.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowNoteActivity extends AppCompatActivity {

    private void load_note(){
        SQLiteDatabase baza = this.openOrCreateDatabase("Baza.db", MODE_PRIVATE, null);

        String statement = String.format("SELECT date, content FROM notes WHERE title='%s'", MainActivity.selected_note_title);
        Cursor cursor = baza.rawQuery(statement, null);

        if(cursor.moveToFirst()){
            do{
                String title = MainActivity.selected_note_title;
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String content = cursor.getString(cursor.getColumnIndex("content"));

                TextView title_view = findViewById(R.id.title);
                TextView date_view = findViewById(R.id.date);
                TextView content_view = findViewById(R.id.content);

                title_view.setText(title);
                date_view.setText(date);
                content_view.setText(content);
            }while(cursor.moveToNext());

            cursor.close();
        }
        baza.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        load_note();

        //Back button listener
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // Back to main activity
                finish();
            }
        });
    }
}
