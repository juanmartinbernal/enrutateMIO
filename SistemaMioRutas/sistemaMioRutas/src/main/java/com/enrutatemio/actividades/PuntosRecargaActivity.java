package com.enrutatemio.actividades;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.enrutatemio.R;
import com.enrutatemio.adapter.AdapterRecargas;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.objectos.PuntoRecarga;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

public class PuntosRecargaActivity extends FragmentActivity implements TextWatcher
{
	private ListView listarecargas;
	private EditText buscar;
	public AdapterRecargas adapter;
	public ArrayList<PuntoRecarga> pr = new ArrayList<PuntoRecarga>();

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.recargas);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		listarecargas = (ListView) findViewById(R.id.listarecargas);
		buscar        = (EditText) findViewById(R.id.buscarpunto);
		this.getActionBar().setIcon(R.drawable.iconotarjetarecarga);
	    setTitle("  "+getResources().getString(R.string.puntos_recarga));
	    new CargarPuntosdeRecarga().execute();
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		adapter.getFilter().filter(s.toString());
	}
	 class CargarPuntosdeRecarga extends AsyncTask<String, String, String> {
		 
		 	protected String doInBackground(String... args) {
				
		 		JSONArray obj;
				pr.clear();
				try {

					obj = new JSONArray(loadJSONFromAsset());

					for (int i = 0, len = obj.length(); i < len; ++i) {

						JSONObject tmp = obj.getJSONObject(i);
						String nombre = tmp.getString("NOMBRE");
						String direccion = tmp.getString("DIRECCION");
						String tipo = tmp.getString("TIPO");
						pr.add(new PuntoRecarga(nombre,
								direccion, tipo));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(String file_url) {
				listarecargas.setTextFilterEnabled(true);
				buscar.addTextChangedListener(PuntosRecargaActivity.this); 
				adapter =  new AdapterRecargas(PuntosRecargaActivity.this, pr);
				AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(adapter);
			    animAdapter.setAbsListView(listarecargas);
			    listarecargas.setAdapter(animAdapter);
			}

		}
	  @Override
		public void onBackPressed() {
			finishedActivity();
			super.onBackPressed();

		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			finishedActivity();
			return super.onOptionsItemSelected(item);
		}

		public void finishedActivity() {
			finish();
			Intent intento = new Intent(PuntosRecargaActivity.this,
					FragmentChangeActivity.class);
			intento.putExtra("fromnews", 1);
			startActivity(intento);
			overridePendingTransition(R.anim.transition_frag_in,
					R.anim.transition_frag_out);
		}
		public String loadJSONFromAsset() {
			String json = null;
			try {

				InputStream is = getAssets().open(
						"puntos_recarga.json");

				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				json = new String(buffer, "UTF-8");

			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
			return json;

		}

	
}//cierre de actividad