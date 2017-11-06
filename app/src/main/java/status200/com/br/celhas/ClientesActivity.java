package status200.com.br.celhas;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import status200.com.br.celhas.dao.DataBase;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.model.Cliente;
import status200.com.br.celhas.util.FiltraDados;

public class ClientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private EditText edtPesquisaCli;
    private ListView listViewCli;
    private ArrayAdapter adpClientes;
    private RepositorioContato repositorioContato;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        edtPesquisaCli = (EditText)findViewById(R.id.edtPesquisaCli);
        listViewCli = (ListView)findViewById(R.id.listViewCli);

        carregarClientes();
        listViewCli.setOnItemClickListener(this);
        listViewCli.setOnCreateContextMenuListener(this);
    }

    public void carregarClientes(){
        dataBase = new DataBase(this);
        repositorioContato = new RepositorioContato(dataBase.getWritableDatabase());
        adpClientes = repositorioContato.buscaClientes(this);
        listViewCli.setAdapter(adpClientes);
        FiltraDados filtraDados = new FiltraDados(adpClientes);
        edtPesquisaCli.addTextChangedListener(filtraDados);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cliente cliente = (Cliente) adapterView.getItemAtPosition(position);
        Intent i = new Intent(this, ActivityInfoContato.class);
        i.putExtra("cliente",cliente);
        startActivity(i);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Editar");
        menu.add("Excluir");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Cliente cliente = (Cliente) listViewCli.getItemAtPosition((int) info.id);

        switch (item.getTitle().toString()){
            case "Editar":
                Intent  i = new Intent(this, NovoClienteActivity.class);
                i.putExtra("cliente",cliente);
                startActivity(i);
                break;
            case "Excluir":
                repositorioContato.excluir(cliente.getId());
                adpClientes.remove(cliente);
                adpClientes.notifyDataSetChanged();
                Toast.makeText(this, cliente.getNome() + " excluida(o)",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_clientes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.menu_novo:
                i = new Intent(this, NovoClienteActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarClientes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBase.close();
    }

}