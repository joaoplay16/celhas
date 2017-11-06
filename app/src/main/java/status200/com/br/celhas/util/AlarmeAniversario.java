package status200.com.br.celhas.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import status200.com.br.celhas.model.Cliente;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by joao on 26/10/17.
 */

public class AlarmeAniversario {
    private static PendingIntent pi;

    public static void definirAlarme(Context context, Cliente cliente){
        Intent i = new Intent("ALARME");
        i.putExtra("cliente",cliente);
        int _id = (int) System.currentTimeMillis();
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        pi = PendingIntent.getBroadcast(context, _id, i, PendingIntent.FLAG_ONE_SHOT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, cliente.getAniversario().getTime(), pi);
        Log.i("ALARME", "Alarma para " + cliente.getNome() +" na data " + cliente.getAniversario());
    }
}
