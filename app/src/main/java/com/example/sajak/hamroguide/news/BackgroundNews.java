package com.example.sajak.hamroguide.news;

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

public class BackgroundNews extends AsyncTask<Void, Void, Void>{

    private Context ctx;
    ProgressDialog dialog;

    public BackgroundNews(Context ctx){
        this.ctx = ctx;
//        dialog = new ProgressDialog(ctx, ProgressDialog.THEME_HOLO_DARK);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        // set indeterminate style
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setCancelable(false);
//        // set title and message
//        dialog.setTitle("Please wait");
//        dialog.setMessage("Loading Agencies..");
//        // and show it
//        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String json_url = "http://"+ R.string.server_url +"/hamroguide/getnews.php";
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
            Log.d("Hospital JSON-String", json_data);
            JSONObject jsonObject = new JSONObject(json_data);
            JSONArray jsonArray = jsonObject.getJSONArray("news_server_response");
            NewsDBHelper newsDBHelper = new NewsDBHelper(ctx);
            SQLiteDatabase sqLiteDatabase = newsDBHelper.getWritableDatabase();

            int count = 0;
            while (count<jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                newsDBHelper.insert_news_data(
                        JO.getString("news_id"),
                        JO.getString("head"),
                        JO.getString("news_content"),
                        JO.getString("news_date"),
                        JO.getString("writer"),
                        JO.getString("news_image"),
                        JO.getString("website"),
                        JO.getString("images"),
                        JO.getString("imaget"),
                        sqLiteDatabase);
                count++;
            }
            newsDBHelper.close();

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
//        if (dialog.isShowing()){
//            dialog.dismiss();
//        }
    }
}