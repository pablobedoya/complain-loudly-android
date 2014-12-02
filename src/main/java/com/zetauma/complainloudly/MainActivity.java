package main.java.com.zetauma.complainloudly;

import java.util.ArrayList;
import java.util.List;

import main.java.com.zetauma.complainloudly.database.DatabaseHelper;
import main.java.com.zetauma.complainloudly.model.Complaint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class MainActivity extends Activity {
	private ListView listViewReclamacoes;
	private Button buttonReclamar;
	private Button buttonMinhasReclamacoes;
	ArrayList<String> listItems;
	ArrayAdapter<String> adapter;
	DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		databaseHelper = new DatabaseHelper(getApplicationContext());

		listItems = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listItems);
		listViewReclamacoes = (ListView) findViewById(R.id.listViewReclamacoes);
		listViewReclamacoes.setAdapter(adapter);

		buttonReclamar = (Button) findViewById(R.id.buttonReclamar);
		buttonReclamar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ComplainActivity.class);
				startActivity(intent);
			}
		});

		buttonMinhasReclamacoes = (Button) findViewById(R.id.buttonMinhasReclamacoes);
		buttonMinhasReclamacoes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Complaint> allComplaints = databaseHelper.selectAllComplaints();
				
				// Esvazia a lista para evitar duplicar informações a cada clique
				listItems.clear();
				
				for (Complaint complaint : allComplaints) {
					listItems.add(complaint.getCommentary());
				}
				
				// Avisa ao listview que os dados foram alterados
				adapter.notifyDataSetChanged();
			}
		});
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
