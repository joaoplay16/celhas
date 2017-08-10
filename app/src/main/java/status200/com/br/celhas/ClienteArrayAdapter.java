package status200.com.br.celhas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import status200.com.br.celhas.model.Cliente;

/**
 * Created by PauloVinicius on 16/05/2015.
 */
public class ClienteArrayAdapter extends ArrayAdapter< Cliente >{

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;


    public ClienteArrayAdapter(Context context, int resource)
    {
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View view = null;
        ViewHolder viewHolder = null;
        final Cliente cliente = getItem(position);

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);


            viewHolder.txtNome = (TextView)view.findViewById(R.id.txtNome);
            viewHolder.txtTelefone = (TextView)view.findViewById(R.id.txtTelefone);
//            viewHolder.checkContato = (CheckBox)view.findViewById(R.id.checkContato);

            view.setTag(viewHolder);
//            viewHolder.checkContato.setChecked(cliente.isChecked());
//            viewHolder.checkContato.setTag(cliente);
            convertView = view;

        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.checkContato = (CheckBox) viewHolder.checkContato.getTag();

                viewHolder.checkContato.setChecked(cliente.isChecked());

            view = convertView;
        }


//        viewHolder.checkContato.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox check = (CheckBox) view;
//                if(check.isChecked()){
//                    cliente.setChecked(true);
//                }else {
//                    cliente.setChecked(false);
//                }
//            }
//        });


        viewHolder.txtNome.setText(cliente.getNome());
        viewHolder.txtTelefone.setText(cliente.getTelefone());

        return view;

    }

    static class ViewHolder
    {

        TextView txtNome;
        TextView txtTelefone;
        CheckBox checkContato;
    }


}
