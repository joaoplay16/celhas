package status200.com.br.celhas.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import status200.com.br.celhas.model.Cliente;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by joao on 26/10/17.
 */

public class AlarmeAniversario {
    private static PendingIntent pi;


    public static void definirAlarme(Context context, Cliente cliente) {
        Intent i = new Intent("ALARME");
        i.putExtra("cliente", cliente);
        int _id  = (int) cliente.getId();
        pi = PendingIntent.getBroadcast(context, _id, i, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Date aniversario = cliente.getAniversario();
        long dataAtual = Calendar.getInstance().getTimeInMillis();

        if(aniversario.getTime() < dataAtual){
            Calendar c =Calendar.getInstance();
            int anoAtual = c.get(Calendar.YEAR); //2017
            int anoAniversario = (cliente.getAniversario().getYear()+1900);//2011
            int diferenca = anoAtual - anoAniversario; //6

            c.set(Calendar.DATE, aniversario.getDate());
            c.set(Calendar.MONTH, aniversario.getMonth()+1);
            c.set(Calendar.YEAR, (anoAniversario + diferenca) );
            c.set(Calendar.MINUTE, 1);
            c.set(Calendar.SECOND, 1);

            Log.i("TEMPO", "Ano atual : " + anoAtual);
            Log.i("TEMPO", "anp aniversario : " + anoAniversario);
            Log.i("TEMPO", "Diferenca: " + diferenca);
            Log.i("TEMPO", "Data modificada: dia = " + cliente.getAniversario().getDate() + " mes = "
                    + cliente.getAniversario().getMonth() + " ano = " + c.get(Calendar.YEAR));

            alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            Log.i("ALARME", "Alarma modificado para " + cliente.getNome()
                    + " na data " + c.getTime());
        }else {

            alarmMgr.set(AlarmManager.RTC_WAKEUP, cliente.getAniversario().getTime(), pi);
            Log.i("ALARME", "Alarma para " + cliente.getNome()
                    + " na data " + cliente.getAniversario());
        }

    }
}

