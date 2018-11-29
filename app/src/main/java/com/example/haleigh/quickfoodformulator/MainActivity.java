package com.example.haleigh.quickfoodformulator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button login;

    public String ingredients = "";
    public String product_name = "";
    public String barcode_number = "";

    TextView barcodeResult;     //barcode id
    TextView barcodeIngredients;
    TextView barcodeName;
    TextView qrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login = findViewById(R.id.loginDirectionalButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });

        barcodeResult = (TextView)findViewById(R.id.barcode_result);
        barcodeName = (TextView)findViewById(R.id.barcode_name);
        barcodeIngredients = (TextView)findViewById(R.id.barcode_ingredients);
        qrResult = (TextView)findViewById(R.id.qr_result);

        barcode_number = "";
        product_name = "";
        ingredients = "";
    }

    public void scanBarcode(View v) {
        if (barcodeIngredients != null) {
            barcodeIngredients.setText("");
        }
        if (barcodeName != null) {
            barcodeName.setText("");
        }
        if (barcodeResult != null) {
            barcodeResult.setText("");
        }
        if (qrResult != null) {
            qrResult.setText("");
        }
        Intent intent = new Intent(MainActivity.this, cameraActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcode_number = "";
                    product_name = "";
                    ingredients = "";
                    if (barcodeIngredients != null) {
                        barcodeIngredients.setText("");
                    }
                    if (barcodeName != null) {
                        barcodeName.setText("");
                    }
                    if (barcodeResult != null) {
                        barcodeResult.setText("");
                    }
                    if (qrResult != null) {
                        qrResult.setText("");
                    }
                    //If the Barcode is a number
                    if(barcode.valueFormat == 5) {      //PRODUCT
                        new JsonTask().execute("https://api.barcodelookup.com/v2/products?barcode=" + barcode.displayValue + "&formatted=y&key=jjgszqhu4fhqqa6369sd9elzn13omy");
                    }
                    else if (barcode.valueFormat == 8) {        //URL
                        Bundle bundle = new Bundle();
                        Intent i = new Intent(this, Result.class);
                        bundle.putString("qr_result", barcode.displayValue);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        Intent i = new Intent(this, Result.class);
                        bundle.putString("barcode_number", "No barcode found!");
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
                else {
                    Bundle bundle = new Bundle();
                    Intent i = new Intent(this, Result.class);
                    bundle.putString("barcode_number", "No barcode found!");
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }

        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private class JsonTask extends AsyncTask<String, String, String> {


        public ProgressDialog pd = new ProgressDialog(MainActivity.this);;

        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            barcode_number = "";
            product_name = "";
            ingredients = "";
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = "";
                String data2 = "";

                while (null != (str= br.readLine())) {
                    data2 +=str;
                }

                Gson g = new Gson();

                Result.Sample.RootObject value = g.fromJson(data2, Result.Sample.RootObject.class);

                barcode_number = "";
                product_name = "";
                ingredients = "";
                barcode_number = value.products[0].barcode_number;

                product_name = value.products[0].product_name;

                ingredients = value.products[0].ingredients;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            Bundle bundle = new Bundle();
            Intent i = new Intent(getApplicationContext(), Result.class);
            bundle.putString("ingredients", ingredients);
            bundle.putString("product_name", product_name);
            bundle.putString("barcode_number", barcode_number);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

}
