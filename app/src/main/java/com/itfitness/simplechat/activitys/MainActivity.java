package com.itfitness.simplechat.activitys;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hss01248.notifyutil.NotifyUtil;
import com.itfitness.simplechat.R;
import com.itfitness.simplechat.manager.SocketManager;
import com.itfitness.simplechat.socket.SocketThread;
import com.itfitness.simplechat.widget.AvatarView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<String, BaseViewHolder> mBaseQuickAdapter;
    private ArrayList<String> mContactList;
    private SocketThread.SocketThreadListener mSocketThreadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotifyUtil.init(getApplicationContext());
        initView();
        initSocket();
    }

    private void initSocket() {
        mSocketThreadListener = new SocketThread.SocketThreadListener() {
            @Override
            public void onReceivedSingleMsg(String oppositeUserName, String msg) {
                Intent intent = new Intent(MainActivity.this, SingleMessageActivity.class);
                intent.putExtra("OppositeUserName",oppositeUserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,
                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotifyUtil.buildMailBox(104,R.mipmap.ic_launcher,oppositeUserName)
                        .addMsg(msg)
                        .setContentIntent(pIntent)
                        .setBigIcon(R.mipmap.ic_launcher)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .show();
            }

            @Override
            public void onUpdateConnectList(String[] msgbodyarr) {
                upDateConnectList(msgbodyarr);
            }

            @Override
            public void onReceivedRoomMsg(String oppositeUserName, String msg) {
                Intent intent = new Intent(MainActivity.this, RoomMessageActivity.class);
                intent.putExtra("OppositeUserName",oppositeUserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this,
                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotifyUtil.buildMailBox(104,R.mipmap.ic_launcher,oppositeUserName)
                        .addMsg(msg)
                        .setContentIntent(pIntent)
                        .setBigIcon(R.mipmap.ic_launcher)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .show();
            }
        };
        SocketManager.init(getIntent().getBundleExtra("SocketInit"));
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.activity_main_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContactList = new ArrayList<>();
        mContactList.add("聊天室");
        mBaseQuickAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_contactlist, mContactList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                AvatarView avatarView = helper.getView(R.id.item_contactlist_av);
                avatarView.setAvtarText(item);
                helper.setText(R.id.item_contactlist_tv_name, item);
            }
        };
        mBaseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position!=0){
                    Intent intent = new Intent(MainActivity.this, SingleMessageActivity.class);
                    intent.putExtra("OppositeUserName", mBaseQuickAdapter.getData().get(position));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, RoomMessageActivity.class);
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mBaseQuickAdapter);
    }

    /**
     * 更新联系人列表
     *
     * @param msgBody
     */
    private void upDateConnectList(String[] msgBody) {
        ArrayList<String> connectList = new ArrayList<>();
        connectList.add("聊天室");
        if (msgBody != null && msgBody.length > 1) {
            for (int i = 1; i < msgBody.length; i++) {
                connectList.add(msgBody[i]);
            }
            mBaseQuickAdapter.setNewData(connectList);
            mBaseQuickAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //换到当前Activity的监听事件
        SocketManager.setSocketListener(mSocketThreadListener);
    }
}
