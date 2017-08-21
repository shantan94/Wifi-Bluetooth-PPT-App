package com.example.shantan.myapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by shantan on 7/31/2017.
 */

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    private Button forward = null;
    private Button back = null;
    private Button go_to_slide = null;
    private Button slide_show = null;
    private Button exit_slide_show = null;
    private TextView t1 = null;
    public String uuid = "";
    public String btaddress = "";
    public BluetoothDevice device;
    private BluetoothSocket btSocket;
    private OutputStream out;
    private byte[] buffer = new byte[1024];
    public InputStream din;
    private static UUID MY_UUID;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public DataOutputStream dout = null;
    public int index = 1;
    public String totalSlides = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent next = this.getIntent();
        Bundle b = next.getExtras();
        uuid = b.getString("uuid");
        btaddress = b.getString("device");
        MY_UUID = UUID.fromString(uuid);
        setContentView(R.layout.activity_main1);
        findViewById();
        device = mBluetoothAdapter.getRemoteDevice(btaddress);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            dout = new DataOutputStream(btSocket.getOutputStream());
            dout.write(("getdata").getBytes());
            din = btSocket.getInputStream();
            int y = din.read(buffer);
            String x = new String(buffer, 0, y);
            String[] break1 = x.split(" ");
            totalSlides = break1[0];
            setCurrentSlide(break1[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findViewById(){
        forward = (Button)findViewById(R.id.forward);
        forward.setOnClickListener(this);
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        go_to_slide = (Button)findViewById(R.id.go_to_slide);
        t1 = (TextView)findViewById(R.id.current_slide);
        go_to_slide.setOnClickListener(this);
        slide_show = (Button)findViewById(R.id.slide_show);
        slide_show.setOnClickListener(this);
        exit_slide_show = (Button)findViewById(R.id.exit_slide_show);
        exit_slide_show.setOnClickListener(this);
    }

    public void setCurrentSlide(String cs){
        t1.setText(cs + "/" + totalSlides);
    }

    @Override
    public void onClick(View view) {
        String cur_slide = t1.getText().toString().split("/")[0];
        if(view == forward){
            device = mBluetoothAdapter.getRemoteDevice(btaddress);
            if(Integer.parseInt(totalSlides) >= Integer.parseInt(cur_slide)+1) {
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    btSocket.connect();
                    dout = new DataOutputStream(btSocket.getOutputStream());
                    dout.write(("forward " + cur_slide).getBytes());
                    din = btSocket.getInputStream();
                    int y = din.read(buffer);
                    String s1 = new String(buffer, 0, y);
                    setCurrentSlide(s1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Reached max length", Toast.LENGTH_SHORT).show();
                Log.d("status","Max length reached");
            }
        }
        else if(view == back){
            TextView t1 = (TextView)findViewById(R.id.current_slide);
            device = mBluetoothAdapter.getRemoteDevice(btaddress);
            if((Integer.parseInt(cur_slide)-1) >= index) {
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    btSocket.connect();
                    dout = new DataOutputStream(btSocket.getOutputStream());
                    dout.write(("back " + cur_slide).getBytes());
                    din = btSocket.getInputStream();
                    int y = din.read(buffer);
                    String s1 = new String(buffer, 0, y);
                    setCurrentSlide(s1);
                    Log.d("in", "kjdsfhkj");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Reached min length", Toast.LENGTH_SHORT).show();
                Log.d("status","Min length reached");
            }
        }
        else if(view == go_to_slide){
            EditText e1 =(EditText)findViewById(R.id.get_slide);
            String go_slide = e1.getText().toString();
            device = mBluetoothAdapter.getRemoteDevice(btaddress);
            if((Integer.parseInt(go_slide) >= index) && (Integer.parseInt(go_slide) <= Integer.parseInt(totalSlides))){
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    btSocket.connect();
                    dout = new DataOutputStream(btSocket.getOutputStream());
                    dout.write(("goto " + go_slide).getBytes());
                    din = btSocket.getInputStream();
                    int y = din.read(buffer);
                    String s1 = new String(buffer, 0, y);
                    setCurrentSlide(s1);
                    Log.d("in", "kjdsfhkj");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Page number does not exist", Toast.LENGTH_SHORT).show();
                Log.d("status","Page number does not exist");
            }
        }
        else if(view == slide_show){
            TextView t1 = (TextView)findViewById(R.id.current_slide);
            device = mBluetoothAdapter.getRemoteDevice(btaddress);
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                btSocket.connect();
                dout = new DataOutputStream(btSocket.getOutputStream());
                dout.write(("slideshow").getBytes());
                din = btSocket.getInputStream();
                int y = din.read(buffer);
                String s1 = new String(buffer, 0, y);
                setCurrentSlide(s1);
                Log.d("in", "kjdsfhkj");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            TextView t1 = (TextView)findViewById(R.id.current_slide);
            device = mBluetoothAdapter.getRemoteDevice(btaddress);
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                btSocket.connect();
                dout = new DataOutputStream(btSocket.getOutputStream());
                dout.write(("exit").getBytes());
                Log.d("in", "kjdsfhkj");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
