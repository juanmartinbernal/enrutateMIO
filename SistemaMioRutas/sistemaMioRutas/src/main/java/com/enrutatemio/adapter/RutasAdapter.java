package com.enrutatemio.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrutatemio.R;
import com.enrutatemio.util.Icons;

public class RutasAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String>  values;

	public RutasAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.rutasadapter, values);
		this.context = context;
		this.values = values;
	}
    
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.rutasadapter, parent, false);
		ImageView image = (ImageView) rowView.findViewById(R.id.imagenrutaparada); 
		TextView parada = (TextView) rowView.findViewById(R.id.rutasparadaoverlayparada);
		String reco = values.get(position);
		char letra = reco.charAt(0);
		parada.setText(" "+reco.toUpperCase());
		if(letra =='E')
		{
			//buses expresos
			parada.setBackgroundColor(Color.parseColor("#ECDF3E"));
			
			image.setImageResource(Icons.BUS_EXPRESS);
		}
		else if(letra == 'P')
		{
			//buses pretroncal
			parada.setBackgroundColor(Color.parseColor("#2E79B9"));
			image.setImageResource(Icons.BUS_PRETRONCAL);
		}
		else if(letra == 'T')
		{
			//bus trocal
			parada.setBackgroundColor(Color.parseColor("#E30613"));
			image.setImageResource(Icons.BUS_TRONCAL);
		}
		else if(letra == 'A')
		{
			//bus alimentador
			parada.setBackgroundColor(Color.parseColor("#5FB985"));
			image.setImageResource(Icons.BUS_ALIMENTADOR);
		}
		else
			image.setImageResource(Icons.BUS_ALIMENTADOR);
		
		

		return rowView;
	}
	
		  

}