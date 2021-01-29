package com.example.easyeat.Api.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.easyeat.Api.Holders.ItemHolder;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTaskItems extends AsyncTask<Void, Void, ItemHolder[]> {
    @Override
    protected ItemHolder[] doInBackground(Void... voids) {
        try {
            String url = "http://192.168.0.107:8080/allItems";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ItemHolder[] itemHolders = restTemplate.getForObject(url, ItemHolder[].class);
            return itemHolders;
        } catch (Exception exception) {
            Log.e("", exception.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(ItemHolder[] itemHolders) {
        super.onPostExecute(itemHolders);
    }

    public RestTaskItems() {
        super();
    }
}
