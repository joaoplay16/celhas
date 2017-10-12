package status200.com.br.celhas;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import status200.com.br.celhas.model.Cliente;
import status200.com.br.celhas.util.ImageTask;

public class ClienteArrayAdapter extends ArrayAdapter< Cliente >{

    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;


    public ClienteArrayAdapter(Context context, int resource){
        super(context, resource);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;
        final Cliente cliente = getItem(position);

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.txtNome = (TextView)view.findViewById(R.id.txtNome);
            viewHolder.txtTelefone = (TextView)view.findViewById(R.id.txtTelefone);
            viewHolder.imgCliente = (ImageView)view.findViewById(R.id.imgCliente);

            view.setTag(viewHolder);

            convertView = view;

        }else{
            viewHolder = (ViewHolder)convertView.getTag();

            view = convertView;
        }

        viewHolder.txtNome.setText(cliente.getNome());
        viewHolder.txtTelefone.setText(cliente.getTelefone());
//        if(cliente.getImagem()!= null) {
//            viewHolder.imgCliente.setImageBitmap(BitmapFactory.decodeFile(cliente.getImagem()));
//        }else{
//
//            viewHolder.imgCliente.setImageResource(R.drawable.user_male);
//        }

        ImageTask imgTask = new ImageTask(cliente, viewHolder.imgCliente);
        imgTask.execute();
        return view;

    }

    static class ViewHolder
    {
        TextView txtNome;
        TextView txtTelefone;
        ImageView imgCliente;
        CheckBox checkContato;
    }


}
