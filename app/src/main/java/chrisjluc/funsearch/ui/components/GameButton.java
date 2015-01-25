package chrisjluc.funsearch.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import chrisjluc.funsearch.R;

public class GameButton extends Button {

    private static Typeface typeface;

    public GameButton(Context context) {
        super(context);
        setTypeFace(context);
        setAppearance();
    }

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
        setAppearance();
    }

    public GameButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeFace(context);
        setAppearance();
    }

    private void setAppearance(){
        this.setBackgroundResource(R.drawable.curvedbutton);
    }

    private void setTypeFace(Context context) {
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/gothic.ttf");
        this.setTypeface(typeface);
        this.setTextColor(Color.WHITE);
        this.setTextSize(24);
    }
}
