package com.example.b100.seongbuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CouponRegisterActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;
    String REGISTER_URL = Config.URL + "coupon_register.php";
    String coup_id;
    String user_id;
    String image;
    private TextView edit_coup_id;
    private TextView edit_user_id;
    private TextView edit_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_register);
        edit_coup_id = (TextView) findViewById(R.id.coup_id);
        edit_user_id = (TextView) findViewById(R.id.user_id);
        edit_image = (TextView) findViewById(R.id.image);
        Button btn = (Button) findViewById(R.id.button3);


        qrScan = new IntentIntegrator(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        edit_coup_id = (TextView) findViewById(R.id.coup_id);
        edit_user_id = (TextView) findViewById(R.id.user_id);
        edit_image = (TextView) findViewById(R.id.image);
        Button btn = (Button) findViewById(R.id.button3);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(CouponRegisterActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    edit_coup_id.setText(obj.getString("coup_id"));
                    edit_image.setText(obj.getString("image"));
                    edit_user_id.setText(obj.getString("userid"));
                    if (edit_coup_id.getText().toString().equals("restaurant") || edit_coup_id.
                            getText().toString().equals("cafe")) {
                        //qrcode 결과가 있으면
                        Toast.makeText(CouponRegisterActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                        insertToDatabase(edit_coup_id.getText().toString(),edit_image.getText().toString(),edit_user_id.toString());
                    } else
                        Toast.makeText(CouponRegisterActivity.this, "일치하지않는 코드!", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CouponRegisterActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    // textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void insertToDatabase(String coup_id, String image, String user_id) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CouponRegisterActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String coup_id = (String) params[0];
                    String image = (String) params[1];
                    String user_id = (String) params[2];


                    String data = URLEncoder.encode("coup_id", "UTF-8") + "=" + URLEncoder.encode(coup_id, "UTF-8");
                    data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
                    data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");

                    URL url = new URL(REGISTER_URL);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(coup_id, image, user_id);
    }
}