package com.meplus.client.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.marvinlabs.intents.MediaIntents;
import com.meplus.activity.BaseActivity;
import com.meplus.avos.Constants;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;
import com.meplus.client.utils.UUIDUtils;
import com.meplus.fir.FirVersion;
import com.meplus.punub.ErrorEvent;
import com.meplus.punub.PubnubPresenter;
import com.meplus.client.utils.IntentUtils;
import com.meplus.client.viewholder.NavHeaderViewHolder;
import com.meplus.events.EventUtils;
import com.meplus.events.LogoutEvent;
import com.meplus.events.SaveEvent;
import com.meplus.presenters.AgoraPresenter;
import com.meplus.punub.Command;
import com.meplus.punub.CommandEvent;
import com.meplus.punub.StateEvent;
import com.meplus.utils.JsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.PackageUtils;
import cn.trinea.android.common.util.ToastUtils;
import hugo.weaving.DebugLog;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import io.agora.sample.agora.AgoraApplication;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.fab)
    ImageView fab;
    private NavHeaderViewHolder mHeaderHolder;

    private PubnubPresenter mPubnubPresenter = new PubnubPresenter();
    private AgoraPresenter mAgoraPresenter = new AgoraPresenter();//声明agora
    private String mChannel;
    private int mUserId;

    //add
    boolean flag;
    boolean isOnline;

    //add 自动更新
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        EventUtils.register(this);

        context = this;

        // 初始化
        final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        mUserId = user.getUserId();                                  // agora 中的用户名
        final String uuId = user.getUUId();                             // pubnub 中的用户名
        mChannel = robot == null ? "" : robot.getUUId();            // pubnub 中的channel

        mAgoraPresenter.initAgora((AgoraApplication) getApplication(), String.valueOf(mUserId));

        mPubnubPresenter.initPubnub(uuId);
        if (!TextUtils.isEmpty(mChannel)) {
            mPubnubPresenter.subscribe(getApplicationContext(), mChannel);
        }
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        //add----------------标题透明
        mToolbar.setTitleTextColor(getResources().getColor(R.color.alpha));

        mToolbar.setNavigationIcon(R.drawable.menu);

//        getSupportActionBar().setTitle("");
//        mToolbar.setNavigationIcon(R.drawable.menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();//注释掉,则会隐藏首页的toobarIcon


        mNavigationView.setNavigationItemSelectedListener(this);

        final View headerView = mNavigationView.getHeaderView(0);
        mHeaderHolder = new NavHeaderViewHolder(headerView);
        mHeaderHolder.updateHeader();

        //add bind or wake
        if(robot == null){
            fab.setBackgroundResource(R.drawable.bind);
        }else{
            fab.setBackgroundResource(R.drawable.wake);
        }

        //测试userUUID
        Log.i("userUUID",UUIDUtils.getUUID(this)+"============");
        //版本更新时自动弹出更新对话框,没有反应？
        final int versionCode = PackageUtils.getAppVersionCode(context);//手机版本
        Log.i("ver",versionCode+"##");
        FIR.checkForUpdateInFIR(Constants.FIR_TOKEN, new VersionCheckCallback() {
            @Override
            public void onSuccess(String s) {
//                super.onSuccess(s);
                final FirVersion version = JsonUtils.readValue(s, FirVersion.class);
                Log.i("ver",version.getVersion()+"**");
                if(versionCode<version.getVersion()){
                    //弹出对话框(系统对话框)
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("软件更新");
                    builder.setMessage("检测到新版本，立即更新吗？");

                    //确定按钮
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            context.startActivity(MediaIntents.newOpenWebBrowserIntent(version.getUpdate_url()));
                        }
                    });
                    //取消按钮
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    //show,不能忘
                    builder.show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
        String uuid = user.getRobotUUId();
        //add bind or wake
        if(uuid == null){
            fab.setBackgroundResource(R.drawable.bind);
        }else{
            fab.setBackgroundResource(R.drawable.wake);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPubnubPresenter.destroy();
        EventUtils.unregister(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_history:
                startActivity(com.meplus.activity.IntentUtils.generateRecordIntent(this));
                break;
            case R.id.nav_settings:
                startActivity(IntentUtils.generateIntent(this, SettingsActivity.class));
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event) {
        if (event.ok()) {
            startActivity(IntentUtils.generateIntent(this, LoginActivity.class));
            finish();
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveEvent(SaveEvent<AVOSRobot> event) {
        if (event.ok()) {
            final AVOSRobot robot = event.getData();
            MPApplication.getsInstance().setRobot(robot);
            mChannel = robot.getUUId();                 // 订阅机器人
            mPubnubPresenter.subscribe(getApplicationContext(), mChannel);

            mHeaderHolder.updateHeader();
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommandEvent(CommandEvent event) {
        if (event.ok()) {
            final Command command = event.getCommand();
            final String message = command.getMessage();

            switch (message) {
                case Command.ACTION_LEFT:
                case Command.ACTION_UP:
                case Command.ACTION_RIGHT:
                case Command.ACTION_DOWN:
                case Command.ZERO:
                case Command.ONE:
                case Command.TWO:
                case Command.THREE:
                case Command.FOUR:
                case Command.FIVE:
                case Command.SIX:
                case Command.SEVEN:
                case Command.NINE:
                case Command.ACTION_HOME:
                case Command.ACTION_STOP:
                    mPubnubPresenter.publish(getApplicationContext(), command.getMessage());
                    break;
                case Command.ACTION_CALL:
                    break;
                default:
                    break;
            }
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStateEvent(StateEvent event) {
        if (event.ok()) {
            final Command command = event.getCommand();
            final String message = command.getMessage();

            switch (message) {
                case Command.ACTION_LEFT:
                case Command.ACTION_UP:
                case Command.ACTION_RIGHT:
                case Command.ACTION_DOWN:
                case Command.ACTION_STOP:
                    // TODO
                    break;
                case Command.ACTION_CALL:
                    startActivity(IntentUtils.generateCallIntent(this, mChannel, mUserId));
                    break;
                default:
                    break;
            }
        }
    }

    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        if (event.ok()) {
            final String errorString = event.getErrorString();
            ToastUtils.show(this, errorString);
        }
    }


    @OnClick(R.id.fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
                String uuId = user.getRobotUUId();

                if(uuId == null){
                    startActivity(IntentUtils.generateIntent(this, BindRobotActivity.class));
                }else{
                    //唤醒前访问数据库,add 代码
                    final AVOSRobot robot = MPApplication.getsInstance().getRobot();
                    AVObject theTodo = AVObject.createWithoutData("Robot", robot.getObjectId());
                    String call = "call";// 指定刷新的 call 字符串
                    String online = "online";

                    theTodo.fetchInBackground(online, new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            isOnline = avObject.getBoolean("online");
                            flag = avObject.getBoolean("call");
                            if (isOnline == false & flag == true) {
                                com.meplus.client.utils.ToastUtils.toShowToast(MainActivity.this, "该机器人不在线！");
                            }
                            if (flag && isOnline) {
//                                if (flag) {
                                mPubnubPresenter.publish(getApplicationContext(), Command.ACTION_CALL);
                                //finish();
                            } else if (flag == false) {
                                com.meplus.client.utils.ToastUtils.toShowToast(MainActivity.this, "该机器人正在被连接，请稍后再试！");
                                //finish();
                            }
                        }
                    });
                }
               /* Snackbar.make(view, uuId == null ? "绑定多我机器人吗？" : "唤醒多我机器人吗？", Snackbar.LENGTH_LONG).setAction("确定", v -> {
                    if (uuId == null) {
                        startActivity(IntentUtils.generateIntent(this, BindRobotActivity.class));
                    } else {
                        ///////////////////////////////////////////////*//*//*
                        //唤醒前访问数据库,add 代码
                        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
                        AVObject theTodo = AVObject.createWithoutData("Robot", robot.getObjectId());
                        String call = "call";// 指定刷新的 call 字符串
                        String online = "online";

                        theTodo.fetchInBackground(online, new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                isOnline = avObject.getBoolean("online");
                                flag = avObject.getBoolean("call");
                                if (isOnline == false & flag == true) {
                                    com.meplus.client.utils.ToastUtils.toShowToast(MainActivity.this, "该机器人不在线！");
                                }
                                if (flag && isOnline) {
//                                if (flag) {
                                    mPubnubPresenter.publish(getApplicationContext(), Command.ACTION_CALL);
                                    //finish();
                                } else if (flag == false) {
                                    com.meplus.client.utils.ToastUtils.toShowToast(MainActivity.this, "该机器人正在被连接，请稍后再试！");
                                    //finish();
                                }
                            }
                        });
                    }
                }).show();*/
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
