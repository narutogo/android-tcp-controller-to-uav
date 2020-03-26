package com.example.jason.myapplication;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekbar1,seekbar2,seekbar3,seekbar4;
    private EditText edittext1,mes;
    private TextView textview,receive,textview2;
    private Button on,off,send,heion,heioff;
    private Vibrator vibrator;
    boolean heiflag=false;
    private boolean systemPower=false;
    private int manual_throttle=0,pid_throttle=50;
    private boolean sendOkFlag=false;
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        seekbar1=(SeekBar)findViewById(R.id.seekBar);
        seekbar2=(SeekBar)findViewById(R.id.seekBar2);
        seekbar3=(SeekBar)findViewById(R.id.seekBar3);
        seekbar4=(SeekBar)findViewById(R.id.seekBar4);
        edittext1=(EditText)findViewById(R.id.editText);
        textview=(TextView)findViewById(R.id.textView);
        textview2=(TextView)findViewById(R.id.textView2);
        receive=(TextView)findViewById(R.id.receive);
        mes=(EditText)findViewById(R.id.instruction);
        on=(Button)findViewById(R.id.buttonon);
        off=(Button)findViewById(R.id.buttonoff);
        send=(Button)findViewById(R.id.buttonsend);
        heion=(Button)findViewById(R.id.heion);
        heioff=(Button)findViewById(R.id.heioff);
        new Thread(new Runnable() {

            DatagramPacket datagrampacket = null;
            DatagramSocket datagramsocket = null;

            public void run() {
                byte[] receiveData = new byte[1024];
                datagrampacket = new DatagramPacket(receiveData, receiveData.length);
                Log.i("sd", "1");
                try {
                    //InetSocketAddress socketAddres = new InetSocketAddress(65531);
                    datagramsocket = new DatagramSocket(null);
                    datagramsocket.setReuseAddress(true);
                    datagramsocket.bind(new InetSocketAddress(8082));

                    while (true) {
                        datagramsocket.receive(datagrampacket);
                        byte[] datas = datagrampacket.getData();
                        json = new String(datagrampacket.getData(), 0, datagrampacket.getLength());
                        if (json.equals("@")) {
                            sendOkFlag = true;
                        } else {
                            sendOkFlag = false;
                        }
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                receive.setText(json);
                               //  textview2.setText(json);
                            }
                        });
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    Log.i("sd", "2");

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("sd", "3");
                }

            }
        }).start();

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdpClient("e6!");
            }

        });


        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdpClient("e1!");//system off
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UdpClient(mes.getText().toString() + "!");//system off
            }
        });
        heion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heiflag=true;
                seekbar1.setProgress(pid_throttle);
                UdpClient("B1".toString() + "!");
            }
        });
        heioff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heiflag=false;
                seekbar1.setProgress(50);
                UdpClient("B0".toString() + "!");
            }
        });
        seekbar1.setProgress(0);
        seekbar2.setProgress(50);
        seekbar3.setProgress(50);
        seekbar4.setProgress(50);
        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(heiflag==false) {
                    textview.setText(seekBar.getProgress() + "throttle");
                    manual_throttle=seekBar.getProgress();
                    UdpClient("d" + seekBar.getProgress() + "!");
                }else{
                    textview.setText(seekBar.getProgress() + "heipid");
                    pid_throttle= seekBar.getProgress();
                    UdpClient("A" + seekBar.getProgress() + "!");
                }

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
                UdpClient("a" + seekBar.getProgress() + "!");
                if(seekBar.getProgress()==50)
                    vibrator.vibrate(100);
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
                UdpClient("b" + seekBar.getProgress() + "!");
                if(seekBar.getProgress()==50)
                    vibrator.vibrate(100);
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
                UdpClient("c" + seekBar.getProgress() + "!");
                if(seekBar.getProgress()==50)
                    vibrator.vibrate(100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }
    public boolean UdpClient(final String sendStr){
         final String netAddress = edittext1.getText().toString();
         final int PORT = 8081;
     for(int i=0;i<5;i++) {
         new Thread(new Runnable() {
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
                         ) {
                     e.printStackTrace();
                 } catch (
                         SocketException e
                         ) {
                     e.printStackTrace();
                 } catch (
                         IOException e
                         ) {
                     e.printStackTrace();
                 } finally {
                     // 关闭socket
                     if (datagramSocket != null) {
                         datagramSocket.close();
                     }
                 }
             }
         }).start();
         try {
             Thread.sleep(50);
         } catch (InterruptedException e) {

         }
         if(sendOkFlag==true){
             break;
         }
     }
        if(sendOkFlag==true){
            sendOkFlag=false;
            return true;
        }else{
            receive.setText("sendflase");
            return false;
        }

    }




}


