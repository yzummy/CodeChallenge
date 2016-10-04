package com.example.yunchao.healthcare;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {


    private ListView lv;
    private Button bt;
    ArrayList<HashMap<String, String>> contactList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public HashMap<String,JSONObject> index = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.list);
        bt = (Button) findViewById(R.id.analyze);
        contactList = new ArrayList<>();
        HashMap<String, JSONObject> jsObj = new HashMap<>();
        String obj_id;
        int totalPatients = 1;
        int totalVaccine = 0, avgVaccine = 0;
        int totalMedication = 0, avgMedication = 0;
        String genders;
        HashMap<Integer, Integer> age = new HashMap<>();
        HashMap<String, Integer> gender = new HashMap<>();
        gender.put("Male", 0);
        gender.put("Female", 0);
        gender.put("Unspecified", 0);
        AssetManager assetManager = getAssets();

        // To get names of all files inside the "Files" folder
        try {
            String[] files = assetManager.list("");
            totalPatients = files.length - 2;
            for (int i = 0; i < files.length - 3; i++) {
                //System.out.println(files[i]);

                try {
                    JSONObject obj = new JSONObject(loadJSONFromAsset(files[i]));
                    //THis will store all the values inside "Hydrogen" in a element string
                    genders = obj.getJSONObject(("Demographics")).getJSONObject("Gender").getString("EnumValue");

                    //Gender distribution
                    gender.put(genders, gender.get(genders) + 1);

                    //immunization
                    totalVaccine += obj.getJSONObject("HealthRecord").getJSONObject("Immunizations").getJSONArray("Collection").length();
                    totalMedication += obj.getJSONObject("HealthRecord").getJSONObject("Medications").getJSONArray("Collection").length();

                    //THis will store "1" inside atomicNumber
                    String firstName = obj.getJSONObject("Name").getString("FirstName");
                    String lastName = obj.getJSONObject("Name").getString("LastName");
                    obj_id = obj.getString("id");

                    jsObj.put(obj_id, obj);
                    String name = firstName + " " + lastName;
                    index.put(name,obj);
                   // System.out.println(name);
                    //System.out.println(firstName);

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value

                    contact.put("name", name);


                    // adding contact to contact list
                    contactList.add(contact);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        avgMedication = totalMedication / totalPatients;
        avgVaccine = totalVaccine / totalPatients;

        Collections.sort(contactList, new MapComparator("name"));

        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, contactList,
                R.layout.list_item, new String[]{"name"}, new int[]{R.id.name});
        lv.setAdapter(adapter);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                //
                // System.out.println(position+" "+id);
                Intent intent = new Intent(view.getContext(),Profile.class);
                TextView textView = (TextView) view.findViewById(R.id.name);
                String n = textView.getText().toString();
                intent.putExtra("name",n);
                CharSequence text = "Not finished! Suppose to show details";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(view.getContext(), text, duration);
                toast.show();
                //startActivity(intent);

            }
        });


        final String finalTotalPatients = Integer.toString(totalPatients);
        final String finalAvgMedication = Integer.toString(avgMedication);
        final String finalAvgVaccine = Integer.toString(avgVaccine);
        final String[] gen = new String[]{Integer.toString(gender.get("Unspecified")), String.valueOf(gender.get("Male")), String.valueOf(gender.get("Female"))};
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),Analyze.class);
                intent.putExtra("totalPatients", finalTotalPatients);
                intent.putExtra("AvgMedication", finalAvgMedication);
                intent.putExtra("AvgImmunization", finalAvgVaccine);
                intent.putExtra("gender",gen);

                startActivity(intent);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public String loadJSONFromAsset(String files) {
        String json = null;
        try {


            InputStream is = getAssets().open(files);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
