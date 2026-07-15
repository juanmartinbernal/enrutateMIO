package com.enrutatemio.actividades;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.BD.Consultas;
import com.enrutatemio.adapter.ManejoRutas;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.objectos.Ruta_paradas;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

public class InfoRutasActivity extends FragmentActivity implements TextWatcher {

	public ListView lista;
	public EditText filtro;
	public ManejoRutas adapter;
	public ArrayList<Ruta_paradas> rutas = new ArrayList<Ruta_paradas>();
	public String tiporuta;
	public int opcion;
	public TextView tx;

	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rutas);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lista = (ListView) findViewById(R.id.listarutas);
		filtro = (EditText) findViewById(R.id.filtro);
		tx = (TextView) findViewById(R.id.textorutas);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new mostrarTipoRuta().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, tiporuta);
		else
			new mostrarTipoRuta().execute();
	}

	
	class mostrarTipoRuta extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			Bundle extras = getIntent().getExtras();

			if (extras == null) {
				return;
			}
			tiporuta = extras.getString("tiporuta");
			String mostrar = extras.getString("mostrar");
			opcion = extras.getInt("tipo");
			setTitle("Rutas " + mostrar);
			pDialog = new ProgressDialog(InfoRutasActivity.this);
			pDialog.setMessage("Cargando rutas " + tiporuta + "...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		protected String doInBackground(String... args) {

			rutas = Consultas.listarRutasOrdenadas(InfoRutasActivity.this,
					tiporuta);

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

			adapter = new ManejoRutas(InfoRutasActivity.this, rutas, opcion);
			lista.setTextFilterEnabled(true);
			filtro.addTextChangedListener(InfoRutasActivity.this);
			AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(
					adapter);
			animAdapter.setAbsListView(lista);
			lista.setAdapter(animAdapter);

		}

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
		Intent intento = new Intent(InfoRutasActivity.this,
				FragmentChangeActivity.class);
		intento.putExtra("fromnews", 1);
		startActivity(intento);
		overridePendingTransition(R.anim.transition_frag_in,
				R.anim.transition_frag_out);
	}

}