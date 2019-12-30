package com.example.professt.asl_ocr;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.karan.churi.PermissionManager.PermissionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileCreatorActivity extends AppCompatActivity {

    private Profile profile;

    private EditText nameInput;
    private EditText jobTitleInput;
    private EditText companyInput;
    private EditText telephoneInput;
    private EditText cellInput;
    private EditText emailInput;
    private EditText addressInput;
    private EditText websiteInput;
    private EditText faxInput;
    private Button nameCandidatesButton;
    private Button jobTitleCandidatesButton;
    private Button companyCandidatesButton;
    private Button telephoneCandidatesButton;
    private Button cellCandidatesButton;
    private Button emailCandidatesButton;
    private Button addressCandidatesButton;
    private Button websiteCandidatesButton;
    private Button faxCandidatesButton;
    private Button addressCropButton;

    ImageView imageView;

    String item = "";

    Map<String, Integer> phoneNumberCandidates = new HashMap<String, Integer>();
    Map<String, Integer> cellNumberCandidates = new HashMap<String, Integer>();
    Map<String, Integer> emailCandidates = new HashMap<String, Integer>();
    List<String> genericCandidates = new ArrayList<String>();
    List<String> nameCandidates = new ArrayList<String>();
    List<String> companyCandidates = new ArrayList<String>();
    List<String> addressCandidates = new ArrayList<String>();
    List<String> websiteCandidates = new ArrayList<String>();

    private ProfileDao profileDao;

    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    Uri image_uri;

    String addresses;

    PermissionManager permissionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        nameInput = (EditText) findViewById(R.id.input_name);
        jobTitleInput = (EditText) findViewById(R.id.input_job_title);
        companyInput = (EditText) findViewById(R.id.input_company);
        telephoneInput = (EditText) findViewById(R.id.input_telephone);
        cellInput = (EditText) findViewById(R.id.input_cell);
        emailInput = (EditText) findViewById(R.id.input_email);
        addressInput = (EditText) findViewById(R.id.input_address);
        websiteInput = (EditText) findViewById(R.id.input_website);
        faxInput = (EditText) findViewById(R.id.input_fax);

        imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setVisibility(View.GONE);

        nameCandidatesButton = (Button) findViewById(R.id.name_candidates_button);
        jobTitleCandidatesButton = (Button) findViewById(R.id.job_title_candidates_button);
        companyCandidatesButton = (Button) findViewById(R.id.company_candidates_button);
        telephoneCandidatesButton = (Button) findViewById(R.id.telephone_candidates_button);
        cellCandidatesButton = (Button) findViewById(R.id.cell_candidates_button);
        emailCandidatesButton = (Button) findViewById(R.id.email_candidates_button);
        addressCandidatesButton = (Button) findViewById(R.id.address_candidates_button);
        websiteCandidatesButton = (Button) findViewById(R.id.website_candidates_button);
        faxCandidatesButton = (Button) findViewById(R.id.fax_candidates_button);
        addressCropButton = (Button) findViewById(R.id.address_crop_button);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
        
        
        addressCropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageImportDialog();
            }
        });

        if (!generateProfile()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.profile_creator_alert_read_fail);
            builder.setNeutralButton(R.string.profile_creator_alert_read_fail_retry,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ProfileCreatorActivity.this,
                                    CameraReaderActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton(R.string.profile_creator_alert_read_fail_manual,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.setPositiveButton(R.string.profile_creator_alert_read_fail_exit,
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ProfileCreatorActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.show();
        }

        profileDao = ProfileDao.getInstance(this);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button rescanButton = (Button) findViewById(R.id.rescan_button);
        Button exitButton = (Button) findViewById(R.id.exit_button);

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                validateAndCreateProfile();
            }
        });
        rescanButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                confirmRescan();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                confirmExit();
            }
        });

        nameCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidates(nameCandidates, nameInput);
            }
        });
        jobTitleCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidates(genericCandidates, jobTitleInput);
            }
        });
        companyCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidates(companyCandidates, companyInput);
            }
        });
        addressCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidatesForAddress(addressCandidates, addressInput);
            }
        });
        telephoneCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidates(phoneNumberCandidates.keySet(), telephoneInput);
            }
        });

        cellCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpCandidates(phoneNumberCandidates.keySet(), cellInput);
            }
        });
        emailCandidatesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                popUpCandidates(emailCandidates.keySet(), emailInput);
            }
        });
        faxCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpCandidates(phoneNumberCandidates.keySet(), faxInput);
            }
        });

        websiteCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpCandidates(websiteCandidates, websiteInput);
            }
        });
    }

    private void showImageImportDialog() {

        String[] items = {" Camera"," Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which==0)
                {
                    pickCamera();
                }
                if(which==1)
                {
                    pickGallery();
                }

            }
        });

        dialog.create().show();

    }

    private void pickCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK)
        {
            if(requestCode==IMAGE_PICK_GALLERY_CODE)
            {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if(requestCode==IMAGE_PICK_CAMERA_CODE)
            {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                imageView.setVisibility(View.VISIBLE);
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);

                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if(!recognizer.isOperational())
                {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i =0; i<items.size();i++)
                    {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }


                    addressInput.setText(sb.toString());
                }

            }

            else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmRescan(){
        dialogConfirm(R.string.profile_creator_confirm_rescan,
                R.string.profile_creator_button_rescan,
                CameraReaderActivity.class);
    }

    private void confirmExit(){
        dialogConfirm(R.string.profile_creator_confirm_exit,
                R.string.profile_creator_button_exit,
                MainActivity.class);
    }

    private void dialogConfirm(int dialogMessage,
                               int confirmMessage,
                               final Class newActivityClass){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirmation);
        builder.setMessage(dialogMessage);
        builder.setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.setPositiveButton(confirmMessage,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                newActivityClass);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }

    private void popUpCandidates(Collection<String> candidates, final EditText input){
        if (!candidates.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final CharSequence[] items = candidates.toArray(new CharSequence[candidates.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInteface, int i) {
                    input.setText(items[i]);
                }
            });
            builder.show();
        }
    }

    private void popUpCandidatesForAddress(final Collection<String> candidates, final EditText input){
        if (!candidates.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final CharSequence[] items = candidates.toArray(new CharSequence[candidates.size()]);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInteface, int i) {
                    item = item.concat((String) items[i]);
                    input.setText(item);
                    candidates.remove(items[i]);
                }
            });
            builder.show();
        }
    }

    private void validateAndCreateProfile(){

        Profile profile = new Profile(
                nameInput.getText().toString(),
                jobTitleInput.getText().toString(),
                companyInput.getText().toString(),
                telephoneInput.getText().toString(),
                emailInput.getText().toString(),
                addressInput.getText().toString(),
                cellInput.getText().toString(),
                websiteInput.getText().toString(),
                faxInput.getText().toString()
        );
        if (profile.isValid()){
            if (saveProfile(profile)) {
                showSaveSuccessDialog();
            } else {
                Utils.displayErrorDialog(this);
            }
        } else {
            alertInvalidProfile();
        }
    }

    private boolean saveProfile(Profile profile) {
        return profileDao.insert(profile);
    }

    private void alertInvalidProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.profile_creator_save_invalid_title);
        builder.setMessage(R.string.profile_creator_save_invalid_message);
        builder.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

    private void showSaveSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_success);
        builder.setMessage(R.string.profile_creator_save_success_message);
        builder.setCancelable(false); //Don't let them touch out!
        builder.setNegativeButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                ProfileListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.setPositiveButton(R.string.profile_creator_save_success_scan_another,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ProfileCreatorActivity.this,
                                CameraReaderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.create().show();
    }

    private boolean generateProfile() {
        ArrayList<String> profileData;
        try{
            profileData = getIntent().getStringArrayListExtra(CameraReaderActivity.PROFILE_DATA_KEY);
        } catch(Exception e) {
            Log.w(ProfileCreatorActivity.class.getName(), Log.getStackTraceString(e));
            return false;
        }

        for (String snapshot : profileData){
            for (String text : snapshot.split("\n")){
                int selected = 0;
                selected = selectPhoneNumber(text, phoneNumberCandidates)
                        + selectEmail(text, emailCandidates)
                        + selectPhoneNumber(text, phoneNumberCandidates)
                        + selectPhoneNumber(text, phoneNumberCandidates);
                if (selected == 0) {
                    selectRest(text, genericCandidates);
                }
            }
        }
        boolean generateProfile = false;
        String phoneNumber = getBestCandidate(phoneNumberCandidates);
        String cellNumber = getBestCandidate(phoneNumberCandidates);
        String fax = getBestCandidate(phoneNumberCandidates);
        if (StringUtils.isNotBlank(phoneNumber)){
            generateProfile = true;
            telephoneInput.setText(phoneNumber);
            cellInput.setText(cellNumber);
            faxInput.setText(fax);
        }
        String email = getBestCandidate(emailCandidates);

        if (StringUtils.isNotBlank(email)){
            generateProfile = true;
            emailInput.setText(email);
            String namePart = email.substring(0, email.indexOf("@"));
            String companyPart = email.substring(email.indexOf("@")+1, email.length());
            companyPart = companyPart.substring(0, companyPart.indexOf("."));

            StringBuilder nameBuilder = new StringBuilder();
            int j = 0;
            for (String str : namePart.split("\\.")){
                j++;
                nameBuilder.append(str.substring(0, 1).toUpperCase());
                if (str.length() > 1){
                    nameBuilder.append(str.substring(1));
                }
                nameBuilder.append(" ");
            }
            if (j > 0) {
                nameCandidates.add(nameBuilder.toString().trim());
            }

            if (companyPart.length() > 1
                    && !companyPart.equals("googlemail")
                    && !companyPart.equals("gmail")
                    && !companyPart.equals("hotmail")
                    && !companyPart.equals("live")){
                companyCandidates.add(companyPart.substring(0, 1).toUpperCase()+companyPart.substring(1));
                companyCandidates.add(companyPart.toUpperCase());
                companyCandidates.add(companyPart);
            }

        }

        nameCandidates.addAll(genericCandidates);
        companyCandidates.addAll(genericCandidates);
        addressCandidates.addAll(genericCandidates);
        websiteCandidates.addAll(genericCandidates);

        if (!nameCandidates.isEmpty()){
            nameInput.setText(nameCandidates.get(0));
            generateProfile = true;
        }
        int i = 0;
        if (!companyCandidates.isEmpty()){
            if (companyCandidates.get(0).equals(nameCandidates.get(0)) && companyCandidates.size() != 1){
                i++;
            }
            companyInput.setText(companyCandidates.get(i));
        }
        int j = 0;
        int k = 0;
        if (!addressCandidates.isEmpty()){
            while(j<addressCandidates.size())
            {
                if (addressCandidates.get(j).matches("\\d+[ ](?:[A-Za-z0-9.-]+[ ]?)+(?:Avenue|Lane|Road|Boulevard|Drive|Street|Ave|Dr|Rd|Blvd|Ln|St)\\.?")){
                    k=j;
                }
                j++;
            }

            addressInput.setText(addressCandidates.get(k));
        }

        int l = 0;
        int m = 0;
        if (!websiteCandidates.isEmpty()){
            while(l<websiteCandidates.size())
            {
                if (websiteCandidates.get(l).contains("www")){
                    m=l;
                }
                l++;
            }

            websiteInput.setText(websiteCandidates.get(m));
        }
        if (!genericCandidates.isEmpty()){
            jobTitleInput.setText(genericCandidates.get(0));
        }

        genericCandidates.addAll(phoneNumberCandidates.keySet());
        genericCandidates.addAll(emailCandidates.keySet());
        return generateProfile;
    }

    private void selectRest(String text, List<String> genericCandidates) {
        List<String> toFilter = new ArrayList<String>();
        boolean filter = false;
        for (String candidate : genericCandidates){
            if (candidate.contains(text)){
                filter = true;
                break;
            }
            if (text.contains(candidate)){
                toFilter.add(candidate);
            }
        }
        if (!filter){
            genericCandidates.add(text);
        }
        genericCandidates.removeAll(toFilter);
    }

    private int selectPhoneNumber(String text, Map<String, Integer> phoneNumberCandidates) {
        //At least 6 numbers, allow other characters
        String trimmed = text.toLowerCase().replaceAll("tel:","").replaceAll("mob:","").trim();
        if (phoneNumberCandidates.containsKey(trimmed)) {
            phoneNumberCandidates.put(trimmed, phoneNumberCandidates.get(trimmed) + 1);
        } else {
            int numCount = 0;

            for (char c : trimmed.toCharArray()) {
                if (Character.isDigit(c)) {
                    numCount++;
                }
                if (numCount == 6) {
                    phoneNumberCandidates.put(trimmed, 1);
                    return 1;
                }
            }
        }
        return 0;
    }
    private int selectEmail(String text, Map<String, Integer> emailCandidates) {
        int atPos = text.indexOf("@");
        int dotPos = text.lastIndexOf(".");
        //Very basic check to see if a text COULD BE an email address
        if (atPos != -1 && dotPos > atPos){
            String trimmed = text.trim();
            if (emailCandidates.containsKey(trimmed)){
                emailCandidates.put(trimmed, emailCandidates.get(trimmed)+1);
            } else {
                emailCandidates.put(trimmed, 1);
            }
            return 1;
        }
        return 0;
    }

    private String getBestCandidate(Map<String, Integer> candidates){
        int maxValue = 0;
        String bestCandidate ="";
        for (Map.Entry<String, Integer> candidate : candidates.entrySet()){
            if (candidate.getValue() > maxValue){
                maxValue = candidate.getValue();
                bestCandidate = candidate.getKey();
            }
        }
        //candidates.remove(bestCandidate);
        return bestCandidate;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ProfileCreatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
