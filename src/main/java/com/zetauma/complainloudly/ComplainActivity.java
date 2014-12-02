package main.java.com.zetauma.complainloudly;

import com.google.android.gms.maps.model.LatLng;

import main.java.com.zetauma.complainloudly.database.DatabaseHelper;
import main.java.com.zetauma.complainloudly.model.Complaint;
import main.java.com.zetauma.complainloudly.service.GPSTracker;
import main.java.com.zetauma.complainloudly.util.ReverseGeocoding;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ComplainActivity extends Activity {
	private Button buttonSelecionarImagem;
	private Button buttonSelecionarLocal;
	private Button buttonEnviarReclamacao;
	private ImageView imageViewImagem;
	private EditText editTextReclamacao;
	private Intent takePictureIntent = null;
	protected static final int GALLERY_PICTURE_REQUEST = 0;
	protected static final int CAMERA_REQUEST = 1;
	Bitmap imageBitmap;
	DatabaseHelper databaseHelper;
	GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complain);
		
		databaseHelper = new DatabaseHelper(getApplicationContext());
		gps = new GPSTracker(getApplicationContext());

		imageViewImagem = (ImageView) findViewById(R.id.imageViewImagem);
		editTextReclamacao = (EditText) findViewById(R.id.editTextReclamacao);
		
		buttonSelecionarImagem = (Button) findViewById(R.id.buttonSelecionarImagem);
		buttonSelecionarImagem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage();
			}
		});
		
		buttonSelecionarLocal = (Button) findViewById(R.id.buttonSelecionarLocal);
		buttonSelecionarLocal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng latlng = new LatLng(gps.getLatitude(), gps.getLongitude());
				
				new ReverseGeocoding(ComplainActivity.this).execute(latlng);
			}
		});
		
		buttonEnviarReclamacao = (Button) findViewById(R.id.buttonEnviarReclamacao);
		buttonEnviarReclamacao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user = "";
				String image = "";
				String commentary = editTextReclamacao.getText().toString();
				double lat = gps.getLatitude();
				double lng = gps.getLongitude();
				
				Complaint complaint = new Complaint(user, image, commentary, lat, lng);
				
				databaseHelper.insertComplaint(complaint);
			}
		});
	}

	private void selectImage() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Selecionar imagem");

		alertDialog.setNegativeButton("Câmera",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						takePictureIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent
								.resolveActivity(getPackageManager()) != null) {
							startActivityForResult(takePictureIntent,
									CAMERA_REQUEST);
						}
					}
				});

		alertDialog.setPositiveButton("Galeria",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						takePictureIntent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(takePictureIntent,
								GALLERY_PICTURE_REQUEST);
					}
				});

		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			
			imageBitmap = (Bitmap) extras.get("data");
			imageViewImagem.setImageBitmap(imageBitmap);
		} else if (requestCode == GALLERY_PICTURE_REQUEST && resultCode == RESULT_OK) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			imageBitmap = BitmapFactory.decodeFile(picturePath);
			imageViewImagem.setImageBitmap(imageBitmap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
