package status200.com.br.celhas;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;
import status200.com.br.celhas.app.MessageBox;
import status200.com.br.celhas.dao.DataBase;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.model.Cliente;
import status200.com.br.celhas.util.AlarmeAniversario;
import status200.com.br.celhas.util.DateUtils;

public class NovoClienteActivity extends AppCompatActivity implements View.OnLongClickListener{
    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private static int RESULTADO_IMAGEM;

    private EditText edtNome,edtTelefone,edtAniversario;
    private Button btnSalvar;
    private MaterialDialog mMaterialDialog;
    private QuickContactBadge qcbCliente;
    private DataBase dataBase;
    private RepositorioContato repositorioContato;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_cliente);

        btnSalvar = (Button)findViewById(R.id.btnSalvar);
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtTelefone =(EditText)findViewById(R.id.edtTelefone);
        qcbCliente = (QuickContactBadge) findViewById(R.id.qcbCliente);
        edtAniversario = (EditText)findViewById(R.id.edtAniversario);

        Bundle extra = getIntent().getExtras();

        if(extra!=null){
            cliente = (Cliente) extra.getSerializable("cliente");
            edtNome.setText(cliente.getNome());
            edtTelefone.setText(cliente.getTelefone());
            String aniversario = DateUtils.dateToString(cliente.getAniversario());
            edtAniversario.setText(aniversario.substring(0,5));
            btnSalvar.setText("Atualizar");

        }else {
            cliente = new Cliente();
        }

        if(cliente.getImagem()!=null) {
            qcbCliente.setImageBitmap(BitmapFactory.decodeFile(cliente.getImagem()));
        }else{
            qcbCliente.setImageResource(R.drawable.user_male);
        }

        ExibeDataListener listener = new ExibeDataListener();
        edtAniversario.setOnClickListener(listener);
        edtAniversario.setOnFocusChangeListener(listener);
        qcbCliente.setOnLongClickListener(this);

        try {
            dataBase = new DataBase(this);
            repositorioContato = new RepositorioContato( dataBase.getWritableDatabase());

        }catch(SQLException ex)
        {
            MessageBox.show(this, "Erro", "Erro ao criar o banco: " + ex.getMessage());
        }
    }

    public void salvar(View v){
        repositorioContato =
                new RepositorioContato(dataBase.getWritableDatabase());
        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();

        if(nome.equals("") || nome.equals(null)){
            edtNome.setError("Digite o nome");
            edtNome.requestFocus();
        }else if(telefone.equals("") || telefone.equals(null)){
            edtTelefone.setError("Digite o telefone");
            edtTelefone.requestFocus();
        }else {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            if (cliente.getId() == 0){
                repositorioContato.inserir(cliente);
                Toast.makeText(this, "Cliente adicionado!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                int sucesso = repositorioContato.alterar(cliente);
                if(sucesso>0) {
                    Toast.makeText(this, "Cliente atualizado!", Toast.LENGTH_SHORT).show();
                }
                AlarmeAniversario.definirAlarme(this, cliente);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        callreadImagem();
        return false;
    }

    public void getImagem(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULTADO_IMAGEM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_novo_cliente,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.menu_cdt:
                i = new Intent(this, ContatoActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULTADO_IMAGEM && resultCode == RESULT_OK && null != data) {
            Uri imagemSelecionada = data.getData();
            String[] colunaDoArquivoDeImagem = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imagemSelecionada,
                    colunaDoArquivoDeImagem, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(colunaDoArquivoDeImagem[0]);
            String caminhoDaImagem = cursor.getString(columnIndex);
            cursor.close();
            qcbCliente.setImageBitmap(BitmapFactory.decodeFile(caminhoDaImagem));
            cliente.setImagem(caminhoDaImagem);
        }
    }

    private void exibeData(){
        Calendar calendar = Calendar.getInstance();
        int ano =  calendar.get(Calendar.YEAR);
        int mes =  calendar.get(Calendar.MONTH);
        int dia =  calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        @Override
        public void onClick(View v) {
            exibeData();
        }
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                exibeData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String dt = DateUtils.dateToString(year, monthOfYear, dayOfMonth);
            Date data = DateUtils.getDate(year, monthOfYear, dayOfMonth);

            edtAniversario.setText(dt);
            cliente.setAniversario(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){
                    if( permissions[i].equalsIgnoreCase( Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){

                        callreadImagem();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void callreadImagem() {

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){
            //verifica se o usuario negou a permissao mais de uma vez
            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) ){
                callDialog( "É preciso conceder a permissão WRITE_EXTERNAL_STORAGE para usar imagens da galeria", new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} );
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{
            getImagem();
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

                        ActivityCompat.requestPermissions(NovoClienteActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBase.close();
    }
}
