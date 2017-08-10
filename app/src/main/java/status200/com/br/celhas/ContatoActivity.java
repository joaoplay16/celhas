package status200.com.br.celhas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import me.drakeet.materialdialog.MaterialDialog;
import status200.com.br.celhas.app.MessageBox;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.model.Cliente;

public class ContatoActivity extends AppCompatActivity {

    private EditText edtPesquisa;
    private ListView listView;
    private MaterialDialog mMaterialDialog;
    private ArrayAdapter<Cliente> adpContatos;
    private RepositorioContato repositorioContato;

    private static final int REQUEST_PERMISSIONS_CODE = 128;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        listView = (ListView)findViewById(R.id.listViewCon);
        edtPesquisa  = (EditText)findViewById(R.id.edtPesquisa);
        callreadContacts();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){

                    if( permissions[i].equalsIgnoreCase( Manifest.permission.READ_CONTACTS )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){

                        callreadContacts();

                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void carregarContatos(){

        try{
            repositorioContato = new RepositorioContato(null);
            adpContatos = repositorioContato.buscaContatos(this);
            listView.setAdapter(adpContatos);

            FiltraDados filtraDados = new FiltraDados(adpContatos);
            edtPesquisa.addTextChangedListener(filtraDados);

        }catch (Exception ex){
            MessageBox.show(this, "Erro", "Erro no repositorio" + ex.getMessage());
        }
    }

    public void callreadContacts() {

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ){
            //verifica se o usuario negou a permissao mais de uma vez
            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_CONTACTS ) ){
                callDialog( "É preciso conceder a permissão READ_CONTACTS para apresentação dos contatos do telefone.", new String[]{Manifest.permission.READ_CONTACTS} );
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{
            carregarContatos();
        }
    }

    private class FiltraDados implements TextWatcher {
        private ArrayAdapter<Cliente> arrayAdapter;

        private FiltraDados(ArrayAdapter<Cliente> arrayAdapter)
        {
            this.arrayAdapter = arrayAdapter;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    // UTIL
    private void callDialog( String message, final String[] permissions ){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permissão")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(ContatoActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
}
