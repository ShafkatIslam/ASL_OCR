package com.example.professt.asl_ocr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileViewActivity extends AppCompatActivity {

    ImageView BackId,MenuId;

    Profile profile;
    public static final String PROFILE_ID_KEY = "profile_id_key";

    private ProfileDao profileDao;

    private EditText textViewName;
    private EditText textViewJobTitle;
    private EditText textViewCompany;
    private EditText textViewTelephone;
    private EditText textViewCellphone;
    private EditText textViewEmail;
    private EditText textViewWebsite;
    private EditText textViewFax;
    private EditText textViewAddress;

    Integer id;

    String name,job,company,telephone,cellphone,email,website,fax,address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        profileDao = ProfileDao.getInstance(this);
        profile = loadProfile();
        if (profile == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.profile_creator_alert_read_fail);
            builder.setPositiveButton(R.string.profile_viewer_return_to_list_button,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            returnToList();
                        }
                    });
            builder.show();
        } else {
            textViewName = (EditText) findViewById(R.id.input_name);
            textViewJobTitle = (EditText) findViewById(R.id.input_job_title);
            textViewCompany = (EditText) findViewById(R.id.input_company);
            textViewTelephone = (EditText) findViewById(R.id.input_telephone);
            textViewCellphone = (EditText) findViewById(R.id.input_cell);
            textViewEmail = (EditText) findViewById(R.id.input_email);
            textViewWebsite = (EditText) findViewById(R.id.input_website);
            textViewFax = (EditText) findViewById(R.id.input_fax);
            textViewAddress = (EditText) findViewById(R.id.input_address);

            BackId = (ImageView)findViewById(R.id.BackId);
            MenuId = (ImageView)findViewById(R.id.MenuId);

            BackId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileViewActivity.this, ProfileListActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            textViewName.setEnabled(false);
            textViewJobTitle.setEnabled(false);
            textViewCompany.setEnabled(false);
            textViewTelephone.setEnabled(false);
            textViewCellphone.setEnabled(false);
            textViewEmail.setEnabled(false);
            textViewWebsite.setEnabled(false);
            textViewFax.setEnabled(false);
            textViewAddress.setEnabled(false);

            textViewName.setText(profile.getName());
            textViewJobTitle.setText(profile.getJobTitle());
            textViewCompany.setText(profile.getCompany());
            textViewTelephone.setText(profile.getPrimaryContactNumber());
            textViewCellphone.setText(profile.getSecondaryContactNumber());
            textViewEmail.setText(profile.getEmail());
            textViewAddress.setText(profile.getAddress());
            textViewWebsite.setText(profile.getWebsite());
            textViewFax.setText(profile.getFax());

            id = profile.getId();
            name = profile.getName();
            job = profile.getJobTitle();
            company = profile.getCompany();
            telephone = profile.getPrimaryContactNumber();
            cellphone = profile.getSecondaryContactNumber();
            email = profile.getEmail();
            address = profile.getAddress();
            website = profile.getWebsite();
            fax = profile.getFax();

            MenuId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileViewActivity.this, ProfileEditActivity.class);
                    intent.putExtra("id",id);
                    intent.putExtra("name",name);
                    intent.putExtra("job",job);
                    intent.putExtra("company",company);
                    intent.putExtra("telephone",telephone);
                    intent.putExtra("cellphone",cellphone);
                    intent.putExtra("email",email);
                    intent.putExtra("address",address);
                    intent.putExtra("website",website);
                    intent.putExtra("fax",fax);
                    startActivity(intent);
                }
            });
        }

    }

    private void returnToList() {
        Intent intent = new Intent(ProfileViewActivity.this, ProfileListActivity.class);
        startActivity(intent);
        finish();
    }

    private Profile loadProfile(){
        Profile profile = null;
        try {
            int profileId = getIntent().getIntExtra(PROFILE_ID_KEY, -1);
            if (profileId < 0){
                Log.d(ProfileViewActivity.class.getName(),
                        "Profile ID is not passed on from the previous activity");
            } else {
                profile = profileDao.load(profileId);
            }
        } catch (Exception e){
            Log.e(ProfileViewActivity.class.getName(), "Failed to load profile!");
            Log.e(ProfileViewActivity.class.getName(), Log.getStackTraceString(e));
        }
        return profile;
    }

    public void onBackPressed(){
        returnToList();
    }
}
