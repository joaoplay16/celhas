package status200.com.br.celhas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import me.drakeet.materialdialog.MaterialDialog;
import status200.com.br.celhas.dao.DataBase;
import status200.com.br.celhas.dao.RepositorioContato;
import status200.com.br.celhas.model.Cliente;

public class ActivityInfoContato extends AppCompatActivity {
    private MaterialDialog mMaterialDialog;
    private TextView txtNome, txtTelefone;
    private ImageView imgCliente;
    private Cliente cliente;
    private static int RESULTADO_IMAGEM;
    private static final int REQUEST_PERMISSIONS_CODE = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_contato);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtTelefone = (TextView)findViewById(R.id.txtTelefone);
        imgCliente=(ImageView)findViewById(R.id.imgInfoCliente);

        Bundle extra = getIntent().getExtras();
        if(extra!=null && extra.containsKey("cliente")){
            cliente = (Cliente) extra.getSerializable("cliente");
            txtNome.setText(cliente.getNome());
            txtTelefone.setText(cliente.getTelefone());
        }
        if (cliente.getImagem() != null) {
            imgCliente.setImageBitmap(BitmapFactory.decodeFile(cliente.getImagem()));
        } else {
            imgCliente.setImageResource(R.drawable.user_male);
        }

        imgCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callreadImagem();
            }
        });
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

            imgCliente.setImageBitmap(BitmapFactory.decodeFile(caminhoDaImagem));

            DataBase conn = new DataBase(this);
            RepositorioContato repositorioContato = new RepositorioContato(conn.getWritableDatabase());
            cliente.setImagem(caminhoDaImagem);
            repositorioContato.alterar(cliente);
            conn.close();

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

                        ActivityCompat.requestPermissions(ActivityInfoContato.this, permissions, REQUEST_PERMISSIONS_CODE);
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
