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
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;

import java.util.List;

/**
 * Created by jessm on 20/04/2017.
 */

public class AddAssemblyToOrderActivity extends AppCompatActivity {
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
            idAStext.setText(Integer.toString(assembly.getId()));
            desAStext.setText((assembly.getDescripcion()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu4 = new PopupMenu(AddAssemblyToOrderActivity.this, itemView);
                    menu4.getMenuInflater().inflate(R.menu.add_prodtoassem, menu4.getMenu());

                    menu4.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(menu4.getMenu().getItem(0).getTitle())) {
                                finish();
                            }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assemblytoorder);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.AATO_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddATOAdapter(compustore.getAllAssemblies());
        recyclerview.setAdapter(adapter);
    }
}
