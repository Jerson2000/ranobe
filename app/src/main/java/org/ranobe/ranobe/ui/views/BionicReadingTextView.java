package org.ranobe.ranobe.ui.views;


import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BionicReadingTextView extends AppCompatTextView {

    private boolean isBionicEnabled = false;

    public BionicReadingTextView(Context context) {
        super(context);
        init();
    }

    public BionicReadingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BionicReadingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        reloadBionicView();
    }

    public void reloadBionicView() {
        Spannable wordToSpan = new SpannableString(getText());
        int length = wordToSpan.length();
        Matcher matcher = Pattern.compile("([a-zà-ýA-ZÀ-ÝåäöÅÄÖ].*?)[^a-zà-ýA-ZÀ-ÝåäöÅÄÖ'’]").matcher(getText());
        while (matcher.find()) {
            int rangeStart = matcher.start(1);
            int rangeEnd = matcher.end(1);
            int rangeLength = rangeEnd - rangeStart;

            int correctLength;
            if (rangeLength == 1 || rangeLength == 2 || rangeLength == 3) {
                correctLength = 1;
            } else if (rangeLength == 4) {
                correctLength = 2;
            } else {
                correctLength = Math.round(rangeLength * 0.4f);
            }

            wordToSpan.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    Math.min(Math.max(rangeStart, 0), length),
                    Math.min(Math.max(rangeStart + correctLength, 0), length),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        setText(isBionicEnabled?wordToSpan:getText());
    }


    public boolean isBionicEnabled() {
        return isBionicEnabled;
    }

    public void setBionicEnabled(boolean bionicEnabled) {
        isBionicEnabled = bionicEnabled;
        reloadBionicView();
    }
}
