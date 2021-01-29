package com.example.easyeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.easyeat.model.Order;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private ArrayList<Order> orderList;
    private ListView commandeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        orderList = (ArrayList<Order>) intent.getSerializableExtra("orderList");

        commandeList = (ListView) findViewById(R.id.commandeList);

    }
}