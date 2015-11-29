package com.castrolol.hidra.notas.data;

import android.content.Context;
import android.content.Intent;

import com.castrolol.hidra.notas.notas.Nota;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaShareService {

    Context context;

    public NotaShareService(Context context){
        this.context = context;
    }

    public void share(Nota nota){

        context.startActivity(Intent.createChooser(fromNota(nota), "Compartilhar Nota"));
    }

    public static Intent fromNota(Nota nota) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, nota.getConteudo());
        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, nota.toHtml());
        sendIntent.putExtra(Intent.EXTRA_TITLE, nota.getTitulo());
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Nota: " + nota.getTitulo());


        sendIntent.setType("text/plain");
        return sendIntent;
    }
}
