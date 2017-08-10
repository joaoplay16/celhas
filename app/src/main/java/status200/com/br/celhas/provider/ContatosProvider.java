package status200.com.br.celhas.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import java.util.ArrayList;
import java.util.List;

import status200.com.br.celhas.model.Cliente;

/**
 * Created by joao on 01/07/17.
 */

public class ContatosProvider {

    private Cliente contato;

    public List<Cliente> getContatos(Context context){
        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        List<Cliente> listaClientes = new ArrayList<Cliente>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    contato = new Cliente();
                    contato.setId(Integer.parseInt(contact_id));
                    contato.setNome(name);

                    // Loop pra cada numero do contato
                    Cursor phoneCursor = contentResolver.query(
                            PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                   while(phoneCursor.moveToNext()) {

                       contato = new Cliente();

                       contato.setNome(name);
                       phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contato.setTelefone(phoneNumber);

                    }
                    listaClientes.add(contato);
                    phoneCursor.close();

            }

        }


        }
        return  listaClientes;
    }
}
