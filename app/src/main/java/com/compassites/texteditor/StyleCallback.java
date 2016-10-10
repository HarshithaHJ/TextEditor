package com.compassites.texteditor;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

class StyleCallback implements  ActionMode.Callback {

    //    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        Log.d(TAG, "onCreateActionMode");
//        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.option, menu);
//        menu.removeItem(android.R.id.selectAll);
//        Log.d("LOGTAG", "content selected");
//        return true;
//    }
//
//    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//        return false;
//    }
//
//    private EditText bodyView;
//
//    public void setEditText(EditText editText) {
//        this.bodyView = editText;
//    }
//
//    //
//    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        Log.d(TAG, String.format("onActionItemClicked item=%s/%d", item.toString(), item.getItemId()));
//        CharacterStyle cs;
//        int start = bodyView.getSelectionStart();
//        int end = bodyView.getSelectionEnd();
//        SpannableStringBuilder ssb = new SpannableStringBuilder(bodyView.getText());
//
//        switch (item.getItemId()) {
//
//            case R.id.bold:
//                cs = new StyleSpan(Typeface.BOLD);
//                ssb.setSpan(cs, start, end, 1);
//                bodyView.setText(ssb);
//                return true;
//
//            case R.id.italic:
//                cs = new StyleSpan(Typeface.ITALIC);
//                ssb.setSpan(cs, start, end, 1);
//                bodyView.setText(ssb);
//                return true;
//
//            case R.id.underline:
//                cs = new UnderlineSpan();
//                ssb.setSpan(cs, start, end, 1);
//                bodyView.setText(ssb);
//                return true;
//        }
//        return false;
//    }
    private CallbackMethods callbackMethods;

    public void setListener(CallbackMethods callbackMethods) {
        this.callbackMethods = callbackMethods;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        callbackMethods.onSelection();
        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.option, menu);
//        menu.removeItem(android.R.id.selectAll);
        Log.d("LOGTAG", "content selected");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    //
    public void onDestroyActionMode(ActionMode mode) {
        callbackMethods.onSelectionRemoved();
    }

    public interface CallbackMethods {
        public void onSelection();

        public void onSelectionRemoved();
    }
}