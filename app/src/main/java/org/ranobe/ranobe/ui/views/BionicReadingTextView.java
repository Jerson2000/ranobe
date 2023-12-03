package org.ranobe.ranobe.ui.views;


import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

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
        CharSequence originalText = getText();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String[] sentences = originalText.toString().split("\\.\\s+");

        int sentenceCount = 0;
        if(isBionicEnabled){
            for (String sentence : sentences) {
                String[] words = sentence.split("\\s+");

                for (String word : words) {
                    int boldCount = isBionicEnabled ? (int) Math.ceil(word.length() * 0.5) : 0;

                    for (int i = 0; i < word.length(); i++) {
                        char currentChar = word.charAt(i);
                        spannableStringBuilder.append(currentChar);
                        if (isBionicEnabled && i < boldCount) {
                            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }

                    spannableStringBuilder.append(" ");
                }
                sentenceCount++;

                if (sentenceCount % 5 == 0) {
                    spannableStringBuilder.append("\n\n");
                }
            }
        }

        setText(spannableStringBuilder);
    }


    public boolean isBionicEnabled() {
        return isBionicEnabled;
    }

    public void setBionicEnabled(boolean bionicEnabled) {
        isBionicEnabled = bionicEnabled;
        reloadBionicView();
    }
}
