package status200.com.br.celhas.util;

<<<<<<< HEAD
import android.content.Context;
import android.content.res.Resources;
=======
>>>>>>> master
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import status200.com.br.celhas.R;
import status200.com.br.celhas.model.Cliente;

public class ImageTask extends AsyncTask<String, String, Bitmap> {
	// Componentes, que precisam ser acessados pela task
	private ImageView imagem;
	private Cliente cliente;
	private Context context;

	// Construtor
	public ImageTask(Context context,Cliente cliente, ImageView imagem) {
		this.context = context;
		this.imagem = imagem;
		this.cliente = cliente;
	}

	@Override
	// Este método executa numa thread a parte
<<<<<<< HEAD
	protected Bitmap doInBackground(String... params) {
		String image = cliente.getImagem();
		Resources resources = context.getResources();
		if(cliente.getImagem()!= null) {
			try {
				/*Reduzindo a qualidade da imagem para preservar memoria.
				* Aqui você pode testar a redução que melhor atende sua necessidade
				*/
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 20;
				return  BitmapFactory.decodeStream(new FileInputStream(image), null, options);
			} catch (FileNotFoundException e) {
				Log.e("FILE_NOT_FOUND",e.getMessage());
				return BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_help);
			}
		}else{
			return BitmapFactory.decodeResource(resources, R.drawable.user_male);
		}
=======
	protected Void doInBackground(String... params) {

		publishProgress(cliente.getImagem());



		return null;
>>>>>>> master
	}

	@Override
	// Este método executa diretamente na UI thread, portanto pode alterar a
	// interface gráfica
	protected void onProgressUpdate(String... values) {
<<<<<<< HEAD

=======
		String image = values[0];
		if(cliente.getImagem()!= null) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
        /*Reduzindo a qualidade da imagem para preservar memoria.
        * Aqui você pode testar a redução que melhor atende sua necessidade
        */
				options.inSampleSize = 20;

				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, options);
				imagem.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}else{
			imagem.setImageResource(R.drawable.user_male);
		}
>>>>>>> master
	}

	@Override
	// Este método é executado antes do início da task
	protected void onPreExecute() {

	}

	@Override
	// Este método é executado após a finalização da task
	protected void onPostExecute(Bitmap result) {
		imagem.setImageBitmap(result);
	}
}
