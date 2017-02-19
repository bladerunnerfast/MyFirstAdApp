package com.example.jamessmith.firstadapp.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by James Smith on 2/17/2017.
 */

public class PaymentDatabase extends SQLiteOpenHelper {

    private static final String DB = "UserPayments";
    private static final String tableName = "Payments";
    private static final String orderCode = "orderNumber";
    private static final String paid = "paid";
    private static final String paymentDate = "paymentDate";
    private static final String expireDate = "expireDate";
    private static final int version = 1;

    private Cursor cursor;
    private SQLiteDatabase database;
    private ContentValues contentValues;
    private static final String TAG = PaymentDatabase.class.getName();

    public PaymentDatabase(Context context) {
        super(context, DB, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + tableName + "(" + orderCode + " INT PRIMARY KEY, " + paid + " TEXT, " +
                paymentDate + " TEXT, " + expireDate + " TEXT)";

        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }

        db.execSQL("DROP IF EXISTS " + tableName);
        onCreate(db);
        db.close();
    }

    /**
     *
     * @param paymentModel Takes data model of a payment, containing all details.
     * @return Integer 0 for an unexpected response, 1 entry already exits, and 2 new entry has been successfully added to database.
     */
    public int addPayment(PaymentModel paymentModel){
        database = this.getWritableDatabase();
        boolean hasRow = false;
        String query = "SELECT * FROM " + tableName + " WHERE " +
                orderCode + "=" +"'" + paymentModel.getOrderCode() + "'";
        cursor = database.rawQuery(query , null);


        if(cursor.getCount() >= 1){
            hasRow = true;
        }

        if(hasRow){
            closeDatabase();
            return 1;
        }

        else{
            contentValues = new ContentValues();
            contentValues.put(orderCode, paymentModel.getOrderCode());
            contentValues.put(paid, paymentModel.getPaid());
            contentValues.put(paymentDate, paymentModel.getPaymentDate());
            contentValues.put(expireDate, paymentModel.getExpireDate());
            hasRow = cursor.getCount() >= 1;
            Log.v(TAG, "Has Row: " + hasRow);
            return 2;
        }
    }

    /**
     *
     * @param arg String used to refine the SQL query.
     * @return Payment model containing all details of payment.
     */
    public PaymentModel fetchPaymentStatus(String arg) {
        PaymentModel model = new PaymentModel();
        database = this.getReadableDatabase();
        String query = "SELECT * FROM " + tableName + " WHERE " +
                paid + "=" +"'" + arg + "'";
        cursor = database.rawQuery(query, null);

        if(cursor != null){
            while(cursor.moveToNext()){
                model.setOrderCode(cursor.getInt(0));
                model.setExpireDate(cursor.getString(3));
                model.setPaid(cursor.getString(1));
                model.setPaymentDate(cursor.getString(2));
            }
            closeDatabase();
        }
        return model;
    }

    /**
     * Invoked to close database connection, and ContentValues if has been already set.
     */
    private void closeDatabase(){
        database.close();

        if(contentValues != null) {
            contentValues.clear();
        }
    }
}