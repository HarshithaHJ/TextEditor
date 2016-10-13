package com.compassites.texteditor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    public static final int IMAGE = 0;
    public static final int VIDEO = 1;
    public static final int FILE = 2;
    public static final int TEXT = 3;

    private List<SampleModel> list;
    private EditorCallbacks mEditorCallbacks;

    public CustomAdapter(List<SampleModel> list, EditorCallbacks editorCallbacks) {
        this.list = list;
        mEditorCallbacks = editorCallbacks;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_editor)
        TextEditor editorView;


        public MyViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }


    public void updateData(List<SampleModel> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_editor_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.editorView.setData(list.get(position), position, mEditorCallbacks);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<SampleModel> getData() {
        return list;
    }

    public interface EditorCallbacks {
        void closeListener(int position, int type);

        void dataModified(SampleModel sampleModel, int pos);
    }
}