package com.example.easyeat.Api.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.easyeat.Api.Holders.SubCategoryHolder;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTaskSubCategories extends AsyncTask<Void, Void, SubCategoryHolder[]> {
    @Override
    protected SubCategoryHolder[] doInBackground(Void... voids) {
        try {
            String url = "http://192.168.0.107:8080/allSubCategories";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            SubCategoryHolder[] subCategoryHolders = restTemplate.getForObject(url, SubCategoryHolder[].class);
            return subCategoryHolders;
        } catch (Exception exception) {
            Log.e("", exception.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(SubCategoryHolder[] subCategoryHolders) {
        super.onPostExecute(subCategoryHolders);
    }

    public RestTaskSubCategories() {
        super();
    }
}
