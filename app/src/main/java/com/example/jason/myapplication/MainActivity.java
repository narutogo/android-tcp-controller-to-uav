package com.example.jason.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar1,seekbar2,seekbar3,seekbar4;
    private EditText edittext1;
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekbar1=(SeekBar)findViewById(R.id.seekBar);
        seekbar2=(SeekBar)findViewById(R.id.seekBar2);
        seekbar3=(SeekBar)findViewById(R.id.seekBar3);
        seekbar4=(SeekBar)findViewById(R.id.seekBar4);
        edittext1=(EditText)findViewById(R.id.editText);
        textview=(TextView)findViewById(R.id.textView);
        seekbar1.setProgress(0);
        seekbar2.setProgress(50);
        seekbar3.setProgress(50);
        seekbar4.setProgress(50);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress()+"" );
                UdpClient("a" + seekBar.getProgress() + " ");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress() + " ");
                UdpClient("b" + seekBar.getProgress() + " ");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress() + " ");
                UdpClient("c" + seekBar.getProgress() + " ");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress() + " ");
                UdpClient("d" + seekBar.getProgress() + " ");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }
    public void UdpClient(final String sendStr){
         final String netAddress = edittext1.getText().toString();
         final int PORT = 8081;

        new Thread ( new Runnable(){
            @Override
            public void run() {
                DatagramPacket datagramPacket = null;
                DatagramSocket datagramSocket = null;
                try {
                    datagramSocket = new DatagramSocket();
                    byte[] buf = sendStr.getBytes();
                    InetAddress address = InetAddress.getByName(netAddress);
                    datagramPacket = new DatagramPacket(buf, buf.length, address, PORT);
                    datagramSocket.send(datagramPacket);

                    //获取服务端ip
                    // String serverIp = recePacket.getAdress();
                } catch (
                        UnknownHostException e
                        )
                {
                    e.printStackTrace();
                } catch (
                        SocketException e
                        )
                {
                    e.printStackTrace();
                } catch (
                        IOException e
                        )
                {
                    e.printStackTrace();
                } finally
                {
                    // 关闭socket
                    if (datagramSocket != null) {
                        datagramSocket.close();
                    }
                }
            }
        }).start();
    }




}


