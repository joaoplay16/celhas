package status200.com.br.celhas.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import status200.com.br.celhas.ActivityInfoContato;
import status200.com.br.celhas.model.Cliente;

/**
 * Created by joao on 26/10/17.
 */

public class AlarmeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Cliente cliente = (Cliente) intent.getSerializableExtra("cliente");

        Toast.makeText(context, "Alarme disparou", Toast.LENGTH_SHORT).show();

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(context, ActivityInfoContato.class);
        i.putExtra("cliente",cliente);

        int _id = (int) System.currentTimeMillis();
        PendingIntent pi =
                PendingIntent.getActivity(context, _id, i,0);

        NotificationCompat.Builder builder =
                new  NotificationCompat.Builder(context);

        builder.setAutoCancel(true);
        builder.setTicker("Alarme!");
        builder.setContentTitle("Aniversariante!");
        builder.setContentText("Hoje é o aniversário de " + cliente.getNome());
        builder.setContentIntent(pi);
        builder.setOngoing(false);
        builder.setColor(Color.MAGENTA);
        builder.setSmallIcon(android.R.drawable.ic_input_get);

        Notification n = builder.build();

        long notifyID = System.currentTimeMillis();

        nm.notify((int)notifyID, n);

        Log.i("QUEM", cliente.getNome());
    }
}
