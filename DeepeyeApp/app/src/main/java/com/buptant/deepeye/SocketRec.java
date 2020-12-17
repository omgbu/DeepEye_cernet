package com.buptant.deepeye;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JiaoJiao on 2017/8/19.
 */

public class
SocketRec {
    private String TAG = "socket";
    private Socket clientSocket;
 //   private InputStream recvStream;
    private Handler mHandler;
    public int start = 0;
    public int end = 0;
    public boolean q = true;
//    public static String filename = "log_rec";
    public static String filename1 = "log_tran";
    private String IMEI;
    public SocketRec(Context context, Handler handler){
        mHandler=handler;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            IMEI = tm.getDeviceId();
        }
        new initSocket().start();
    }

    private class initSocket extends Thread{  //initsocket链接
        @Override
        public void run() {
            super.run();
                try {
//                   clientSocket = new Socket("10.112.50.120", 1051);
                    clientSocket = new Socket("2001:da8:215:6a01::b198", 1051);
                    Log.d(TAG, "客户端连接成功");
                } catch (IOException e) {
                    e.printStackTrace();
                    q = false;
                }
            if(q)
            new RecvThread().start();
            }
        }

    public class RecvThread extends Thread {
        @Override
        public void run() {
            int i =0;
            JSONObject jsonim;
            jsonim = new JSONObject();
            OutputStream mOutStream =null;
            try {
                jsonim.put("imei", IMEI);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mOutStream = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mOutStream.write(jsonim.toString().getBytes());
                mOutStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
            while(true) {
                InputStream in = null;
                try {
                    in = clientSocket.getInputStream();
                int l =0;
                byte[] buf = new byte[1024];
                int bytes = in.read(buf);
                JSONObject jsonObj = new JSONObject();
                if(bytes>0){
                try{
                    jsonObj = new JSONObject(new String(buf, 0, bytes));
                    System.out.println(jsonObj.toString());
                    l =jsonObj.getInt("bytes");
                }
                catch(JSONException e) {
                    e.printStackTrace();
                    break;
                }}else {break;}
                try {
                    sendAcknowledgement(clientSocket,1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                InputStream in1 = clientSocket.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                byte[] data =new byte[1024];
                int len =0;
                int offset = 0;
                    while ((len=in1.read(buffer))>0){
                        outStream.write(buffer,0,len);
                        outStream.flush();
                        offset += len;
                        if (offset >= l) {
                            break;
                        }
                    }
                    outStream.close();
                data = outStream.toByteArray();
               // Log.d("jj", outStream.toString());
                try {
                    sendAcknowledgement(clientSocket,1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                writelog("end"+end , filename1);
                end++;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                Message msg = mHandler.obtainMessage();
                msg.what = Constants.MESSAGE_IMAGE_RECEIVED;
                msg.obj = bitmap;
                mHandler.sendMessage(msg);
                i++;
                }catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            }

        }

    public void cancel() {
        if(clientSocket!=null){
        try {
            clientSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }}
    }

    public static void sendAcknowledgement(Socket s, int jSON_VALUE_OK) throws JSONException, IOException {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("acknowledgement", jSON_VALUE_OK);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        OutputStream out = s.getOutputStream();
        out.write(jobj.toString().getBytes());
        out.flush();
        Log.d("jj", jobj.toString());
       // out.close();

    }
    public void writelog(String log, String filename){
        SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm:ss.SSS");
        String str4 = sdf4.format(new Date());
        String time4 = str4;
        String str = time4+"\t"+log+"\n";
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdDire = Environment.getExternalStorageDirectory();
                FileOutputStream outFileStream = new FileOutputStream(
                        sdDire.getCanonicalPath() + "/" + filename + ".txt", true);
                outFileStream.write(str.getBytes());
                outFileStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




