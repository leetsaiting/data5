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
        //for(int j = 0 ; j < limit; j++)
        JSONArray data = jsonObject.getJSONObject("records").getJSONArray("location");
        String str_loc = "location:" + data.getJSONObject(0).getString("locationName");
        list.add(str_loc);
        for(int i = 0 ; i < data.length(); i++){
            JSONObject o = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(0);
            JSONObject o2 = o.getJSONArray("time").getJSONObject(0);
            //String str = "elementName:" + o2.getString("elementName");
            //String str = "startTime:" + o2.getString("startTime") + "\n";
            /*JSONObject data_Wx = o2.getJSONObject("parameter");
            JSONObject data_PoP = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(1).getJSONArray("time").getJSONObject(0).getJSONObject("parameter");
            JSONObject data_MinT = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(2).getJSONArray("time").getJSONObject(0).getJSONObject("parameter");*/
            String str_tim = "startTime:" + o2.getString("startTime") + "\n" +
                    "endTime:" + o2.getString("endTime") + "\n"
                    /* + "現在溫度:" + data_MinT.getString("parameterName") + "°C" + "\n" +
                    "天氣狀況:" + data_Wx.getString("parameterName") + "\n" +
                    "降雨機率:" + data_PoP.getString("parameterName") + "%"*/;
            //list.add(str);

            String str_MinT = null;
            String str_Wx = null;
            String str_PoP = null;
            for (int j = 0 ; j < o2.length(); j++){
                JSONObject o3 = data.getJSONObject(i).getJSONArray("weatherElement").getJSONObject(j).getJSONArray("time").getJSONObject(0).getJSONObject("parameter");

                if(j == 2)str_MinT = "現在溫度:" + o3.getString("parameterName") + "°C" + "\n" ;
                if(j == 0)str_Wx = "天氣狀況:" + o3.getString("parameterName") + "\n" ;
                if(j == 1)str_PoP ="降雨機率:" + o3.getString("parameterName") + "%";

            }

            String str2 = str_tim + str_MinT + str_Wx + str_PoP ;
            list.add(str2);

            //JSONObject data2 = o.getJSONArray("weatherElement")
            //JSONObject o = data.getJSONObject(i).getJSONArray("weatherElement");
            //JSONObject o2 = o.getJSONArray("time");
                    /*for(int j = 0 ; j < o2.length(); j++){
                        JSONObject o3 = o.getJSONObject(j);
                        //JSONObject o3 = o.getJSONObject(j).getJSONArray("time").getJSONObject(0);
                        //String str = "elementName:" + o2.getString("elementName");
                        String str = "startTime:" + o3.getString("startTime") + "\n";
                        list.add(str);
                    }*/
        }
        /*JSONArray data_2 = jsonObject.getJSONObject("records").getJSONArray("location");
        for(int i = 0 ; i < data_2.length(); i++){
            JSONObject o = data_2.getJSONObject(i);
            String str = "locationName:" + o.getString("locationName");
            list.add(str);
        }*/
        lv_json.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list));
    }


}
