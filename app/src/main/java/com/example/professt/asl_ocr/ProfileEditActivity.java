package com.example.professt.asl_ocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileEditActivity extends AppCompatActivity {

    ImageView noId,yesId;

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

    String getName,getJob,getCompany,getTelephone,getCellphone,getEmail,getWebsite,getFax,getAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        textViewName = (EditText) findViewById(R.id.input_names);
        textViewJobTitle = (EditText) findViewById(R.id.input_job_titles);
        textViewCompany = (EditText) findViewById(R.id.input_companys);
        textViewTelephone = (EditText) findViewById(R.id.input_telephones);
        textViewCellphone = (EditText) findViewById(R.id.input_cells);
        textViewEmail = (EditText) findViewById(R.id.input_emails);
        textViewWebsite = (EditText) findViewById(R.id.input_websites);
        textViewFax = (EditText) findViewById(R.id.input_faxs);
        textViewAddress = (EditText) findViewById(R.id.input_addresss);

        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        job = getIntent().getStringExtra("job");
        company = getIntent().getStringExtra("company");
        telephone = getIntent().getStringExtra("telephone");
        cellphone = getIntent().getStringExtra("cellphone");
        email = getIntent().getStringExtra("email");
        website = getIntent().getStringExtra("website");
        fax = getIntent().getStringExtra("fax");
        address = getIntent().getStringExtra("address");

        textViewName.setText(name);
        textViewJobTitle.setText(job);
        textViewCompany.setText(company);
        textViewTelephone.setText(telephone);
        textViewCellphone.setText(cellphone);
        textViewEmail.setText(email);
        textViewWebsite.setText(website);
        textViewFax.setText(fax);
        textViewAddress.setText(address);

        profileDao = new ProfileDao(this);

        noId = (ImageView)findViewById(R.id.noId);
        yesId = (ImageView)findViewById(R.id.yesId);

        noId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEditActivity.this, ProfileListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        yesId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName = textViewName.getText().toString();
                getJob = textViewJobTitle.getText().toString();
                getCompany = textViewCompany.getText().toString();
                getTelephone = textViewTelephone.getText().toString();
                getCellphone = textViewCellphone.getText().toString();
                getEmail = textViewEmail.getText().toString();
                getWebsite = textViewWebsite.getText().toString();
                getFax = textViewFax.getText().toString();
                getAddress = textViewAddress.getText().toString();

                profileDao.updateContactData(id,getName,getJob,getCompany,getTelephone,getCellphone,getEmail,getWebsite,getFax,getAddress);

                Toast.makeText(ProfileEditActivity.this,"Successfully Updated",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProfileEditActivity.this, ProfileListActivity.class);
                startActivity(intent);



            }
        });
    }
}
