package com.tied.android.tiedapp.customs.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatEditText;

/**
 * Created by Femi on 7/20/2016.
 */


public class MyEditText extends AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context) {
        super(context);

    }


    public void setIsEditable(boolean editable) {
        if(editable) {
           // setFilters(null);
        }else{
            setLongClickable(false);
            setFocusableInTouchMode(false);
            setClickable(false);

            setFilters(new InputFilter[] {
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
                        }
                    }
            });
        }
    }



}
