package com.example.professt.asl_ocr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileCustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ProfileInfo> objects1;

    public ProfileCustomAdapter(Context context, ArrayList<ProfileInfo> products) {
        ctx = context;
        objects1 = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects1.size();
    }

    @Override
    public Object getItem(int position) {
        return objects1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater infalter = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = infalter.inflate(R.layout.list_profile, parent, false);

        LinearLayout profile = (LinearLayout)rowView.findViewById(R.id.profile);
        TextView textView = (TextView) rowView.findViewById(R.id.profile_text);
        TextView textView1 = (TextView) rowView.findViewById(R.id.profile_text1);

        final ProfileInfo profileInfo = getFriends(position);

        textView.setText(profileInfo.getName());
        textView1.setText(profileInfo.getCompany());


        profile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(ctx, ProfileViewActivity.class);
                i.putExtra(ProfileViewActivity.PROFILE_ID_KEY, profileInfo.getId());
                ctx.startActivity(i);
            }
        });

        return rowView;

    }

    private ProfileInfo getFriends(int position) {
        return ((ProfileInfo) getItem(position));
    }
}
