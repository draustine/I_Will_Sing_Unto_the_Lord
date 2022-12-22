package com.precise.i_will_sing_unto_the_lord;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class HymnDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hymn_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        TextView titleView = findViewById(R.id.hymn_title);
        titleView.setText(title);

        TextView textView = findViewById(R.id.hymn_text);
        textView.setText(text);

        RecyclerView tableOfContentView = findViewById(R.id.table_of_content);
        tableOfContentView.setLayoutManager(new LinearLayoutManager(this));

        final List<String> lines = Arrays.asList(text.split("\n"));
        TableOfContentAdapter adapter = new TableOfContentAdapter(lines);
        tableOfContentView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TableOfContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: Scroll to the selected line in the hymn text
            }
        });


    }

    public abstract class TableOfContentAdapter extends RecyclerView.Adapter<TableOfContentAdapter.ViewHolder> {

        private List<String> lines;
        private OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        // TODO: Implement the rest of the adapter class

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView, final TableOfContentAdapter.OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public TableOfContentAdapter(List<String> lines) {
        this.lines = lines;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_of_content_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String line = lines.get(position);
        holder.textView.setText(line);
    }

    @Override
    public int getItemCount() {
        return lines.size();
    }

}