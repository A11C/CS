package com.fiuady.hadp.compustore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuady.db.Assembly;
import com.fiuady.db.AssemblyProduct;
import com.fiuady.db.CompuStore;
import com.fiuady.db.Order;
import com.fiuady.db.Product;

import java.util.List;

/**
 * Created by jessm on 18/04/2017.
 */

public class AddOrderActivity extends AppCompatActivity {
    private class OrderHolder extends RecyclerView.ViewHolder {

        private TextView idPrtext, ProductIDtext, qtyAsPrtext;

        public OrderHolder(View itemView) {
            super(itemView);
            idPrtext = (TextView) itemView.findViewById(R.id.idPr_text);
            ProductIDtext = (TextView) itemView.findViewById(R.id.ProductID_text);
            qtyAsPrtext = (TextView) itemView.findViewById(R.id.qtyAsPr_text);


        }

        private void bindOrder(final AssemblyProduct order) {

            idPrtext.setText(Integer.toString(order.getId()));
            ProductIDtext.setText(Integer.toString(order.getProduct_id()));
            qtyAsPrtext.setText(Integer.toString(order.getQty()));

        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{

        private List<AssemblyProduct> orders;

        public OrderAdapter(List<AssemblyProduct> orders) { this.orders = orders; }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_assemblyproducts, parent, false);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.new_order);
        super.onCreate(savedInstanceState);
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.clients_rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(compustore.getAllAssemblyProducts());

        recyclerview.setAdapter(adapter);
    }
}


