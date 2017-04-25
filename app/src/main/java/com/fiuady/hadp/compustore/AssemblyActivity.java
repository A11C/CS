package com.fiuady.hadp.compustore;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.db.Assembly;
import com.fiuady.db.CompuStore;

import java.util.List;

public class AssemblyActivity extends AppCompatActivity {

    public static class mDialogFragment extends DialogFragment {

        private int Id;
        private String Desc;
        private CompuStore compustore;

        static mDialogFragment newInstance(int id, String desc) {
            mDialogFragment dF = new mDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            args.putString("desc", desc);
            dF.setArguments(args);
            return dF;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            Id = getArguments().getInt("id");
            Desc = getArguments().getString("desc");
            return DialogErase();
        }

        public AlertDialog DialogErase() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.Elimina_ensamble));
            builder.setMessage("El siguiente ensamble será eliminado: " + Desc);
            compustore = new CompuStore(getActivity());

            builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    compustore.DeleteAssembly(Id);
                    ((AssemblyActivity) getActivity()).UpdateAdapter();
                    Toast.makeText(getActivity(), R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                }
            });
            return builder.create();
        }
    }

    private EditText editText;
    private TextView dialogTitle;

    private class AssemblyHolder extends RecyclerView.ViewHolder {

        private TextView descriptionassembly;

        public AssemblyHolder(View itemView) {
            super(itemView);
            descriptionassembly = (TextView) itemView.findViewById(R.id.assembly_description);
        }

        public void bindAssembly(final Assembly assembly) {
            descriptionassembly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu = new PopupMenu(AssemblyActivity.this, descriptionassembly);
                    menu.getMenuInflater().inflate(R.menu.menu_option_catassem, menu.getMenu());

                    if (compustore.AssemblyInOrder(assembly.getId())) {
                        menu.getMenu().removeItem(R.id.menu_1);
                    }

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(menu.getMenu().getItem(0).getTitle())) {
                                Intent i = new Intent(AssemblyActivity.this, ModAssemblyActivity.class);
                                i.putExtra(ModAssemblyActivity.EXTRA_IDASSEMBLY, assembly.getId());
                                i.putExtra(ModAssemblyActivity.EXTRA_DESCASSEMBLY, assembly.getDescripcion());
                                startActivityForResult(i,ModAssemblyActivity.REQUESTCODE);
                            } else {
                                mDialogFragment fragment = mDialogFragment.newInstance(assembly.getId(), assembly.getDescripcion());
                                fragment.show(getFragmentManager(), "DialogErase");
                            }
                            return true;
                        }
                    });
                    menu.show();
                }
            });
            descriptionassembly.setText(assembly.getDescripcion());
        }
    }

    private class AssemblyAdapter extends RecyclerView.Adapter<AssemblyHolder> {

        private List<Assembly> assemblies;

        public AssemblyAdapter(List<Assembly> assemblies) {
            this.assemblies = assemblies;
        }


        @Override
        public AssemblyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.assembly_list, parent, false);
            return new AssemblyHolder(view);

        }

        @Override
        public void onBindViewHolder(AssemblyHolder holder, int position) {
            holder.bindAssembly(assemblies.get(position));
        }

        @Override
        public int getItemCount() {

            return assemblies.size();
        }


    }

    private CompuStore compustore;
    private RecyclerView recyclerview;
    private AssemblyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly);

        //if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAssemblies));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorAssemblies));
        //}
        compustore = new CompuStore(this);

        recyclerview = (RecyclerView) findViewById(R.id.activity_assemblies);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        UpdateAdapter();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = new Intent(AssemblyActivity.this, AddAssemblyActivity.class);
        startActivityForResult(i, AddAssemblyActivity.REQUESTCODE);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Toast.makeText(AssemblyActivity.this, R.string.EnsambleAdd, Toast.LENGTH_SHORT).show();
        }else if(requestCode==0 && resultCode==RESULT_OK){
            Toast.makeText(AssemblyActivity.this, R.string.EnsambleMod, Toast.LENGTH_SHORT).show();
        }
        UpdateAdapter();
    }

    public void UpdateAdapter() {
        adapter = new AssemblyAdapter(compustore.getAllAssemblies());
        recyclerview.setAdapter(adapter);
    }
}

