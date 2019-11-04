package com.example.sajak.hamroguide.Agencies;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sajak.hamroguide.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundAgencies extends AsyncTask<Void, Void, Void>{
    private Context ctx;

    public BackgroundAgencies(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String json_url = "https://"+ R.string.server_url + "/hamroguide/getagenies.php";
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }

            httpURLConnection.disconnect();
            inputStream.close();
            bufferedReader.close();

            String json_data = stringBuilder.toString().trim();
            Log.d("JSON-String", json_data);
            JSONObject jsonObject = new JSONObject(json_data);
            JSONArray jsonArray = jsonObject.getJSONArray("agencies_server_response");
            AgenciesDBHolder agenciesDBHolder = new AgenciesDBHolder(ctx);
            SQLiteDatabase sqLiteDatabase = agenciesDBHolder.getWritableDatabase();

            int count = 0;
            while (count<jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                agenciesDBHolder.insert_agencies(
                        JO.getString("id"),
                        JO.getString("name"),
                        JO.getString("location"),
                        JO.getString("latitude"),
                        JO.getString("longitude"),
                        JO.getString("image"),
                        JO.getString("contact"),
                        sqLiteDatabase);
                count++;
            }
            agenciesDBHolder.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
