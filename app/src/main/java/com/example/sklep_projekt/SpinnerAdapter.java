package com.example.sklep_projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private int[] images;
    private String[] descriptions;
    private double[] prices;

    public SpinnerAdapter(Context context, int[] images, String[] descriptions, double[] prices) {
        super(context, R.layout.spinner_item, descriptions);
        this.context = context;
        this.images = images;
        this.descriptions = descriptions;
        this.prices = prices;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
        TextView textViewPrice = convertView.findViewById(R.id.textViewPrice);

        if (position == 0) {
            imageView.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);

        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(images[position]);
            textViewDescription.setVisibility(View.VISIBLE);
            textViewDescription.setText(descriptions[position]);
            textViewPrice.setVisibility(View.VISIBLE);
            textViewPrice.setText(String.format("%.2f zł", prices[position]));
        }

        return convertView;
    }
}

