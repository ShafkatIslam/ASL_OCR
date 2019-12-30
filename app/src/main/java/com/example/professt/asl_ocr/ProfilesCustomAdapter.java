package com.example.professt.asl_ocr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfilesCustomAdapter  extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ProfileInfo> objects1;

    public ProfilesCustomAdapter(Context context, ArrayList<ProfileInfo> products) {
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
        View rowView = infalter.inflate(R.layout.lists_profile, parent, false);

        LinearLayout profile = (LinearLayout)rowView.findViewById(R.id.profiles);
        TextView textView = (TextView) rowView.findViewById(R.id.profiles_text);
        TextView textView1 = (TextView) rowView.findViewById(R.id.profiles_text1);


        CheckBox cbBuy = (CheckBox) rowView.findViewById(R.id.select_check_box);

        final ProfileInfo profileInfo = getFriends(position);



            cbBuy.setOnCheckedChangeListener(myCheckChangList);
            cbBuy.setTag(position);
            cbBuy.setChecked(profileInfo.select);


        textView.setText(profileInfo.getName());
        textView1.setText(profileInfo.getCompany());


        return rowView;

    }

    private ProfileInfo getFriends(int position) {
        return ((ProfileInfo) getItem(position));
    }

    ArrayList<ProfileInfo> getBox() {
        ArrayList<ProfileInfo> box = new ArrayList<ProfileInfo>();
        for (ProfileInfo friends : objects1) {
            if (friends.select)
                box.add(friends);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {

        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getFriends((Integer) buttonView.getTag()).select = isChecked;

        }
    };
}
