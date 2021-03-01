package com.app.givebackrx.appcode.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoSuggestAdapter extends ArrayAdapter<String> /*implements Filterable*/ {

    private List<String> mlistData;
    private int resource;

    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.resource=resource;
        mlistData = new ArrayList<>();
    }

    public void setData(List<String> list) {
        mlistData.clear();
        mlistData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mlistData.get(position);
    }

    public String getObject(int position) {
        return mlistData.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        try {
            if (view == null) {
                view = LayoutInflater.from(convertView.getContext()).inflate(resource, parent, false);
            }
            ((TextView) view.findViewById(android.R.id.text1)).setText(getObject(position));
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

   /* @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }*/
}