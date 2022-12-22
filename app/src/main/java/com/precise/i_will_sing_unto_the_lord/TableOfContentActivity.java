package com.precise.i_will_sing_unto_the_lord;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TableOfContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_of_content);

        ListView hymnListView = findViewById(R.id.hymn_list);
        final List<Hymn> hymns = new ArrayList<>();

// Read the hymns from the "hymns.txt" file in the "raw" folder
        InputStream inputStream = getResources().openRawResource(R.raw.hymns);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder builder = new StringBuilder();
        String title = null;

        try {
            while ((line = reader.readLine()) != null) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<Hymn> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hymns);
        hymnListView.setAdapter(adapter);

        hymnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hymn hymn = hymns.get(position);
                String title = hymn.getTitle();
                String text = hymn.getText();

                Intent intent = new Intent(TableOfContentActivity.this, HymnDetailActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("text", text);
                startActivity(intent);
            }
        });

        final SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayAdapter<Hymn> adapter = (ArrayAdapter<Hymn>) hymnListView.getAdapter();
                adapter.getFilter().filter(newText);

                return false;
            }
        });

    }
}