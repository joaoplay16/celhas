package status200.com.br.celhas.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import status200.com.br.celhas.model.Cliente;

/**
 * Created by joao on 12/08/17.
 */

public class FIltraDados implements TextWatcher{
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
