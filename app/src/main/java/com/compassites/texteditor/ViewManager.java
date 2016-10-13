package com.compassites.texteditor;

import android.view.View;
import android.widget.TextView;

public class ViewManager {

    public static void enableTheViews(View... views) {
        for (View v : views)
            v.setEnabled(true);
    }

    public static void hideTheViews(View... views) {
        for (View v : views)
            v.setVisibility(View.GONE);
    }

    public static void showTheViews(View... views) {
        for (View v : views)
            v.setVisibility(View.VISIBLE);
    }

    public static void invisibleTheViews(View... views) {
        for (View v : views)
            v.setVisibility(View.INVISIBLE);
    }

    public static void disableTheViews(View... views) {
        for (View v : views)
            v.setEnabled(false);

    }

    public static void deselectTheViews(View... views) {
        for (View v : views) {
            v.setSelected(false);
        }
    }

    public static void selectTheViews(View... views) {
        for (View v : views) {
            v.setSelected(true);
        }
    }

    public static void setTagToViews(View... views) {
        for (View v : views) {
            v.setTag(((TextView) v).getText());
        }
    }

    public static boolean isVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }

    public static void toggleViewVisibility(View view) {
        if (view.isShown()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
