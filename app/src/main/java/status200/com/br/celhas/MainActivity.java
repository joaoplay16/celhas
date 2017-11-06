package status200.com.br.celhas;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import status200.com.br.celhas.R;

public class MainActivity extends AppCompatActivity {
    private TextView txtCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCliente = (TextView)findViewById(R.id.txtCliente);
        String fontPath = "fonts/AMERSN.ttf";
        Typeface typeface= Typeface.createFromAsset(getAssets(), fontPath);
        txtCliente.setTypeface(typeface);
    }

    public void clientes(View v){
        Intent i = new Intent(this, ClientesActivity.class);
        startActivity(i);
    }
}
