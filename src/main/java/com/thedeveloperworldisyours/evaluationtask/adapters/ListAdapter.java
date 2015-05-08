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

    static class ViewHolder {
        public TextView title;
        public TextView subtitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item,parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.item_list_firstLine);
            viewHolder.subtitle = (TextView) rowView.findViewById(R.id.item_list_secondLine);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.title.setText(mListArtist.get(position).getTitle());
        holder.subtitle.setText(mListArtist.get(position).getSubtitle());

        return rowView;
    }

}

