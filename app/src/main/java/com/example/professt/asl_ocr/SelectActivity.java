package com.example.professt.asl_ocr;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    ImageView backsId,deleteId;
    SearchView nameSearchsViewId;

    ProfileDao profileDao;

    Cursor cursor;

    ListView listView;

    ArrayList<ProfileInfo> profileSearches = new ArrayList<ProfileInfo>();

    ProfilesCustomAdapter profilesCustomAdapter;

    int selects,selectsAll;

    public long rowId1;

    int selectValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        profileDao = ProfileDao.getInstance(this);
        listView = (ListView) findViewById(R.id.lists_view);
        backsId = (ImageView)findViewById(R.id.backsId);
        deleteId = (ImageView)findViewById(R.id.deleteId);
        nameSearchsViewId = (SearchView)findViewById(R.id.nameSearchsViewId);

        profilesCustomAdapter = new ProfilesCustomAdapter(this,profileSearches);
        listView.setAdapter(profilesCustomAdapter);

        selects = getIntent().getIntExtra("Select", -1);
        selectsAll = getIntent().getIntExtra("SelectAll", -1);

        showData();

        deleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(profilesCustomAdapter.getBox().isEmpty())
                {
                    Toast.makeText(SelectActivity.this, "Please Select", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //rowId1 = profileDao.deleteData2();

                    String result = "Selected Product are :";
                    int id;

                    for (ProfileInfo p : profilesCustomAdapter.getBox()) {

                        if(!(p.select))
                        {
                            Toast.makeText(SelectActivity.this, "Please Select", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if (p.select){
                                id = p.id;

                                {
                                    rowId1 = profileDao.deleteData(id); //method is calling

                                }

                            }
                        }

                    }

                    if (rowId1 > 0) {
                        Toast.makeText(SelectActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SelectActivity.this,ProfileListActivity.class);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(SelectActivity.this, "Please Select", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        nameSearchsViewId.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        backsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this, ProfileListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showData() {

        cursor = profileDao.showAllDataBlood();     //we have to call the showAlllData method with the help of myDatabaseHelper because showAllData is a method of myDatabaseHelper


        if(cursor.getCount() == 0)          //now we have to check how many no of rows are in the cursor
        {
            //there is no data so we will diplay message
            showData("Error","No Data Found.\nPlease Import New Contact");

        }
        else
        {
            if(cursor.moveToFirst())
            {
                do {
                    String id,name,company;
                    int idNumber;

                    id = cursor.getString(0);
                    name = cursor.getString(1);
                    company = cursor.getString(3);

                    idNumber = Integer.parseInt(id);
//                    Friends friends = new Friends(name,number);
//                    friendsCustomAdapter.add(friends);
//                    list.add(friends);

                    if(selects==1)
                    {
                        profileSearches.add(new ProfileInfo(idNumber,name,company,false));
                    }
                    else if(selectsAll==2)
                    {
                        profileSearches.add(new ProfileInfo(idNumber,name,company,true));
                    }



                }
                //it will check the String from first to last in the cursor
                while(cursor.moveToNext());  //to check till there is a row after a previous row
                {
                    //listData.add(cursor.getString(1)+" \t "+cursor.getString(3));  //everytime add the 0 and 1 index value with the listdata
                }
            }
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

                if(selects==1)
                {
                    profileSearches.add(new ProfileInfo(idNumber,name,company,false));
                }
                else if(selectsAll==2)
                {
                    profileSearches.add(new ProfileInfo(idNumber,name,company,true));
                }
            }

            profilesCustomAdapter = new ProfilesCustomAdapter(this,profileSearches);
            listView.setAdapter(profilesCustomAdapter);

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
        Intent intent = new Intent(SelectActivity.this, ProfileListActivity.class);
        startActivity(intent);
        finish();
    }
}
