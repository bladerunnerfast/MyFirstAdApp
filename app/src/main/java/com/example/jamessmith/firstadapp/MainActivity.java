package com.example.jamessmith.firstadapp;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jamessmith.firstadapp.storage.PaymentDatabase;
import com.example.jamessmith.firstadapp.storage.PaymentModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static boolean[] selected = new boolean[9];
    protected PaymentDatabase paymentDatabase;
    protected PaymentModel model = new PaymentModel();
    private static boolean showBanner = true;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = MainActivity.class.getName();
    View squ1, squ2, squ3, squ4, squ5, squ6, squ7, squ8, squ9;
    AdView adView;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        squ1 = findViewById(R.id.view_square1);
        squ2 = findViewById(R.id.view_square2);
        squ3 = findViewById(R.id.view_square3);
        squ4 = findViewById(R.id.view_square4);
        squ5 = findViewById(R.id.view_square5);
        squ6 = findViewById(R.id.view_square6);
        squ7 = findViewById(R.id.view_square7);
        squ8 = findViewById(R.id.view_square8);
        squ9 = findViewById(R.id.view_square9);
        adView = (AdView) findViewById(R.id.adView);

        squ1.setOnClickListener(this);
        squ2.setOnClickListener(this);
        squ3.setOnClickListener(this);
        squ4.setOnClickListener(this);
        squ5.setOnClickListener(this);
        squ6.setOnClickListener(this);
        squ7.setOnClickListener(this);
        squ8.setOnClickListener(this);
        squ9.setOnClickListener(this);
        paymentDatabase = new PaymentDatabase(this);
        showAd();
    }

    /**
     * Set boxes to green once has been touched, and update the selected array to true for that box. Invoke verifySelection to check
     * if all boxes has been touched.
     * @param v Fetches the current view.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.view_square1:
                squ1.setBackgroundColor(Color.GREEN);
                selected[0] = true;
                verifySelection();
                break;
            case R.id.view_square2:
                squ2.setBackgroundColor(Color.BLUE);
                selected[1] = true;
                verifySelection();
                break;
            case R.id.view_square3:
                squ3.setBackgroundColor(Color.BLACK);
                selected[2] = true;
                verifySelection();
                break;
            case R.id.view_square4:
                squ4.setBackgroundColor(Color.CYAN);
                selected[3] = true;
                verifySelection();
                break;
            case R.id.view_square5:
                squ5.setBackgroundColor(Color.MAGENTA);
                selected[4] = true;
                verifySelection();
                break;
            case R.id.view_square6:
                squ6.setBackgroundColor(Color.YELLOW);
                selected[5] = true;
                verifySelection();
                break;
            case R.id.view_square7:
                squ7.setBackgroundColor(Color.DKGRAY);
                selected[6] = true;
                verifySelection();
                break;
            case R.id.view_square8:
                squ8.setBackgroundColor(Color.LTGRAY);
                selected[7] = true;
                verifySelection();
                break;
            case R.id.view_square9:
                squ9.setBackgroundColor(Color.RED);
                selected[8] = true;
                verifySelection();
                break;
        }
    }

    /**
     * Verify all boxes has been selected by the user. Once all 9 boxes has been selected addEntry will be invoked.
     */
    private void verifySelection() {
        boolean allSelected = true;
        for (int i = 0; allSelected && i < selected.length; i++){
            allSelected &= selected[i];
        }

        if(allSelected){
            displayPaymentDialog();
        }
    }

    /**
     * Checks if the database has already got the payment otherwise
     * will attempt to add new payment. showBanner is set to hide/show banner.
     */
    private void addEntry(String user){
        model= new PaymentModel("0.50", getCurrentTimeStamp(), (generateCode()), getFutureTimeStamp(), user);
        if(paymentDatabase.addPayment(model) == 0){
            showBanner = true;
            Toast.makeText(this, "Payment sum of 0.50 pence has been sucessfull.", Toast.LENGTH_LONG).show();
            Log.v(TAG, "Payment entry unsuccessful.");
        }else{
            showBanner = false;
            Log.v(TAG, "Payment has been added.");
            showAd();
        }
    }

    /**
     *
     * @return Integer: Random number is generated once invoked.
     */
    private int generateCode(){
        int code;
        Random r = new Random();
        code = r.nextInt(100000 - 1 + 1) + 1;
        Log.v(TAG, "code: " + code);
        return code;
    }

    /**
     *
     * @return String: Current date.
     */
    private String getCurrentTimeStamp(){
        try {
            Log.v(TAG, "current time stamp: " + dateFormat.format(new Date()));
            return dateFormat.format(new Date());
        } catch (Exception e) {
            Log.v(TAG, e.toString());
            return "";
        }
    }

    /**
     *
     * @return String expected 7 days of current date. Formatted as yyyy-MM-dd HH:mm:ss
     */
    private String getFutureTimeStamp(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +7);
        String timeStamp = dateFormat.format(cal.getTime());
        Log.v(TAG, "End date: " + timeStamp);
        return timeStamp;
    }

    /**
     * Display payment dialog once the user has checked all squares.
     * Customised dialog to display generated number(read only), and request user to enter a username.
     */
    private void displayPaymentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(null);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.payment_dialog, null, false);
        builder.setView(dialogView);
        Button pay, cancel;
        final EditText orderNumber, username;

        pay = (Button) dialogView.findViewById(R.id.btn_pay);
        cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        orderNumber = (EditText) dialogView.findViewById(R.id.et_order_number);
        username = (EditText) dialogView.findViewById(R.id.et_username);
        dialog = builder.create();

        orderNumber.setText(String.valueOf(generateCode()));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!username.getText().toString().equals("")) && (username != null) && (!username.getText().toString().equals("Username"))){
                    addEntry(username.getText().toString());
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Username field has to be populated.", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Only display's ad banner if showBanner is set to true, otherwise ad banner is destroyed.
     */
    private void showAd(){
        if(!showBanner){
            Log.v(TAG, "banner not visible");
            adView.destroy();
        }else{
            Log.v(TAG, "banner is visible");
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }
}