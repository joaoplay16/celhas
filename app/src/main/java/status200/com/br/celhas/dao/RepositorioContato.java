package status200.com.br.celhas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import status200.com.br.celhas.ContatoArrayAdapter;
import status200.com.br.celhas.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import status200.com.br.celhas.ClienteArrayAdapter;
import status200.com.br.celhas.model.Cliente;
import status200.com.br.celhas.provider.ContatosProvider;



public class RepositorioContato {

    private SQLiteDatabase conn;

    public RepositorioContato(SQLiteDatabase conn)
    {
        this.conn = conn;
    }


    private ContentValues preencheContentValues(Cliente cliente)
    {
        ContentValues values = new ContentValues();

        values.put(Cliente.NOME    , cliente.getNome());
        values.put(Cliente.TELEFONE    , cliente.getTelefone());
        values.put(Cliente.ANIVERSARIO, cliente.getAniversario().getTime());
        values.put(Cliente.IMAGEM    , cliente.getImagem());


        return values;

    }

    public int excluir(long id)
    {
        return conn.delete(Cliente.TABELA, " _id = ? ", new String[]{ String.valueOf( id ) });
    }

    public int alterar(Cliente contato)
    {
        ContentValues values = preencheContentValues(contato);

        return conn.update(Cliente.TABELA, values, " _id = ? ", new String[]{ String.valueOf( contato.getId()) } );

    }

    public long inserir(Cliente contato)
    {
        ContentValues values = preencheContentValues(contato);
       return  conn.insertOrThrow(Cliente.TABELA,null , values);
    }


    public ClienteArrayAdapter buscaClientes(Context context)
    {

        ClienteArrayAdapter adpClientes = new ClienteArrayAdapter(context, R.layout.item_cliente );

        Cursor cursor  =  conn.query(Cliente.TABELA, null, null, null, null, null, null);
        if (cursor.getCount() > 0 )
        {

            cursor.moveToFirst();

            do {

                Cliente cliente = new Cliente();
                cliente.setId( cursor.getLong( cursor.getColumnIndex(Cliente.ID) ) );
                cliente.setNome( cursor.getString( cursor.getColumnIndex(Cliente.NOME ) ) );
                cliente.setTelefone( cursor.getString( cursor.getColumnIndex(Cliente.TELEFONE )) );
                cliente.setAniversario(new Date(cursor.getLong(cursor.getColumnIndex(Cliente.ANIVERSARIO))));
                cliente.setImagem( cursor.getString( cursor.getColumnIndex(Cliente.IMAGEM ) ) );
                adpClientes.add(cliente);

            }while (cursor.moveToNext());

        }

        cursor.close();
        return adpClientes;



    }

    public List<Cliente> buscaClientesList()
    {

        List<Cliente> listaClientesSalvos = new ArrayList<Cliente>();

        Cursor cursor  =  conn.query(Cliente.TABELA, null, null, null, null, null, null);

        if (cursor.getCount() > 0 )
        {

            cursor.moveToFirst();

            do {

                Cliente cliente = new Cliente();
                cliente.setId( cursor.getLong( cursor.getColumnIndex(Cliente.ID) ) );
                cliente.setNome( cursor.getString( cursor.getColumnIndex(Cliente.NOME ) ) );
                cliente.setAniversario(new Date(cursor.getLong(cursor.getColumnIndex(Cliente.ANIVERSARIO))));
                cliente.setTelefone( cursor.getString( cursor.getColumnIndex(Cliente.TELEFONE ) ) );
                listaClientesSalvos.add(cliente);

            }while (cursor.moveToNext());

        }
        cursor.close();
        return listaClientesSalvos;

    }

    public ContatoArrayAdapter buscaContatos(Context context){

        ContatoArrayAdapter adpContatos = new ContatoArrayAdapter(context, R.layout.item_contato );

        List<Cliente> contatos = new ContatosProvider().getContatos(context);

        for (Cliente cliente: contatos ) {

            adpContatos.add(cliente);

        }

        return adpContatos;

    }

}
