package status200.com.br.celhas.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import status200.com.br.celhas.R;
import status200.com.br.celhas.model.Cliente;

public class ImageTask extends AsyncTask<String, String, Void> {

	// Componentes, que precisam ser acessados pela task
	private ImageView imagem;
	private Cliente cliente;

	// Construtor
	public ImageTask(Cliente cliente, ImageView imagem) {
		this.imagem = imagem;
		this.cliente = cliente;
	}

	@Override
	// Este método executa numa thread a parte
	protected Void doInBackground(String... params) {

		publishProgress(cliente.getImagem());



		return null;
	}

	@Override
	// Este método executa diretamente na UI thread, portanto pode alterar a
	// interface gráfica
	protected void onProgressUpdate(String... values) {
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
	}

	@Override
	// Este método é executado antes do início da task
	protected void onPreExecute() {

	}

	@Override
	// Este método é executado após a finalização da task
	protected void onPostExecute(Void result) {

	}
}
