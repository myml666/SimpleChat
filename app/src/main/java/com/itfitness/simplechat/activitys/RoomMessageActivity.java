package com.itfitness.simplechat.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itfitness.simplechat.R;
import com.itfitness.simplechat.beans.MessageBean;
import com.itfitness.simplechat.manager.SocketManager;
import com.itfitness.simplechat.socket.SocketThread;
import com.itfitness.simplechat.widget.AvatarView;

import java.util.ArrayList;

public class RoomMessageActivity extends BaseActivity {
    private RecyclerView mRvMsgList;
    private EditText mEtMsg;
    private Button mBtSend;
    private ArrayList<MessageBean> mMessageList;
    private SocketThread.SocketThreadListener mSocketThreadListener;
    private BaseQuickAdapter<MessageBean, BaseViewHolder> mBaseQuickAdapterMsg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlemessage);
        initView();
        initListener();
        initAdapter();
    }

    private void initAdapter() {
        mRvMsgList.setLayoutManager(new LinearLayoutManager(this));
        mMessageList = new ArrayList<>();
        mBaseQuickAdapterMsg = new BaseQuickAdapter<MessageBean, BaseViewHolder>(R.layout.item_message,mMessageList) {
            @Override
            protected void convert(BaseViewHolder helper, MessageBean item) {
                LinearLayout mineLayout = helper.getView(R.id.item_message_mine);
                LinearLayout oppositeLayout = helper.getView(R.id.item_message_opposite);
                if(item.isMineMsg()){
//                    我发的消息
                    mineLayout.setVisibility(View.VISIBLE);
                    oppositeLayout.setVisibility(View.GONE);
                    AvatarView avatarView = helper.getView(R.id.item_message_av_mine);
                    avatarView.setAvtarText(SocketManager.getCurrentUserName());
                    helper.setText(R.id.item_message_tv_mine,item.getMsg());
                }else {
//                    其他人发的消息
                    mineLayout.setVisibility(View.GONE);
                    oppositeLayout.setVisibility(View.VISIBLE);
                    AvatarView oppositeView = helper.getView(R.id.item_message_av_opposite);
                    oppositeView.setAvtarText(item.getOppositeUserName());
                    helper.setText(R.id.item_message_tv_opposite,item.getMsg());
                }
            }
        };
        mRvMsgList.setAdapter(mBaseQuickAdapterMsg);
    }

    private void initView() {
        mRvMsgList = findViewById(R.id.activity_singlemessage_rv);
        mEtMsg = findViewById(R.id.activity_singlemessage_et);
        mBtSend = findViewById(R.id.activity_singlemessage_bt);
    }

    private void initListener() {
        //设置Socket监听事件
        mSocketThreadListener = new SocketThread.SocketThreadListener() {
            @Override
            public void onReceivedSingleMsg(String oppositeUserName,String msg) {

            }

            @Override
            public void onUpdateConnectList(String[] msgbodyarr) {
                //聊天的时候不监听联系人列表更新情况
            }

            @Override
            public void onReceivedRoomMsg(String oppositeUserName, String msg) {
                //聊天室
                if (!oppositeUserName.equals(SocketManager.getCurrentUserName())) {
                    //如果发消息的人不是自己才将消息加入消息列表中
                    addMsg2MsgList(new MessageBean(false,msg,oppositeUserName));
                }
            }
        };
        mBtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getText(mEtMsg);
                if(TextUtils.isEmpty(msg)){
                    Toast.makeText(RoomMessageActivity.this, "发送的消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                SocketManager.sendMsg2All(msg);
                addMsg2MsgList(new MessageBean(true,msg));
                mEtMsg.setText("");
            }
        });
    }

    /**
     * 在消息列表中加入消息
     */
    private void addMsg2MsgList(MessageBean msg){
        mBaseQuickAdapterMsg.addData(msg);
        mBaseQuickAdapterMsg.notifyDataSetChanged();
        mRvMsgList.smoothScrollToPosition(mBaseQuickAdapterMsg.getData().size()-1);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //切换Socket监听事件
        SocketManager.setSocketListener(mSocketThreadListener);
    }
}
