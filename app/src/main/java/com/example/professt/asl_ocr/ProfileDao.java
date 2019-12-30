package com.example.professt.asl_ocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDao extends SQLiteOpenHelper {

    private static final String TAG = ProfileDao.class.getName();

    private static final String APP_DATABASE = "miller_bcr.db";
    private static final String PROFILE_TABLE = "profiles";
    private static final String PROFILE_COL_ID = "id";
    private static final String PROFILE_COL_NAME = "name";
    private static final String PROFILE_COL_JOB_TITLE = "job_title";
    private static final String PROFILE_COL_COMPANY = "company";
    private static final String PROFILE_COL_PRIMARY_TEL = "primary_tel";
    private static final String PROFILE_COL_EMAIL = "email";
    private static final String PROFILE_COL_ADDRESS = "address";
    private static final String PROFILE_COL_SECONDARY_TEL = "secondary_tel";
    private static final String PROFILE_COL_WEBSITE = "website";
    private static final String PROFILE_COL_FAX = "fax";
    private static ProfileDao mInstance;

    public static final String SELECT_ALL = "SELECT * FROM "+ PROFILE_TABLE;

    public ProfileDao(Context context) {
        super(context, APP_DATABASE, null, 1);
    }

    public static ProfileDao getInstance(Context context){
        if (mInstance == null){
            mInstance = new ProfileDao(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder createTableQueryBuilder = new StringBuilder();
        createTableQueryBuilder.append("create table ").append(PROFILE_TABLE)
                .append(" (")
                .append(PROFILE_COL_ID).append(" integer primary key")
                .append(", ")
                .append(PROFILE_COL_NAME).append(" text")
                .append(", ")
                .append(PROFILE_COL_JOB_TITLE).append(" text")
                .append(", ")
                .append(PROFILE_COL_COMPANY).append(" text")
                .append(", ")
                .append(PROFILE_COL_PRIMARY_TEL).append(" text")
                .append(", ")
                .append(PROFILE_COL_SECONDARY_TEL).append(" text")
                .append(", ")
                .append(PROFILE_COL_ADDRESS).append(" text")
                .append(", ")
                .append(PROFILE_COL_WEBSITE).append(" text")
                .append(", ")
                .append(PROFILE_COL_FAX).append(" text")
                .append(", ")
                .append(PROFILE_COL_EMAIL).append(" text")
                .append(")");
        sqLiteDatabase.execSQL(createTableQueryBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO implement a proper upgrade task
        // (e.g. backup old database and insert to new schema)

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(sqLiteDatabase);
    }

    public boolean insert(Profile profile){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            setContentValuesFromProfile(values, profile);
            db.insert(PROFILE_TABLE, null, values);
        } catch(Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
        return true;
    }

    public boolean update(Profile profile){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            setContentValuesFromProfile(values, profile);
            db.update(PROFILE_TABLE, values, "id = ?",
                    new String[]{ String.valueOf(profile.getId())});
        } catch(Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
        return true;
    }

    public void updateContactData(int id,String name,String job,String company,String telephone,String cellphone,String email,String website,String fax, String address)
    {
        SQLiteDatabase db = this.getWritableDatabase(); //to write or read in database we need to help getWriteableDatabase method and it will return a SqLiteDatabase
        //to store all the data together we need to use ContentValues class.So we need to make an object of ContentValues
        ContentValues contentValues = new ContentValues();

        String ID = String.valueOf(id);

        //we put the data one by one in the contentValues by using put method and there are two parameter(COLUMN_NAME,VALUE) in the put method
        contentValues.put(PROFILE_COL_ID,ID);
        contentValues.put(PROFILE_COL_NAME,name);
        contentValues.put(PROFILE_COL_JOB_TITLE,job);
        contentValues.put(PROFILE_COL_COMPANY,company);
        contentValues.put(PROFILE_COL_PRIMARY_TEL,telephone);
        contentValues.put(PROFILE_COL_EMAIL,email);
        contentValues.put(PROFILE_COL_ADDRESS,address);
        contentValues.put(PROFILE_COL_SECONDARY_TEL,cellphone);
        contentValues.put(PROFILE_COL_WEBSITE,website);
        contentValues.put(PROFILE_COL_FAX,fax);
//        long rowId=
        db.update(PROFILE_TABLE, contentValues, PROFILE_COL_ID + "= ?", new String[] {ID});
//        return rowId;
    }

    public Cursor showAllDataBlood()  //create a method to show all data which is a return type of "Cursor"
    {
        SQLiteDatabase db = this.getWritableDatabase(); //to access the data from in database we need to help getWriteableDatabase method and it will return a SqLiteDatabase

        //While playng a query all the resultset will return and then to access/read/write it we have to use Cursor interface
        //Cursor cursor = db.rawQuery(SELECT_ALL7 +" WHERE PnumberA LIKE '"+number+"'"+" AND Pid LIKE '"+id+"'",null); //we have to write a query by the help of database "db" and the rawQuery method of SQLiteDatabase returns a dataset or resultset and the resultset must be kept in a variable or interface and it is cursor interface
        Cursor cursor = db.rawQuery(SELECT_ALL,null);
        return cursor;

//        db.execSQL("DELETE FROM notes WHERE note='"+EventName+"'"+" AND times='"+EventDate+"'"+" AND number='"+Number+"'");
    }

    private void setContentValuesFromProfile(ContentValues values, Profile profile){
        values.put(PROFILE_COL_NAME, profile.getName());
        values.put(PROFILE_COL_JOB_TITLE, profile.getJobTitle());
        values.put(PROFILE_COL_COMPANY, profile.getCompany());
        values.put(PROFILE_COL_PRIMARY_TEL, profile.getPrimaryContactNumber());
        values.put(PROFILE_COL_EMAIL, profile.getEmail());
        values.put(PROFILE_COL_ADDRESS, profile.getAddress());
        values.put(PROFILE_COL_SECONDARY_TEL, profile.getSecondaryContactNumber());
        values.put(PROFILE_COL_WEBSITE, profile.getWebsite());
        values.put(PROFILE_COL_FAX, profile.getFax());
    }

    public Cursor loadDataForMinimalList() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select ")
                    .append(PROFILE_COL_ID).append(", ")
                    .append(PROFILE_COL_NAME).append(", ")
                    .append(PROFILE_COL_COMPANY)
                    .append(" from ").append(PROFILE_TABLE)
                    .append(" order by ").append(PROFILE_COL_NAME).append(" asc")
                    .append(";");
            Cursor result =
                    db.rawQuery(queryBuilder.toString(), null);
            return result;
        } catch(Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public Profile load(Integer id){
        Profile profile = null;
        if (id != null) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("select ")
                        .append(PROFILE_COL_NAME).append(", ")
                        .append(PROFILE_COL_JOB_TITLE).append(", ")
                        .append(PROFILE_COL_COMPANY).append(", ")
                        .append(PROFILE_COL_PRIMARY_TEL).append(", ")
                        .append(PROFILE_COL_SECONDARY_TEL).append(", ")
                        .append(PROFILE_COL_ADDRESS).append(", ")
                        .append(PROFILE_COL_WEBSITE).append(", ")
                        .append(PROFILE_COL_FAX).append(", ")
                        .append(PROFILE_COL_EMAIL)
                        .append(" from ").append(PROFILE_TABLE)
                        .append(" where ").append(PROFILE_COL_ID).append(" = ?")
                        .append(";");
                Cursor result =
                        db.rawQuery(queryBuilder.toString(), new String[]{String.valueOf(id)});
                result.moveToFirst();
                profile = new Profile(
                        result.getString(result.getColumnIndex(PROFILE_COL_NAME)),
                        result.getString(result.getColumnIndex(PROFILE_COL_JOB_TITLE)),
                        result.getString(result.getColumnIndex(PROFILE_COL_COMPANY)),
                        result.getString(result.getColumnIndex(PROFILE_COL_PRIMARY_TEL)),
                        result.getString(result.getColumnIndex(PROFILE_COL_EMAIL)),
                        result.getString(result.getColumnIndex(PROFILE_COL_ADDRESS)),
                        result.getString(result.getColumnIndex(PROFILE_COL_SECONDARY_TEL)),
                        result.getString(result.getColumnIndex(PROFILE_COL_WEBSITE)),
                        result.getString(result.getColumnIndex(PROFILE_COL_FAX))
                );
                profile.setId(id);
                result.close();
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
        return profile;
    }

    public Cursor retrieve(String searchTerm)
    {
        String[] columns={PROFILE_COL_ID,PROFILE_COL_NAME,PROFILE_COL_JOB_TITLE,PROFILE_COL_COMPANY,PROFILE_COL_PRIMARY_TEL,PROFILE_COL_EMAIL,PROFILE_COL_ADDRESS,PROFILE_COL_SECONDARY_TEL,PROFILE_COL_WEBSITE,PROFILE_COL_FAX};
        Cursor c=null;

        SQLiteDatabase db = this.getWritableDatabase(); //to access the data from in database we need to help getWriteableDatabase method and it will return a SqLiteDatabase

        if(searchTerm != null && searchTerm.length()>0)
        {
            String sql="SELECT * FROM "+PROFILE_TABLE+" WHERE "+PROFILE_COL_NAME+" LIKE '%"+searchTerm+"%'";
            c=db.rawQuery(sql,null);
            return c;

        }

        c=db.query(PROFILE_TABLE,columns,null,null,null,null,null);
        return c;
    }

    public int deleteData(Integer id) //declare a method to delete data according to its primary key "id"
    {
        SQLiteDatabase db = this.getWritableDatabase(); //to write or read in database we need to help getWriteableDatabase method and it will return a SqLiteDatabase
        return db.delete(PROFILE_TABLE, PROFILE_COL_ID + "=" + id, null);  //delete query has 3 parameters.(Table Name,for which id we have to delete the value,then declare a String Array and the id is passed into it because we will delete the data according to id and the total query has a integer return type

    }
}
