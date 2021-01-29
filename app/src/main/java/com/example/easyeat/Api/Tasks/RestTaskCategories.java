package com.example.easyeat.Api.Tasks;

import android.os.AsyncTask;
import android.util.Log;


import com.example.easyeat.Api.Holders.CategoryHolder;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTaskCategories extends AsyncTask<Void, Void, CategoryHolder[]> {


    @Override
    protected CategoryHolder[] doInBackground(Void... params) {
        try {
            String url = "http://192.168.0.107:8080/allCategories";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            CategoryHolder[] categories = restTemplate.getForObject(url, CategoryHolder[].class);
            return categories;
        } catch (Exception exception) {
            Log.e("", exception.getMessage());
        }
        return null;
    }

    public RestTaskCategories() {
        super();
    }

    @Override
    protected void onPostExecute(CategoryHolder[] categories) {
        super.onPostExecute(categories);

    }
}
