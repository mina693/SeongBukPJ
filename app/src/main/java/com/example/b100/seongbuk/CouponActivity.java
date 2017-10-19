package com.example.b100.seongbuk;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CouponActivity extends AppCompatActivity {
    String DATA_URL = Config.URL + "getData.php";
    private static final String TAG_RESULTS = "result";

    JSONArray couponsJson = null;
    String myJSON;
    ListView list;
    ArrayList<HashMap<String, String>> couponList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        ImageButton reg_button = (ImageButton)findViewById(R.id.register_btn);
        list=(ListView)findViewById(R.id.listview1);

        couponList = new ArrayList<HashMap<String, String>>();
        getData(DATA_URL);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CouponActivity.this,CouponRegisterActivity.class);
                startActivity(intent);
            }
        });
        list.setOnItemClickListener(listener);

    }
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

          /*  Intent intent = new Intent(CouponActivity.this, CouponInfoActivity.class);
            intent.putExtra("coup_id", couponList.get(position).get("coup_id"));
            intent.putExtra("serial_num", couponList.get(position).get("serial_num"));
            intent.putExtra("image", couponList.get(position).get("image"));
            intent.putExtra("user_id", couponList.get(position).get("user_id"));

            startActivity(intent);*/
        }
    };

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                Log.i("rrr",uri);
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                Log.i("result",result);
                Log.i("myJSON",myJSON);

                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            couponsJson = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < couponsJson.length(); i++) {
                JSONObject c = couponsJson.getJSONObject(i);
                // if (LoginActivity.call_email.equals(c.getString(user_email))) {
                String coup_id = c.getString("coup_id");
                String serial_num = c.getString("serial_num");
                String image = c.getString("image");
                String user_id = c.getString("user_id");
                Log.i("coup_id",serial_num);
                HashMap<String, String> coupons = new HashMap<String, String>();

                coupons.put("coup_id", coup_id);
                coupons.put("serial_num", serial_num);
                coupons.put("image", image);
                coupons.put("user_id", user_id);

                couponList.add(coupons);
                //}
            }

            ListAdapter adapter = new SimpleAdapter(
                    CouponActivity.this, couponList, R.layout.list_item,
                    new String[]{"coup_id", "serial_num"},
                    new int[]{R.id.coup_id, R.id.serial_num}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}