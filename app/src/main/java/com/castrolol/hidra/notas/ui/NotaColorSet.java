package com.castrolol.hidra.notas.ui;

import android.graphics.Color;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaColorSet {

    public static final NotaColorSet WHITE = new NotaColorSet(Color.WHITE, Color.argb(255, 60, 60, 60));
    public static final NotaColorSet YELLOW = new NotaColorSet(Color.argb(255,252,251,177), Color.argb(255, 60, 60, 60));
    public static final NotaColorSet BLUE = new NotaColorSet(Color.parseColor("#50D0FF"), Color.argb(255, 60, 60, 60));
    public static final NotaColorSet PURPLE = new NotaColorSet(Color.argb(255, 177, 179, 252), Color.argb(255, 60, 60, 60));
    public static final NotaColorSet GREEN = new NotaColorSet(Color.argb(255, 177, 252, 213), Color.argb(255, 60, 60, 60));
    public static final NotaColorSet RED = new NotaColorSet(Color.argb(255, 252, 177, 217), Color.argb(255, 60, 60, 60));


    private int backgroundColor;
    private int textColor;

    public NotaColorSet(int bg, int txt){
        setBackgroundColor(bg);
        setTextColor(txt);
    }


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
