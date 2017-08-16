package status200.com.br.celhas;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import status200.com.br.celhas.dao.DataBase;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.util.FiltraDados;

public class ClientesActivity extends AppCompatActivity {

    private EditText edtPesquisaCli;
    private ListView listViewCli;
    private ArrayAdapter adpClientes;
    private RepositorioContato repositorioContato;
    private DataBase dataBase;
    private SQLiteDatabase conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        edtPesquisaCli = (EditText)findViewById(R.id.edtPesquisaCli);
        listViewCli = (ListView)findViewById(R.id.listViewCli);

        carregarClientes();
    }

    public void carregarClientes(){

        dataBase = new DataBase(this);
        conn = dataBase.getWritableDatabase();

        repositorioContato = new RepositorioContato(conn);
        adpClientes = repositorioContato.buscaClientes(this);
        listViewCli.setAdapter(adpClientes);

        FiltraDados filtraDados = new FiltraDados(adpClientes);
        edtPesquisaCli.addTextChangedListener(filtraDados);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(conn!=null){
            conn.close();
        }
    }

}
