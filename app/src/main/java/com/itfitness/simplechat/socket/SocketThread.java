package com.itfitness.simplechat.socket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.itfitness.simplechat.Constant;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketThread extends Thread{
    private Handler handler;
    private Socket socket;
    private String serverAddress;//服务器地址
    private int serverPort;//服务器端口
    private OutputStream outputStream;
    private InputStream inputStream;
    private String userName;
    private SocketThreadListener socketThreadListener;
    public SocketThread(String userName,String serverAddress,int serverPort){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                String msgBody = (String) msg.obj;
                Log.e("测试",msgBody);
                String[] paseMsg = msgBody.split(Constant.FLAG_PASE_SYMBOL);
                switch (paseMsg[0]){
                    case Constant.FLAG_MSG_SINGLE:
                        if(socketThreadListener!=null){
                            //收到个人消息的消息格式是：标记头##发送消息的用户名##消息
                            socketThreadListener.onReceivedSingleMsg(paseMsg[1],paseMsg[2]);
                        }
                        break;
                    case Constant.FLAG_MSG_ROOM:
                        //聊天室
                        if(socketThreadListener!=null){
                            //收到个人消息的消息格式是：标记头##发送消息的用户名##消息
                            socketThreadListener.onReceivedRoomMsg(paseMsg[1],paseMsg[2]);
                        }
                        break;
                    case Constant.FLAG_NOTIFY_UPDATECONNECT:
                        //更新联系人列表
                        if(socketThreadListener!=null){
                            socketThreadListener.onUpdateConnectList(paseMsg);
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        this.userName = userName;
    }

    /**
     * 获取当前用户的用户名
     * @return
     */
    public String getCurrentUserName(){
        return userName;
    }
    /**
     * 设置回调事件
     */
    public void setSocketThreadListener(SocketThreadListener socketThreadListener){
        this.socketThreadListener = socketThreadListener;
    }
    /**
     * 发送个人消息
     * @param oppositeuserName 对方的用户名
     * @param msg
     */
    public void sendMsg2Single(String oppositeuserName, String msg){
        try {
            if(outputStream!=null){
                //消息格式： 标记头##我的用户名##对方用户名##消息
                String msgBody = Constant.FLAG_MSG_SINGLE+Constant.FLAG_PASE_SYMBOL+userName+Constant.FLAG_PASE_SYMBOL+oppositeuserName+Constant.FLAG_PASE_SYMBOL+msg;
                outputStream.write(msgBody.getBytes());
                outputStream.flush();
            }
        }catch (Exception e){}
    }

    /**
     * 是否连接
     * @return
     */
    public boolean isConnected(){
        if(socket!=null){
            return socket.isConnected();
        }else {
            return false;
        }
    }
    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress,serverPort);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            outputStream.write((userName+"\n").getBytes());//向服务器发送自己的昵称
            int datalength = 0;
            byte[] data = new byte[1024];
            while ((datalength = inputStream.read(data)) != -1){
                String msgBody = new String(data, 0, datalength);
                Message msg = Message.obtain();
                msg.obj = msgBody;
                handler.sendMessage(msg);
            }
        }catch (Exception e){}
    }

    /**
     * 发送消息给所有人
     * @param msg
     */
    public void sendMsg2All(String msg) {
        try {
            if(outputStream!=null){
                //消息格式： 标记头##我的用户名##消息
                String msgBody = Constant.FLAG_MSG_ROOM+Constant.FLAG_PASE_SYMBOL+userName+Constant.FLAG_PASE_SYMBOL+msg;
                outputStream.write(msgBody.getBytes());
                outputStream.flush();
            }
        }catch (Exception e){}
    }

    public interface SocketThreadListener{
        /**
         * 收到个人消息
         * @param msg
         */
        void onReceivedSingleMsg(String oppositeUserName,String msg);

        /**
         * 收到聊天室消息
         * @param msg
         */
        void onReceivedRoomMsg(String oppositeUserName,String msg);

        /**
         * 当收到联系人列表更新的通知时
         * @param msgbodyarr
         */
        void onUpdateConnectList(String[] msgbodyarr);
    }
}
