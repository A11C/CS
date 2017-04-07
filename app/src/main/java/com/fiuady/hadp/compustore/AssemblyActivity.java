package com.fiuady.hadp.compustore;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals(menu.getMenu().getItem(0).getTitle())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AssemblyActivity.this);
                                View view = getLayoutInflater().inflate(R.layout.agregar_ensamble, null);
                                dialogTitle = (TextView) view.findViewById(R.id.dialog_tittle);
                                editText = (EditText) view.findViewById(R.id.dialog_text);

                                dialogTitle.setText(R.string.Edita_ensamble);

                                builder.setCancelable(false);
                                builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_modificar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (compustore.updateCategory(editText.getText().toString(), assembly.getId())) {
                                            Toast.makeText(AssemblyActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                                          //  adapter = new AssemblyAdapter(compustore.getAllCategories());
                                            recyclerview.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(AssemblyActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });
                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder build = new AlertDialog.Builder(AssemblyActivity.this);
                                build.setCancelable(false);
                                build.setTitle(getString(R.string.Elimina_ensamble));
                                build.setMessage("El siguiente ensamble será eliminado" + ": " + assembly.getDescripcion());

                                build.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }).setPositiveButton(R.string.texto_eliminar, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(AssemblyActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
                                      //  compustore.assemblydelete(assembly.getId(), true);
                                        //adapter = new AssemblyAdapter(compustore.getAllCategories());
                                        recyclerview.setAdapter(adapter);
                                    }
                                });

                                build.create().show();
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
        adapter = new AssemblyAdapter(compustore.getAllAssemblies());

        recyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agregar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.agregar_ensamble, null);
        dialogTitle = (TextView) view.findViewById(R.id.assembly_tittle);
        editText = (EditText) view.findViewById(R.id.assembly_text);
        dialogTitle.setText(R.string.Agrega_ensamble);
        builder.setCancelable(false);

        builder.setNegativeButton(R.string.texto_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.texto_guardar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
             //   if (compustore.InsertAssembly(editText.getText().toString())) {
             //       Toast.makeText(AssemblyActivity.this, R.string.Confirma_operacion, Toast.LENGTH_SHORT).show();
             //   adapter = new AssemblyAdapter(compustore.getAllCategories());
             //       recyclerview.setAdapter(adapter);
             //   } else {
             //       Toast.makeText(AssemblyActivity.this, R.string.Error_operacion, Toast.LENGTH_SHORT).show();
             //   }
            }
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        return super.onOptionsItemSelected(item);
    }
}

