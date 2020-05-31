package com.example.data5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.data5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ListView lv_json;
    String url = "http://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=a880adf3-d574-430a-8e29-3192a41897a5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_json = (ListView) findViewById(R.id.lv_json);
        getData(url);
    }

    private String getData(String urlString) {
        String result = "";
        //使用JsonObjectRequest類別要求JSON資料。
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(urlString,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            //Velloy採非同步作業，Response.Listener  監聽回應
                            public void onResponse(JSONObject response) {
                                Log.d("回傳結果", "結果=" + response.toString());
                                try{
                                    parseJSON(response);
                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Response.ErrorListener 監聽錯誤
                        Log.e("回傳結果", "錯誤訊息：" + error.toString());
                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        return result;

    }

    protected void parseJSON(JSONObject jsonObject) throws JSONException{
        ArrayList<String>list = new ArrayList();
        JSONArray data = jsonObject.getJSONObject("result").getJSONArray("results");
        for(int i = 0 ; i < data.length(); i++){
            JSONObject o = data.getJSONObject(i);
            String str = "id:" + o.getString("id") + "\n" +
                    "項次:" + o.getString("項次") + "\n" +
                    "停車場名稱:" + o.getString("停車場名稱") + "\n" +
                    "經度(WGS84):" + o.getString("經度(WGS84)") + "\n" +
                    "緯度(WGS84):" + o.getString("緯度(WGS84)");
            list.add(str);
        }
        lv_json.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list));
    }


}
