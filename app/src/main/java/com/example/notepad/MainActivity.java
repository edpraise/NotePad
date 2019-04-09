package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    //bellow ill declear the fields designed on the activity main and to_do layout
    ListView listView;
    ArrayList<String>arrayList;
    ArrayAdapter<String>arrayAdapter;
    String messageText;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //here i'd define the declared variables above

        listView= (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, arrayList);
        //bellow i am setting a link between the listView and the arrayAdapter
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,EditMessageClass.class);
                intent.putExtra(Intent_Constants.INTENT_MESSAGE_DATA,arrayList.get(position).toString());
                // this code will pass the location to which the element was present
                intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION,position);
                startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE_TWO);
            }
        });

        try {
            Scanner sc = new Scanner(openFileInput("Notepad.txt"));
            while (sc.hasNextLine()){
                String data =sc.nextLine();
                arrayAdapter.add(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

// the method below calls the onclick method that was called in the activity.main tab

    @Override
    public void onBackPressed() {
        try {
            PrintWriter pw = new PrintWriter(openFileOutput("Notepad.txt",Context.MODE_PRIVATE));
            for (String data : arrayList){
                pw.print(data);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();


    }

    public void onClick(View view){
        //these code bellow will launch a new activity
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,EditFieldClass.class);
        //the method bellow will invoke the EditFieldClass method called above
        startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Intent_Constants.INTENT_REQUEST_CODE){
            messageText = data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
            arrayList.add(messageText);
            arrayAdapter.notifyDataSetChanged();

        }else if (resultCode==Intent_Constants.INTENT_REQUEST_CODE_TWO){
            messageText = data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
            position = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION,-1);
            arrayList.remove(position);
            arrayList.add(position,messageText);
            arrayAdapter.notifyDataSetChanged();

        }
    }
}
