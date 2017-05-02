package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jessm on 20/04/2017.
 */

public class AddAssemblyToOrderActivity extends AppCompatActivity {

    public static final String EXTRA_AID = "com.fiuady.hadp.compustore.extra_pid";
    private final static String KEY_SEARCHED = "searched";
    private boolean searched = false;
    public static final int REQUEST_CODE = 0;


    private CompuStore compustore;
    private RecyclerView recyclerview;
    private AddATOAdapter adapter;

    private class AddATOHolder extends RecyclerView.ViewHolder {

        private TextView idAStext, desAStext;

        public AddATOHolder(View itemView) {
            super(itemView);
            idAStext = (TextView) itemView.findViewById(R.id.idAs_text);
            desAStext = (TextView) itemView.findViewById(R.id.descriptionAs_text);


        }

        private void BindAddATO(final Assembly assembly) {
            idAStext.setText(String.valueOf(assembly.getId()));
            desAStext.setText((assembly.getDescripcion()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu4 = new PopupMenu(AddAssemblyToOrderActivity.this, itemView);
                    menu4.getMenuInflater().inflate(R.menu.add_prodtoassem, menu4.getMenu());

                    menu4.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent data = new Intent();
                            data.putExtra(EXTRA_AID, assembly.getId());
                            setResult(RESULT_OK, data);
                            finish();
                            return true;
                        }
                    });
                    menu4.show();
                }
            });

        }
    }

    private class AddATOAdapter extends RecyclerView.Adapter<AddAssemblyToOrderActivity.AddATOHolder> {
        private List<Assembly> assemblies;

        public AddATOAdapter(List<Assembly> assemblies) {
            this.assemblies = assemblies;
        }


        @Override
        public AddATOHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_assembly, parent, false);
            return new AddAssemblyToOrderActivity.AddATOHolder(view);
        }

        @Override
        public void onBindViewHolder(AddATOHolder holder, int positions) {
            holder.BindAddATO(assemblies.get(positions));
        }

        @Override
        public int getItemCount() {
            return assemblies.size();
        }

    }

    private EditText searchtext;
    private ImageButton btn;
    private List<Assembly> assemblies;
    private String textobusqueda = "";
    private final static String SEARCH_KEY = "search_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assemblytoorder);
        compustore = new CompuStore(this);

        searchtext = (EditText) findViewById(R.id.busquedaAATO_text);
        btn = (ImageButton) findViewById(R.id.buscarAATO_button);

        recyclerview = (RecyclerView) findViewById(R.id.AATO_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        assemblies = new ArrayList<>();
        if (savedInstanceState != null) {
            textobusqueda = savedInstanceState.getString(SEARCH_KEY);
            searchtext.setText(textobusqueda);
            for (Assembly assembly : compustore.getAllAssemblies()) {
                if (assembly.getDescripcion().toUpperCase().contains(searchtext.getText().toString().toUpperCase())) {
                    assemblies.add(assembly);
                }
            }
            if (assemblies.isEmpty()) {
                assemblies = (compustore.getAllAssemblies());
            }
            searched = savedInstanceState.getBoolean(KEY_SEARCHED, false);
            if (searched) {
                UpdateAdapter();
            }
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assemblies.isEmpty()) {
                    assemblies.clear();
                }
                for (Assembly assembly : compustore.getAllAssemblies()) {
                    if (assembly.getDescripcion().toUpperCase().contains(searchtext.getText().toString().toUpperCase())) {
                        assemblies.add(assembly);
                        textobusqueda = searchtext.getText().toString();
                    }

                }
                if (assemblies.isEmpty()) {
                    assemblies = (compustore.getAllAssemblies());
                }
                searched = true;
                UpdateAdapter();

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SEARCH_KEY, textobusqueda);
        outState.putBoolean(KEY_SEARCHED, searched);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void UpdateAdapter() {
        adapter = new AddATOAdapter(assemblies);
        recyclerview.setAdapter(adapter);
    }
}
