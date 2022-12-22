package com.precise.i_will_sing_unto_the_lord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView hymnListView = findViewById(R.id.hymn_list);
        final List<Hymn> hymns = new ArrayList<>();

        // Read the hymns from the "hymns.txt" file in the "raw" folder
        InputStream inputStream = getResources().openRawResource(R.raw.hymns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        StringBuilder builder = new StringBuilder();
        String title = null;

        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line.isEmpty()) {
                // End of a hymn
                hymns.add(new Hymn(title, builder.toString()));
                builder.setLength(0);
                title = null;
            } else if (title == null) {
                // Start of a new hymn
                title = line;
            } else {
                // Continuation of a hymn
                builder.append(line).append("\n");
            }
        }

        ArrayAdapter<Hymn> adapter = new ArrayAdapter<>(this, R.layout.hymn_item, R.id.hymn_title, hymns);
        hymnListView.setAdapter(adapter);

        hymnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hymn hymn = hymns.get(position);
                String title = hymn.getTitle();
                String text = hymn.getText();

                Intent intent = new Intent(MainActivity.this, HymnDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });


    }
}