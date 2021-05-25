package com.lionas.ruwn.fairy;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.List;

/**
 * Created by ruwn on 2017-02-26.
 */

public class LionaComAdapter extends BaseAdapter {
    private List liona;
    private Context context;

    public LionaComAdapter(List liona, Context context) {
        this.liona = liona;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.liona.size();
    }

    @Override
    public Object getItem(int position) {
        return this.liona.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = new LinearLayout(context);
            ((LinearLayout) convertView).setOrientation(LinearLayout.HORIZONTAL);

            TextView txtId = new TextView(context);
            txtId.setPadding(10, 0, 20, 0);
            txtId.setTextColor(Color.rgb(0,0,0));

            TextView txtType = new TextView(context);
            txtType.setPadding(20,0,20,0);
            txtType.setTextColor(Color.rgb(0,0,0));

            TextView txtDetail = new TextView(context);
            txtDetail.setPadding(20,0,20,0);
            txtDetail.setTextColor(Color.rgb(0,0,0));

            TextView txtWord = new TextView(context);
            txtWord.setPadding(20,0,20,0);
            txtWord.setTextColor(Color.rgb(0,0,0));

            ((LinearLayout)convertView).addView(txtId);
            ((LinearLayout)convertView).addView(txtType);
            ((LinearLayout)convertView).addView(txtDetail);
            ((LinearLayout)convertView).addView(txtWord);

            holder = new Holder();
            holder.txtId = txtId;
            holder.txtType = txtType;
            holder.txtDetail = txtDetail;
            holder.txtWord = txtWord;

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        LionaCommunication lionaCommunication = (LionaCommunication) getItem(position);
        holder.txtId.setText(lionaCommunication.get_id() + "");
        holder.txtType.setText(lionaCommunication.getType());
        holder.txtDetail.setText(lionaCommunication.getDetail());
        holder.txtWord.setText(lionaCommunication.getWords());

        return convertView;
    }

    private class Holder {
        public TextView txtId;
        public TextView txtType;
        public TextView txtDetail;
        public TextView txtWord;
    }
}