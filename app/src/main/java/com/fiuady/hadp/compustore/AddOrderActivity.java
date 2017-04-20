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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.AssemblyProduct;
import com.fiuady.db.Client;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.Product;

import java.util.List;

/**
 * Created by jessm on 18/04/2017.
 */


public class AddOrderActivity extends AppCompatActivity {
    private class OrderHolder extends RecyclerView.ViewHolder {

        private TextView idPrtext,Desctext;

        public OrderHolder(View itemView) {
            super(itemView);
            idPrtext = (TextView) itemView.findViewById(R.id.idAs_text);
            Desctext = (TextView) itemView.findViewById(R.id.descriptionAs_text);



        }

        private void bindOrder(final Assembly order) {

            idPrtext.setText(Integer.toString(order.getId()));
            Desctext.setText((order.getDescripcion()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu3 = new PopupMenu(AddOrderActivity.this, itemView);
                    menu3.getMenuInflater().inflate(R.menu.menu_option_catassem, menu3.getMenu());
                    menu3.show();
                }
            });
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        private List<Assembly> orders;

        public OrderAdapter(List<Assembly> orders) { this.orders = orders; }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_assembly, parent, false);
            return new AddOrderActivity.OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            holder.bindOrder(orders.get(position));
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }
    }

    private CompuStore compustore;

    private RecyclerView recyclerview;
    private OrderAdapter adapter;
    private Spinner spinner_clients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.new_order);
        super.onCreate(savedInstanceState);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.clients_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(compustore.getAllAssemblies());
        spinner_clients = (Spinner) findViewById(R.id.spinner_clients);
        recyclerview.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinner_clients.setAdapter(arrayAdapter);

        final List<Client> clients = compustore.getAllClients();
        for (Client client : clients) {
            arrayAdapter.add(client.getFirst_name()+" "+client.getLast_name());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(AddOrderActivity.this,AddAssemblyToOrderActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}


