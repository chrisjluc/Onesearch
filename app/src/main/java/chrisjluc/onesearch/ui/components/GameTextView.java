package chrisjluc.onesearch.ui.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import chrisjluc.onesearch.R;
import chrisjluc.onesearch.utils.DeviceUtils;

public class GameTextView extends TextView {

    private static Typeface typeface;

    public GameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeFace(context);
    }

    public GameTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeFace(context);
    }

    public GameTextView(Context context) {
        super(context);
        setTypeFace(context);
    }

    private void setTypeFace(Context context) {
        if(typeface == null)
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/gothic.ttf");
        this.setTypeface(typeface);

    }
}
