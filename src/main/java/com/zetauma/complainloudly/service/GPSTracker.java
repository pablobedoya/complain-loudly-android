package main.java.com.zetauma.complainloudly.service;

import main.java.com.zetauma.complainloudly.R;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// flag de status do GPS
	boolean isGPSEnabled = false;

	// flag de status da rede
	boolean isNetworkEnabled = false;

	Location location;

	private double latitude;
	private double longitude;
	
	// A base para arredondar valor de latitude e longitude
	private static final int PRECISION = (int) Math.pow(10, 7);

	// A dist�ncia m�nima para mudar atualiza��es em metros
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros

	// O tempo m�nimo entre as datas em milissegundos
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minuto

	protected LocationManager locationManager;

	public GPSTracker(Context context) {
		mContext = context;
		getLocation();
	}

	public void getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// Obtendo status do GPS
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// Obtendo status da rede
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// Nenhum provedor de rede est� habilitado
			} else {
				// Primeiro obtem locaiza��o a partir do provedor de rede
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// Se o GPS est� habilitado, obtem lat/long usando os servi�os
				// de GPS
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Para de usar ouvinte GPS Chamar esta fun��o ir� para de usar o GPS no seu
	 * aplicativo
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/*
	 * Fun��o para verificar GPS/Wi-Fi habilitado
	 */
	public boolean canGetLocation() {
		return location != null;
	}

	/*
	 * Fun��o para mostrar di�logo de alerta das configura��es Ao pressionar o
	 * bot�o Settings iniciar� as op��es de configura��es
	 */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle(R.string.titulo_dialogo_alerta_gps);

		alertDialog.setMessage(R.string.mensagem_dialogo_alerta_gps);

		// Ao pressionar bot�o Settings
		alertDialog.setPositiveButton(R.string.settings,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// Ao pressionar bot�o Cancel
		alertDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Mostrando mensagem de alerta
		alertDialog.show();
	}

	/*
	 * Fun��o para obter latitude
	 */
	public double getLatitude() {
		if (location != null)
			latitude = ((PRECISION * location.getLatitude())) / PRECISION;

		return latitude;
	}

	/*
	 * Fun��o para obter longitude
	 */
	public double getLongitude() {
		if (location != null)
			longitude = ((PRECISION * location.getLongitude())) / PRECISION;

		return longitude;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
