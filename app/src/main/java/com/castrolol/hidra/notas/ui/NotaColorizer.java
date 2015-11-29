package com.castrolol.hidra.notas.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.castrolol.hidra.notas.R;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaColorizer {

    public static final int WHITE = 0;
    public static final int YELLOW = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int RED = 4;
    public static final int PURPLE = 5;



    Context context;

    public NotaColorizer(Context context){
        this.context = context;
    }

    public void changeViewColor(View view, int colorId){
        NotaColorSet colorSet = NotaColorSet.WHITE;
        switch (colorId){
            case YELLOW:
                colorSet = NotaColorSet.YELLOW;
                break;
            case BLUE:
                colorSet = NotaColorSet.BLUE;
                break;
            case GREEN:
                colorSet = NotaColorSet.GREEN;
                break;
            case RED:
                colorSet = NotaColorSet.RED;
                break;
            case PURPLE:
                colorSet = NotaColorSet.PURPLE;
                break;
            case WHITE:
            default:
                break;
        }

        changeViewColor(view, colorSet);
    }


    public void changeViewColor(View view, NotaColorSet colorSet){

        LinearLayout notaLayout = (LinearLayout)view.findViewById(R.id.background_nota);
        LayerDrawable background = (LayerDrawable)notaLayout.getBackground();
        GradientDrawable shape = (GradientDrawable)
                background.findDrawableByLayerId(R.id.color);
        shape.setColor(colorSet.getBackgroundColor());



        TextView[] txtViews = new TextView[]{
                (TextView)notaLayout.findViewById(R.id.txt_nota_titulo),
                (TextView)notaLayout.findViewById(R.id.txt_nota_descricao),
                (TextView)notaLayout.findViewById(R.id.txt_nota_data),
                (TextView)notaLayout.findViewById(R.id.edit_conteudo),
                (TextView)notaLayout.findViewById(R.id.edit_titulo)
        };


        for(TextView txtView : txtViews){
            if(txtView != null){
                txtView.setTextColor(colorSet.getTextColor());
            }
        }

    }

}
