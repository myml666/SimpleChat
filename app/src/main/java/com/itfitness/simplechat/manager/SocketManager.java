package com.itfitness.simplechat.manager;
import android.os.Bundle;
import android.util.Log;
import com.itfitness.simplechat.socket.SocketThread;

public class SocketManager {
    public static SocketThread socketThread;
    public static void init(Bundle socketInitData){
        String username = socketInitData.getString("username");
        String port = socketInitData.getString("port");
        String address = socketInitData.getString("address");
        socketThread = new SocketThread(username,"192.168.192.1",Integer.valueOf(port));
    }

    /**
     * 设置消息监听
     * @param socketListener
     */
    public static void setSocketListener(SocketThread.SocketThreadListener socketListener){
        if(socketThread!=null){
            socketThread.setSocketThreadListener(socketListener);
            if(!isConnected()){
                Log.e("测试","开始启动啦");
                socketThread.start();
            }
        }
    }

    /**
     * 发送单聊消息
     */
    public static void sendMsg2Single(String oppositeuserName, String msg){
        if(socketThread!=null){
            socketThread.sendMsg2Single(oppositeuserName,msg);
        }
    }
    /**
     * 发送单聊消息
     */
    public static void sendMsg2All(String msg){
        if(socketThread!=null){
            socketThread.sendMsg2All(msg);
        }
    }

    /**
     * 是否连接
     * @return
     */
    public static boolean isConnected(){
        if(socketThread!=null){
            return socketThread.isConnected();
        }else {
            return false;
        }
    }

    /**
     * 获取当前用户的用户名
     * @return
     */
    public static String getCurrentUserName(){
        if(socketThread!=null){
            return socketThread.getCurrentUserName();
        }else {
            return "";
        }
    }
}
