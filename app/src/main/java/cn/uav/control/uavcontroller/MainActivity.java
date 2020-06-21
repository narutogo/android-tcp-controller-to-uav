package cn.uav.control.uavcontroller;
import cn.uav.control.uavcontroller.VerticalSeekBar;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;


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
import java.lang.annotation.Target;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class MainActivity extends Activity {//AppCompatActivity {
    private SeekBar seekbar1,seekbar2,seekbar3,seekbar4,pitchmic,rollmic;
    private EditText edittext1,mes;
    private TextView textview,receive,textview2;
    private Button on,info,send,heion,chirp,vertical,control;
    private Vibrator vibrator;
    boolean heiflag=false;
    private boolean systemPower=false;
    private int manual_throttle=0,pid_throttle=50;
    private boolean sendOkFlag=false;
    String json;
    int LastAngle, LastStrength;
    JoystickView joystick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        seekbar1=(SeekBar)findViewById(R.id.seekBar);
        seekbar2=(SeekBar)findViewById(R.id.seekBar2);
        pitchmic=(SeekBar)findViewById(R.id.pitchmic);
        rollmic=(SeekBar)findViewById(R.id.rollmic);
        //  seekbar3=(SeekBar)findViewById(R.id.seekBar3);
        //  seekbar4=(SeekBar)findViewById(R.id.seekBar4);
        edittext1=(EditText)findViewById(R.id.editText);
        textview=(TextView)findViewById(R.id.textView);
        //    textview2=(TextView)findViewById(R.id.textView2);
        receive=(TextView)findViewById(R.id.receive);
        mes=(EditText)findViewById(R.id.instruction);
        on=(Button)findViewById(R.id.buttonon);
        info=(Button)findViewById(R.id.info);
        send=(Button)findViewById(R.id.buttonsend);
        heion=(Button)findViewById(R.id.heion);
        chirp=(Button)findViewById(R.id.chirp);
        vertical=(Button)findViewById(R.id.vertical);
        control=(Button)findViewById(R.id.control);

        // mRockerView.findViewById(R.id.myrocker);
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
                UdpClient("e1!");//system info
                on.setText("开启");

            }

        });
        on.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                UdpClient("e6!");
                on.setText("关闭");
                return  true;
            }
        });


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdpClient("C!");//system info
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UdpClient(mes.getText().toString() + "!");//system info
            }
        });
        heion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  heiflag=true;
               // seekbar3.setProgress(75);
              //  seekbar4.setProgress(50);
                UdpClient("B0".toString() + "!");
                heion.setText("定高开");

            }
        });
        heion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UdpClient("B1".toString() + "!");
                heion.setText("定高关");
                return true;
            }
        });
        chirp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  heiflag=false;
               // seekbar3.setProgress(50);
              //  seekbar4.setProgress(50);
                UdpClient("p0".toString() + "!");
                chirp.setText("鸣叫开");
            }
        });
        chirp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UdpClient("p1".toString() + "!");
                chirp.setText("鸣叫关");
                return true;
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdpClient("P0".toString() + "!");
                control.setText("软控");

            }
        });
        control.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UdpClient("P1".toString() + "!");
                control.setText("遥控");
                return true;
            }
        });
        vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UdpClient("F0".toString() + "!");
              //  joystick.setX(0);
              //  joystick.setY(0);
                seekbar2.setProgress(50);
               UdpClient("c" + seekbar2.getProgress() + "!");
               vertical.setText("水平");
            }
        });
        vertical.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UdpClient("F1".toString() + "!");
                seekbar2.setProgress(50);
                vertical.setText("垂直");
                return true;
            }
        });
        seekbar1.setProgress(0);
        seekbar2.setProgress(50);
        //  seekbar3.setProgress(50);
        //  seekbar4.setProgress(50);
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
                UdpClient("c" + seekBar.getProgress() + "!");
                if(seekBar.getProgress()==50)
                    vibrator.vibrate(100);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(50);
                UdpClient("c" + seekBar.getProgress() + "!");
                textview.setText(seekBar.getProgress() + " ");
            }
        });
        pitchmic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress() + " ");
                UdpClient("K" + (seekBar.getProgress()-20)*2 + "!");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        rollmic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textview.setText(seekBar.getProgress() + " ");
                UdpClient("L" + (seekBar.getProgress()-20)*2 + "!");

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
      /*  seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        });*/
        joystick = (JoystickView) findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                double x,y,z;
                Log.e("test", "ang" + angle + "st" + strength);
                if(abs(angle-LastAngle)>20||abs(strength-LastStrength)>8) {

                    LastAngle=angle;LastStrength=strength;
                    // do whatever you want
                    if (angle >= 0 && angle < 90) {
                        x = -sin(angle/180.0*PI) * strength / 100*50;
                        y = cos(angle/180.0*PI) * strength / 100*50;
                    } else if (angle >= 90 && angle < 180) {
                        angle = 180 - angle;
                        x = -sin(angle/180.0*PI) * strength / 100*50;
                        y = -cos(angle/180.0*PI) * strength / 100*50;
                    } else if (angle >= 180 && angle < 270) {
                        angle = 270 - angle;
                        x = cos(angle/180.0*PI) * strength / 100*50;
                        y = -sin(angle/180.0*PI) * strength / 100*50;
                    } else {
                        angle = 360 - angle;
                        x = sin(angle/180.0*PI) * strength / 100*50;
                        y = cos(angle/180.0*PI) * strength / 100*50;
                    }
                    z=x;
                    x=y;y=z;x=-x;y=-y;
                    x+=50;y+=50;

                    Log.e("test", "x " +(float) x + " y " +(float) y);
                    textview.setText("x" +(float) x + " y " +(float) y);
                    UdpClient("b" +(float) x + "!");
                  /* try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                     }
*/

                    UdpClient("a" + (float)y + "!");
                    if(strength<3)
                        vibrator.vibrate(100);
                }
            }


        });




    }
    public boolean UdpClient(final String sendStr){
        final String netAddress = edittext1.getText().toString();
        final int PORT = 8081;
        for(int i=0;i<2;i++) {
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
                Thread.sleep(30);
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
            receive.setText("发送失败");
            return false;
        }

    }




}


