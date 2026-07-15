package com.enrutatemio.fragmentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;

import com.enrutatemio.R;
import com.enrutatemio.actividades.AlimentadoresActivity;
import com.enrutatemio.actividades.InfoRutasActivity;
import com.enrutatemio.mapa.ShowMapActivity;
import com.enrutatemio.util.Mensajes;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("NewApi")
public class FragmentChangeActivity extends BaseActivity {

	public static final int DRAW_STATIONS = 0;
	public static final int DELETE_STATIONS = 1;

	private Fragment mContent;
	public ListaEstacionesMIO listaEstacionesMIO;
	public InformacionMIO informacionMIO;
	public Tab3 tweets;
	public Acerca acercade;
	public Noticias noticias;
	public ShowMapActivity mapFragment;
	public int fromnews = 0;
	public int widget = -1;
	public SharedPreferences pref;
	public static final String EXPRESO = "expreso";
	public static final String TRONCAL = "troncal";
	public static final String PRETRONCAL = "pretroncal";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		mapFragment = new ShowMapActivity();
		informacionMIO = new InformacionMIO();
		listaEstacionesMIO = new ListaEstacionesMIO();
		tweets = new Tab3();
		acercade = new Acerca();
		
		Bundle b = getIntent().getExtras();
		if (b != null)
		{
			fromnews = b.getInt("fromnews");
			widget   = b.getInt("widget");
		
			if(widget == 0)
			{
				mContent = mapFragment;
				getActionBar().setTitle(getResources().getString(R.string.planear_viaje));
			}
			else if(widget == 1)
			{
				mContent = listaEstacionesMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.listado_estaciones));
			}
			else if(widget == 2)
			{
				mContent =  informacionMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.informacionrutas));
			}
			else if(widget == 3)
			{
				mContent =  informacionMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.informacionrutas));
				finish();
				Intent intent = new Intent(this, AlimentadoresActivity.class);
				startActivity(intent);
			}
			else if(widget == 4)
			{
				mContent =  informacionMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.informacionrutas));
				finish();
				Intent i = new Intent(this, InfoRutasActivity.class);
				i.putExtra("tiporuta", EXPRESO);
				i.putExtra("mostrar", "Expresos");
				i.putExtra("tipo", 1);
				startActivity(i);

			}
			else if(widget == 5)
			{
				mContent =  informacionMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.informacionrutas));
				finish();
				Intent i = new Intent(this, InfoRutasActivity.class);
				i.putExtra("tiporuta", PRETRONCAL);
				i.putExtra("mostrar", "Pretroncales");
				i.putExtra("tipo", 2);
				startActivity(i);

			}
			else if(widget == 6)
			{
				mContent =  informacionMIO;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				getActionBar().setTitle(getResources().getString(R.string.informacionrutas));
				finish();
				Intent i = new Intent(this, InfoRutasActivity.class);
				i.putExtra("tiporuta", TRONCAL);
				i.putExtra("mostrar", "Troncales");
				i.putExtra("tipo", 3);
				startActivity(i);

			}
			
				
		}
		else
		{
			 if (savedInstanceState != null) {
				 mContent = getSupportFragmentManager().getFragment(
				 savedInstanceState, "mContent");
			 }

			if (mContent == null) {
				mContent = mapFragment;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}
		
		// set the Behind View
		
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new ColorMenuFragment(this)).commit();		
		if(mContent != null)
		{
			// set the Above View
			setContentView(R.layout.content_frame);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, mContent).commit();
			
	
		}
		else
		{
			
				finish();
				Intent intento = new Intent(this,FragmentChangeActivity.class);
				startActivity(intento);
		
		}
		if (fromnews == 1) {
			getSlidingMenu().post(new Runnable() {

				@Override
				public void run() {

					getSlidingMenu().showMenu();

				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			case android.R.id.home:
			
				if (pref.getBoolean("ultimoSlide", false))
					if(mapFragment != null)
						if(mapFragment.contenedortutorial != null)
							mapFragment.contenedortutorial.setVisibility(View.GONE);

				toggle();
				return true;
			case R.id.drawStations:
				
				boolean isDark = pref.getBoolean("Name", false);
				if (isDark) {
					mapFragment.cargarEstaciones(DRAW_STATIONS);
					item.setIcon(R.drawable.iconoestacionmapa);
					
					SharedPreferences.Editor editor = pref.edit();
					editor.putBoolean("Name", false);
					editor.commit();
				} else {
					mapFragment.cargarEstaciones(DELETE_STATIONS);
					item.setIcon(R.drawable.iconoestacionoff);
					SharedPreferences.Editor editor = pref.edit();
					editor.putBoolean("Name", true);
					editor.commit();
				}

				return true;
			case R.id.position:
				
				mapFragment.showPosition();
				return true;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		// inicio
		final SearchView searchView = new SearchView(getActionBar()
				.getThemedContext());
		AutoCompleteTextView searchText = (AutoCompleteTextView) searchView
				.findViewById(R.id.action_mode_bar_stub);
		searchView.setQueryHint(getResources().getString(R.string.buscar2));
		//searchText.setHintTextColor(Color.WHITE);
		//searchText.setTextColor(Color.WHITE);

		// Se crea en la barra superior la lupa para buscar sitios, direcciones
		// asignandole el id = 12
		menu.add(Menu.NONE, 12, 0, "Search")
				.setIcon(android.R.drawable.ic_menu_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {

				if (newText.length() > 0) {
					// Search

				} else {
					// Do something when there's no input
				}
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				searchView.clearFocus();
				// Log.d("busqueda", query);
				if (query != null && query.length() > 0) {
					mapFragment.buscarSite(query);
				} else {
					Mensajes.mensajes(getBaseContext(),
							getResources().getString(R.string.consulta_vacia), 1);
				}

				setProgressBarIndeterminateVisibility(true);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						setProgressBarIndeterminateVisibility(false);
					}
				}, 2000);

				return false;
			}

		});

		return true;
	}

	/**
	 * remplaza los fragmentos del FragmentChangeActivity
	 * 
	 * @param fragment
	 */
	public void replaceFragment(Fragment fragment) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

   	 if (keyCode == KeyEvent.KEYCODE_BACK) {
   		 if(!getSlidingMenu().isMenuShowing())
   		 {
   			finish();
			System.exit(0);
   		 }
//   		 if(GlobalData.isKey("notification", FragmentChangeActivity.this))
//   		 {
//   			GlobalData.delete("notification", FragmentChangeActivity.this);
//   			finish();
//			System.exit(0);
//   		 }
//   		 else
//   		 {
//	   		if (!getSlidingMenu().isMenuShowing()) 
//	   			DialogEnrutate.createDialogCloseApp(this, "Enrútate MIO", this.getResources().getString(R.string.mensajesalir));
//   		 }
   		 return true;
   	 }
		return super.onKeyDown(keyCode, event);
	}
	
}
