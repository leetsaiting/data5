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

    public ListView lv_json;
    String url = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-0590E3E1-D81C-4BB7-8D12-19FE159CECCD&locationName=%E8%8A%B1%E8%93%AE%E7%B8%A3";

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

        JSONArray data = jsonObject.getJSONObject("records").getJSONArray("location");
        String str_loc = "location:" + data.getJSONObject(0).getString("locationName");
        list.add(str_loc);
        for(int i = 0 ; i < data.length(); i++){
            //JSONObject o = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(0);
            JSONObject o = data.getJSONObject(i);
            for(int k = 0 ; k < 3; k++) {
                JSONObject o2 = o.getJSONArray("weatherElement").getJSONObject(k).getJSONArray("time").getJSONObject(k);
                String str_tim = "startTime:" + o2.getString("startTime") + "\n" +
                        "endTime:" + o2.getString("endTime") + "\n";
                //list.add(str);

                String[] str_data = new String[5];
                for (int j = 0; j < 5; j++) {
                    JSONObject o3 = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(j).getJSONArray("time").getJSONObject(k).getJSONObject("parameter");
                    str_data[j] = o3.getString("parameterName");
                }

                String str = str_tim
                        +"最低溫度:" + str_data[2] + "°C" + "\n"
                        + "最高溫度:" + str_data[4] + "°C" + "\n"
                        + "天氣狀況:" + str_data[0] + "\n"
                        +"降雨機率:" + str_data[1] + "%";
                list.add(str);
            }
        }
        lv_json.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list));
    }


}
