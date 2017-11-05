package status200.com.br.celhas;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import status200.com.br.celhas.app.MessageBox;
import status200.com.br.celhas.dao.DataBase;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.model.Cliente;
import status200.com.br.celhas.util.AlarmeAniversario;
import status200.com.br.celhas.util.DateUtils;

public class NovoClienteActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private static int RESULTADO_IMAGEM;

    private EditText edtNome,edtTelefone,edtAniversario;
    private Button btnSalvar;
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
            if(cliente.getImagem()!=null) {
                qcbCliente.setImageBitmap(BitmapFactory.decodeFile(cliente.getImagem()));
            }else{
                qcbCliente.setImageResource(R.drawable.user_male);
            }
        }else {
            cliente = new Cliente();
        }

        ExibeDataListener listener = new ExibeDataListener();
        edtAniversario.setOnClickListener(listener);
        edtAniversario.setOnFocusChangeListener(listener);
        qcbCliente.setOnClickListener(this);

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

        cliente.setNome(edtNome.getText().toString());
        cliente.setTelefone(edtTelefone.getText().toString());

        if (cliente.getId() == 0){
            repositorioContato.inserir(cliente);

            Toast.makeText(this, "Cliente adicionado!", Toast.LENGTH_SHORT ).show();
        }else{

            int sucesso = repositorioContato.alterar(cliente);

            if(sucesso>0) {

                AlarmeAniversario.definirAlarme(this, cliente);
                Toast.makeText(this, "Cliente atualizado!", Toast.LENGTH_SHORT).show();
                Log.i("DATA", cliente.getAniversario().toString());
            }
        }

    }

    @Override
    public void onClick(View view) {
        getImagem();
    }

    public void getImagem(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULTADO_IMAGEM);
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

//            DataBase conn = new DataBase(this);
//            RepositorioContato repositorioContato = new RepositorioContato(conn.getWritableDatabase());
            cliente.setImagem(caminhoDaImagem);
//            repositorioContato.alterar(cliente);
//            conn.close();

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
    protected void onDestroy() {
        super.onDestroy();
        dataBase.close();
    }
}
