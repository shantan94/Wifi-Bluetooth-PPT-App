package com.example.shantan.myapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

/**
 * Created by shantan on 7/27/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button forward = null;
    private Button back = null;
    private Button go_to_slide = null;
    private Button slide_show = null;
    private Button exit_slide_show = null;
    private TextView t1 = null;
    public OutputStream outputStream;
    public InputStream inStream;
    public String ip = "";
    public int port = 0;
    public int index = 1;
    public String totalSlides = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent next = this.getIntent();
        Bundle b = next.getExtras();
        ip = b.getString("ip");
        port = b.getInt("port");
        setContentView(R.layout.activity_main);
        findViewById();
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(ip, port),10000);
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.write(("getdata").getBytes());
            dout.flush();
            s.shutdownOutput();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String x = br.readLine();
            String[] break1 = x.split(" ");
            totalSlides = break1[0];
            setCurrentSlide(break1[1]);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findViewById() {
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
            Log.d("dfkj","dkhfkd");
            if(Integer.parseInt(totalSlides) >= Integer.parseInt(cur_slide)+1) {
                Socket s = new Socket();
                Log.d("jfnd",ip+"dkj");
                try {
                    s.connect(new InetSocketAddress(ip, port), 10000);
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.write(("forward " + cur_slide).getBytes());
                    dout.flush();
                    s.shutdownOutput();
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String s1 = br.readLine();
                    setCurrentSlide(s1);
                    Log.d("in", "kjdsfhkj");
                    s.close();
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
            if((Integer.parseInt(cur_slide)-1) >= index) {
                Socket s = new Socket();
                try {
                    s.connect(new InetSocketAddress(ip, port), 10000);
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.write(("back " + cur_slide).getBytes());
                    dout.flush();
                    s.shutdownOutput();
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String s1 = br.readLine();
                    setCurrentSlide(s1);
                    Log.d("in", "kjdsfhkj");
                    s.close();
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
            if((Integer.parseInt(go_slide) >= index) && (Integer.parseInt(go_slide) <= Integer.parseInt(totalSlides))){
                Socket s = new Socket();
                try {
                    s.connect(new InetSocketAddress(ip, port), 10000);
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.write(("goto " + go_slide).getBytes());
                    dout.flush();
                    s.shutdownOutput();
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String s1 = br.readLine();
                    setCurrentSlide(s1);
                    Log.d("in", "kjdsfhkj");
                    s.close();
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
            Socket s = new Socket();
            try {
                s.connect(new InetSocketAddress(ip, port), 10000);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.write(("slideshow").getBytes());
                dout.flush();
                s.shutdownOutput();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String s1 = br.readLine();
                setCurrentSlide(s1);
                Log.d("in", "kjdsfhkj");
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Socket s = new Socket();
            TextView t1 = (TextView)findViewById(R.id.current_slide);
            try {
                s.connect(new InetSocketAddress(ip, port), 10000);
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.write(("exit").getBytes());
                dout.flush();
                s.shutdownOutput();
                s.close();
                Log.d("in", "kjdsfhkj");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
