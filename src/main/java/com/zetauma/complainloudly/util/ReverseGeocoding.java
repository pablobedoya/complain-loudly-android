package main.java.com.zetauma.complainloudly.util;

import main.java.com.zetauma.complainloudly.R;

import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.TextView;

public class ReverseGeocoding extends AsyncTask<LatLng, String, String> {
	private Context mContext;
    ProgressDialog progressDialog;
    
    private TextView textViewLocal;
	
	public ReverseGeocoding(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		// Diálogo de progresso cancelável
		progressDialog = ProgressDialog.show(mContext, "Selecionar local",
				"Procurando...", true, true,
				// Passa um Cancel Listener para a caixa de diálogo do processo
				new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						// Quando o diálogo é cancelado, é preciso cancelar a
						// tarefa explicitamente, caso contrário ela continua
						// sendo executada
						cancel(true);
					}
				});

		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected String doInBackground(LatLng... params) {
		String lat = Double.toString(params[0].latitude);
		String lng = Double.toString(params[0].longitude);
		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true";
		
		try {
			JSONObject jsonObj = JsonParser.getJSONfromURL(url);
			JSONObject location;
			String result;
			
			//Get JSON Array called "results" and then get the 0th complete object as JSON        
		    location = jsonObj.getJSONArray("results").getJSONObject(0); 
		    // Get the value of the attribute whose name is "formatted_string"
		    result = location.getString("formatted_address");
		    
		    return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		progressDialog.dismiss();
		
		// Passando referência Context via construtor
		textViewLocal = (TextView) ((Activity)mContext).findViewById(R.id.textViewLocal);
		
		textViewLocal.setText(result);
	}

}
