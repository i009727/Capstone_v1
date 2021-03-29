package org.techtown.capstone_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    HttpURLConnection conn = null;
    BufferedReader reader;
    BufferedWriter writer;
    InputStreamReader streamReader;
    OutputStream outputStream;

    JSONObject sendObject;
    JSONArray sendArray1;
    JSONArray sendArray2;

    TextView textView;
    EditText editText;
    Button button_submit;
    Button button_json_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        sendObject = new JSONObject();
        sendArray1 = new JSONArray();
        sendArray2 = new JSONArray();
        connect();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(s.toString());
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Hello Android!!", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), textView.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

        button_json_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPostJson();
                        } catch (Exception e) {}
                    }
                }).start();
            }
        });
    }

    public void sendPostJson() throws Exception {
        try {
            OutputStream os = conn.getOutputStream();
            byte[] input = sendObject.toString().getBytes();
            os.write(input, 0, input.length);
        } catch (Exception e) {}

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (Exception e) {}
    }

    public void connect() {
        try {
//            URL url = new URL("http://101.101.216.124/api/v1/save");
            URL url = new URL("https://webhook.site/b4a0b30e-359b-43f7-876d-6e275cb5b786");
            conn = (HttpURLConnection)url.openConnection();
            if (conn == null) {
                Toast.makeText(getApplicationContext(), "HTTP Connection Error...", Toast.LENGTH_LONG).show();
            }
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeJsonData() {
        try {
            for (int i = 0; i < 10; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", "joo" + i);
                obj.put("num", i);
                sendArray1.put(obj);
            }
            sendObject.put("content1", sendArray1);
            for (int i = 0; i < 10; i++) {
                JSONObject obj = new JSONObject();
                obj.put("name", "hyung" + i);
                obj.put("num", i);
                sendArray2.put(obj);
            }
            sendObject.put("content2", sendArray2);
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    private void findViews() {
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        button_submit = (Button)findViewById(R.id.button);
        button_json_submit = (Button)findViewById(R.id.button_json);
    }
}