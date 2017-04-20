package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Product;

import java.util.List;


public class ModOrderActivity extends AppCompatActivity {
    private CompuStore compustore;
    private RecyclerView recyclerview;
    private ModOrderAdapter adapter;
    private TextView idAStext, desAStext;

    private class ModOrderHolder extends RecyclerView.ViewHolder {

        private TextView idAStext, desAStext;

        public ModOrderHolder(View itemView) {
            super(itemView);
            idAStext = (TextView) itemView.findViewById(R.id.idAs_text);
            desAStext = (TextView) itemView.findViewById(R.id.descriptionAs_text);


        }

        private void BindModOrder (final Assembly assembly){
            idAStext.setText(Integer.toString(assembly.getId()));
            desAStext.setText((assembly.getDescripcion()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu3 = new PopupMenu(ModOrderActivity.this, itemView);
                    menu3.getMenuInflater().inflate(R.menu.menu_option_catassem, menu3.getMenu());
                    menu3.show();
                }
            });

        }
    }

    private class ModOrderAdapter extends RecyclerView.Adapter<ModOrderActivity.ModOrderHolder>{
        private List<Assembly>assemblies;

        public ModOrderAdapter(List<Assembly>assemblies){this.assemblies= assemblies;}


        @Override
        public ModOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_assembly, parent, false);
            return new  ModOrderActivity.ModOrderHolder(view);
        }

        @Override
        public void onBindViewHolder(ModOrderHolder holder, int positions) {
        holder.BindModOrder(assemblies.get(positions));
        }

        @Override
        public int getItemCount() {
            return assemblies.size();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_orders);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.assemblyOR_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModOrderAdapter(compustore.getAllAssemblies());
        recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(ModOrderActivity.this,AddAssemblyToOrderActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}
