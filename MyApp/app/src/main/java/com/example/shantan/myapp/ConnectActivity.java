package com.example.shantan.myapp;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by shantan on 7/29/2017.
 */

public class ConnectActivity extends AppCompatActivity implements View.OnClickListener {

    private Button connect = null;
    private Button bluetooth_en = null;
    public BluetoothDevice device;
    private BluetoothSocket btSocket;
    private OutputStream out;
    private static String btAdress = "Enter here";  //Enter your laptop/computer bluetooth address
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        findViewById();
    }

    public void findViewById(){
        connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        bluetooth_en = (Button)findViewById(R.id.bluetooth_connect);
        bluetooth_en.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText e = (EditText)findViewById(R.id.IP_Address);
        EditText e1 = (EditText)findViewById(R.id.port_num);
        if(view == connect){
            String ip = e.getText().toString();
            int port = Integer.parseInt(e1.getText().toString());
            Socket s = new Socket();
            try{
                s.connect(new InetSocketAddress(ip, port), 10000);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.write(("first").getBytes());
                dout.flush();
                s.shutdownOutput();
                Bundle store = new Bundle();
                store.putString("ip", ip);
                store.putInt("port", port);
                Intent next = new Intent(getApplicationContext(), MainActivity.class);
                next.putExtras(store);
                startActivity(next);
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid IP or port number", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            device = mBluetoothAdapter.getRemoteDevice(btAdress);
            try{
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                btSocket.connect();
                DataOutputStream dout = new DataOutputStream(btSocket.getOutputStream());
                dout.write(("first").getBytes());
                dout.flush();
                Bundle store = new Bundle();
                store.putString("device", btAdress);
                store.putString("uuid", String.valueOf(MY_UUID));
                Intent next = new Intent(getApplicationContext(), MainActivity1.class);
                next.putExtras(store);
                startActivity(next);
//                out = btSocket.getOutputStream();
//                byte[] buffer = "hello".getBytes();
//                out.write(buffer);
//                out.flush();
            }
            catch (Exception ex){
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid Bluetooth address or uuid", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
