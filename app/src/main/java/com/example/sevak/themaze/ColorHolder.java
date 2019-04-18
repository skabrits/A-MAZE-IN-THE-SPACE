package com.example.sevak.themaze;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class ColorHolder {
    public static final int CHOOSER_LIST = 0xff116600;
    public static final int CHOOSER_LIST_SELECTED = 0xff11aa00;
    public static final void CHOOSE_VIEW_CUSTOMIZE(TextView view){
        view.setTextSize(25);
        view.setShadowLayer(10,10,10,Color.WHITE);
        view.setPadding(50, 20, 10, 10);
        view.setTextColor(ColorHolder.CHOOSER_LIST);
    }
}
