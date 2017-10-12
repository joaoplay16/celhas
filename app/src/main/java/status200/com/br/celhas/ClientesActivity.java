package status200.com.br.celhas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
    }

    public void carregarClientes(){
        DataBase conn = new DataBase(this);
        repositorioContato = new RepositorioContato(conn.getWritableDatabase());
        adpClientes = repositorioContato.buscaClientes(this);
        listViewCli.setAdapter(adpClientes);
        conn.close();
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
    protected void onResume() {
        super.onResume();
        carregarClientes();
    }
}