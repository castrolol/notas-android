package com.castrolol.hidra.notas.notas;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.castrolol.hidra.notas.R;
import com.castrolol.hidra.notas.ui.NotaColorSet;
import com.castrolol.hidra.notas.ui.NotaColorizer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaListItemAdapter extends ArrayAdapter<Nota> {

    boolean multiselectable = false;
    List<UUID> notasSelecionadas = new ArrayList<>();
    NotaColorizer colorizer ;

    public interface RequestEditarNota {
        void editarNota(Nota n);
    }

    RequestEditarNota requestEditarNota;

    public NotaListItemAdapter(Context context, List<Nota> notas) {
        super(context, R.layout.nota_list_item_view, R.id.txt_nota_titulo, notas);
        colorizer = new NotaColorizer(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        final Nota n = getItem(position);
        TextView txtTitulo = (TextView) v.findViewById(R.id.txt_nota_titulo);
        TextView txtDescricao = (TextView) v.findViewById(R.id.txt_nota_descricao);
        TextView txtData = (TextView) v.findViewById(R.id.txt_nota_data);


        colorizer.changeViewColor(v, n.getCorDaNota());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEditarNota.editarNota(n);
            }
        });
        v.setLongClickable(true);

        txtTitulo.setText(n.getTitulo());
        txtDescricao.setText(n.getDescricaoResumida());
        txtData.setText(n.getDataLiteral());



        return v;
    }



    public void setRequestEditarNota(RequestEditarNota requestEditarNota) {
        this.requestEditarNota = requestEditarNota;
    }
}