package com.thedeveloperworldisyours.evaluationtask.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thedeveloperworldisyours.evaluationtask.R;
import com.thedeveloperworldisyours.evaluationtask.models.Article;
import com.thedeveloperworldisyours.evaluationtask.models.Item;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 8/5/15.
 */
public class ListAdapter extends ArrayAdapter<Item> {

    private Activity mActivity;
    private List<Item> mListArtist;

    public ListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        mActivity = (Activity) context;
        mListArtist = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.item_list_firstLine);
        TextView genres = (TextView) rowView.findViewById(R.id.item_list_secondLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_list_icon);

        name.setText(mListArtist.get(position).getTitle());
        genres.setText(mListArtist.get(position).getSubtitle());


        return rowView;
    }

}

