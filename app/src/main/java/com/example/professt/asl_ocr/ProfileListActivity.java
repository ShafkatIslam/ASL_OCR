package com.example.professt.asl_ocr;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfileListActivity extends AppCompatActivity {

    ImageView backId,menuId;
    SearchView nameSearchViewId;

    ProfileDao profileDao;

    Cursor cursor;

    ListView listView;

    ArrayList<ProfileInfo> profileSearches = new ArrayList<ProfileInfo>();

    ProfileCustomAdapter profileCustomAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);

        profileDao = ProfileDao.getInstance(this);
        listView = (ListView) findViewById(R.id.list_view);
        backId = (ImageView)findViewById(R.id.backId);
        menuId = (ImageView)findViewById(R.id.menuId);
        nameSearchViewId = (SearchView)findViewById(R.id.nameSearchViewId);


        nameSearchViewId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getPlanets(newText);
                return false;
            }
        });

        backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        menuId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ProfileListActivity.this, menuId);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        //Toast.makeText(ProfileListActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        String title = String.valueOf(item.getTitle());

                        //Toast.makeText(ProfileListActivity.this,"You Clicked : " + title, Toast.LENGTH_SHORT).show();

                        if(title.equalsIgnoreCase("Select"))
                        {

                            Intent intent = new Intent(ProfileListActivity.this,SelectActivity.class);
                            intent.putExtra("Select",1);
                            startActivity(intent);
                        }
                        else if(title.equalsIgnoreCase("Select All"))
                        {
                            Intent intent = new Intent(ProfileListActivity.this,SelectActivity.class);
                            intent.putExtra("SelectAll",2);
                            startActivity(intent);
                        }


                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        Cursor profileData = profileDao.loadDataForMinimalList();
        if (profileData == null) {
            Utils.displayErrorDialog(this);
        } else {
            List<String> profileItems = new ArrayList<String>();
            for (profileData.moveToFirst(); !profileData.isAfterLast(); profileData.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append(profileData.getString(1))
                        .append(" / ")
                        .append(profileData.getString(2))
                        .append(ProfileArrayAdapter.DELIMITER)
                        .append(profileData.getString(0));
                profileItems.add(sb.toString());
            }
            Log.d(ProfileListActivity.class.getName(), "Found " + profileItems.size() + " profiles in list");
            profileData.close();

            ProfileArrayAdapter adapter = new ProfileArrayAdapter(
                    this,
                    profileItems.toArray(new String[profileItems.size()])
            );
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void getPlanets(String searchTerm) {

        profileSearches.clear();

        profileDao = new ProfileDao(this);

        //ProfileInfo profileInfo = null;

        cursor = profileDao.retrieve(searchTerm);

        if(cursor.getCount() == 0)          //now we have to check how many no of rows are in the cursor
        {
            //there is no data so we will diplay message
            showData("Error","No Name Found by "+searchTerm+".");
            //bloodListViewId.setVisibility(View.GONE);
            //noData.setVisibility(View.VISIBLE);
            //return;
        }

        else
        {
            while (cursor.moveToNext())
            {
                String id,name,company;
                int idNumber;

                id = cursor.getString(0);
                name = cursor.getString(1);
                company = cursor.getString(3);

                idNumber = Integer.parseInt(id);


//                bloodSearch1 = new BloodSearch();
//
//                bloodSearch1.setName(name);
//                bloodSearch1.setNumber(number);
//                bloodSearch1.setBlood(blood);
//
//                bloodSearches.add(bloodSearch1);

                profileSearches.add(new ProfileInfo(idNumber,name,company,false));
            }

            profileCustomAdapter = new ProfileCustomAdapter(this,profileSearches);
            listView.setAdapter(profileCustomAdapter);

        }

    }

    private void showData(String title, String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ProfileListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
