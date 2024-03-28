package com.fitness.proyecto_tfc.app.usecases.home.fragmentosSegundarios.fragmento_dia_entrenamiento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fitness.proyecto_tfc.R;
import com.fitness.proyecto_tfc.app.model.clases.Ejercicio;
import com.fitness.proyecto_tfc.app.model.clases.Entrenamiento;
import com.fitness.proyecto_tfc.app.usecases.home.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterRutina extends ArrayAdapter<Ejercicio> {
    private ArrayList<Ejercicio> dataset;

    Context mContext;

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
    public CustomAdapterRutina(ArrayList<Ejercicio> data, Context context) {
        super(context, R.layout.fragment_dia_rutina, data);
        this.dataset = data;
        this.mContext = context;
    }
    //Esta funcion seria las que sustituirian el contenido de la imágen de fondo y el texto del view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.diseno_dia_rutina, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.imagenDiaEntrenamientoCard);
            viewHolder.textView = convertView.findViewById(R.id.textoDiaEntrenamientoCard);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Ejercicio item = getItem(position);
        viewHolder.textView.setText(item.getNombre());
        viewHolder.imageView.setImageResource(item.getImagen());

        return result;
    }

}
