package com.roguekingapps.jokesby.ui.url;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class UrlSpanNoUnderLine extends URLSpan {

    public UrlSpanNoUnderLine(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
