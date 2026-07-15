package com.enrutatemio.actividades;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.enrutatemio.R;
import com.enrutatemio.BD.AdminSQLiteOpenHelper;
import com.enrutatemio.adapter.ManejoRutas;
import com.enrutatemio.adapter.Recorrido2;
import com.enrutatemio.fragmentos.FragmentChangeActivity;
import com.enrutatemio.objectos.Ruta_paradas;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingLeftInAnimationAdapter;

public class AlimentadoresActivity extends FragmentActivity implements TextWatcher{
	
	public static ManejoRutas adapter;
	private ListView listaalimentadores;
	
	private EditText buscar;
	private Spinner  combo;
	private ArrayList<Ruta_paradas> alimentadores = new ArrayList<Ruta_paradas>();
	private String[] sentidoruta = {"sentido sur - norte", "No hay sentido norte - sur en esta ruta"};
	public int postItem = 0;
	public String ruta = "";
	private String parada = "";
	private ProgressDialog pDialog;
	public static final String SOUTH = "sur";
	public static final String NORTH = "norte";
	public static final String CENTER = "centro";
	public static final String ALL = "todas";
	public static final String EAST = "oriente";
	public static final String WEST = "occidente";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listaalimentadores);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    //inicializo componentes de la GUI
		initComponentes();
	
		//sur, centro,norte,todas
		combo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		   
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				long zonaEscogida = parent.getItemIdAtPosition(pos);
				String zona = "";
				if(zonaEscogida == 3)
				{
					zona = SOUTH;
					//texto.setText("Rutas alimentadoras zona Sur");
					setTitle("Alimentadores zona Sur");
				}
				else if(zonaEscogida == 1)
				{
					zona = CENTER;
					//texto.setText("Rutas alimentadoras zona Centro");
					setTitle("Alimentadores zona Centro");
				}
				else if(zonaEscogida == 2)
				{
					zona = NORTH;
					//texto.setText("Rutas alimentadoras zona Norte");
					setTitle("Alimentadores zona Norte");
				}
				else if(zonaEscogida == 0)
				{
					zona = ALL;
					//texto.setText("Todas las rutas alimentadoras");
					setTitle("Todas las rutas alimentadoras");
				}
				else if(zonaEscogida == 4)
				{
					zona = EAST;
					//texto.setText("Rutas alimentadoras zona Oriente");
					setTitle("Alimentadores zona Oriente");
				}
				else if(zonaEscogida == 5)
				{
					zona = WEST;
					//texto.setText("Rutas alimentadoras zona Occidente");
					setTitle("Alimentadores zona Occidente");
				}
			
				alimentadores = cargarAlimentadores(zona);
				adapter = new ManejoRutas(AlimentadoresActivity.this, alimentadores, 4);
				AnimationAdapter animAdapter = new SwingLeftInAnimationAdapter(adapter);
			    animAdapter.setAbsListView(listaalimentadores);
				listaalimentadores.setTextFilterEnabled(true);
				buscar.addTextChangedListener(AlimentadoresActivity.this); 
				listaalimentadores.setAdapter(animAdapter);
				   
				adapter.notifyDataSetChanged();
				
			}
		});
	
	}

	 class sentidosHilo extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				pDialog = new ProgressDialog(AlimentadoresActivity.this);
				pDialog.setMessage(getResources().getString(R.string.buscando_sentidos));
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				
			}
			protected String doInBackground(String... args) {
				
				sentidodelaruta(ruta, sentidoruta);

				return null;
			}
			protected void onPostExecute(String file_url) {
				pDialog.dismiss();
				
				busquedaSentidos();
				
			}

		}
	 public String[] sentidodelaruta(String ruta, String g[]) {

			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
			SQLiteDatabase bd = admin.getWritableDatabase();
			String sen = "";
			String paradas = "";
			Cursor fila = bd.rawQuery(
					"select sentido, paradas from ruta_paradas where ruta LIKE '%"
							+ ruta + "%'", null);

			if (fila != null) {
				while (fila.moveToNext()) {
					sen = fila.getString(0);
					paradas = fila.getString(1);
					if (sen.equalsIgnoreCase("surnorte")) {
						String par[] = paradas.split(",");
						g[0] = par[0] + " - " + par[par.length - 1];

					} else {
						String par[] = paradas.split(",");
						g[1] = par[0] + " - " + par[par.length - 1];

					}
				}
				fila.close();
				bd.close();
				admin.close();
			}

			return g;

		}
	 public void busquedaSentidos()
	 {
		 new AlertDialog.Builder(
				 AlimentadoresActivity.this)
					.setIcon(R.drawable.inicio)
					.setTitle("Ruta "+ ruta.toUpperCase()+ " Selecciona el sentido")
					.setAdapter(new Recorrido2(AlimentadoresActivity.this,sentidoruta),
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int item) {
									String para = parada;

									if (item == 0) {
										// sur -
										// norte

										Intent infoRuta = new Intent(AlimentadoresActivity.this,
												ParadasActivity.class);
										infoRuta.putExtra("Ruta",ruta);
										infoRuta.putExtra("sentido",0);
										infoRuta.putExtra("titulo",para);
										infoRuta.putExtra("direccion",sentidoruta[0]);
										infoRuta.putExtra("desde", 1);
										AlimentadoresActivity.this.startActivity(infoRuta);

									} else if (item == 1) {
										// norte
										// - sur

										Intent infoRuta = new Intent(
												AlimentadoresActivity.this,
												ParadasActivity.class);
										infoRuta.putExtra("Ruta",ruta);
										infoRuta.putExtra("sentido",1);
										infoRuta.putExtra("titulo",para);
										infoRuta.putExtra("direccion",sentidoruta[1]);
										infoRuta.putExtra("desde", 1);
										AlimentadoresActivity.this.startActivity(infoRuta);

									} 
								}
							})

					.setPositiveButton(
							getResources().getString(R.string.back_to_map),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
                                    finish();
									Intent infoRuta = new Intent(
											AlimentadoresActivity.this,
											FragmentChangeActivity.class);
									infoRuta.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									AlimentadoresActivity.this
											.startActivity(infoRuta);

								}
							})
					.setNegativeButton(
							R.string.salir,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {

									dialog.cancel();

								}
							}).show();
	 }
	public ArrayList<Ruta_paradas> cargarAlimentadores(String zona)
	{
		 ArrayList<Ruta_paradas> alimen = new ArrayList<Ruta_paradas>();
		 AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
		 SQLiteDatabase bd = admin.getWritableDatabase();
			Cursor fila = null;
			if(zona.equalsIgnoreCase(ALL))
			{
				fila = bd.rawQuery(
						"select DISTINCT(ruta), trayecto,horario, paradas from ruta_paradas where tipo = 'alimentador' and estado = 'A' and sentido = 'nortesur'", null);
			}
			else
			{
				fila = bd.rawQuery(
						"select DISTINCT(ruta), trayecto,horario, paradas from ruta_paradas where zona = '" + zona
								+ "' and tipo = 'alimentador' and estado = 'A' and sentido = 'nortesur' ", null);
	
			}

		

			if (fila != null) {
				while (fila.moveToNext())
				{
					Ruta_paradas rutaParadas = new Ruta_paradas(fila.getString(0),fila.getString(1) ,fila.getString(2),fila.getString(3));
	                alimen.add(rutaParadas);

				}

			} 
			fila.close();
			bd.close();
			admin.close();
		 
		 
		 
		 
		 return alimen;
	}
	public void initComponentes()
	{
		listaalimentadores = (ListView) findViewById(R.id.listaalimentadores);
		buscar             = (EditText) findViewById(R.id.filtroalimentadores);
		combo              = (Spinner)  findViewById(R.id.spinner1);
		//Creamos el adaptador
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.zonas,android.R.layout.simple_spinner_item);
		//Añadimos el layout para el menú
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Le indicamos al spinner el adaptador a usar
		combo.setAdapter(adapter);
	}
	
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		adapter.getFilter().filter(s.toString());
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finishedActivity();
		super.onBackPressed();

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finishedActivity();
		return super.onOptionsItemSelected(item);
	}

	public void finishedActivity() {
		finish();
		Intent intento = new Intent(AlimentadoresActivity.this,
				FragmentChangeActivity.class);
		intento.putExtra("fromnews", 1);
		startActivity(intento);
		overridePendingTransition(R.anim.transition_frag_in,
				R.anim.transition_frag_out);
	}

}