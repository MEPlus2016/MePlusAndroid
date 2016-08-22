package io.agora.sample.agora;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meplus.utils.UUIDUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.sample.agora.EventUtils.TimeEvent;
import io.agora.sample.agora.Model.Record;

/**
 * Created by apple on 15/9/24.
 */
public class RecordActivity extends BaseEngineHandlerActivity {
    private ListView mListView;
    private TextView overallTime;
    private int time;
    private List<Record> records = new ArrayList<Record>();
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstance);
        setContentView(R.layout.agora_activity_record);

        //EventBus.getDefault().register(this);
        sp = getSharedPreferences("time", Activity.MODE_PRIVATE);

        ((AgoraApplication) getApplication()).initRecordsList();
        records.clear();
        records.addAll(((AgoraApplication) getApplication()).getRecordsList());
        initViews();
        setupListView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        records.clear();
        //EventBus.getDefault().unregister(this);
    }

    public void clear(View view) {
        ((AgoraApplication)getApplication()).call.edit().clear();
        mListView.setAdapter(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUserInteraction(View view) {
        int id = view.getId();
        if (id == R.id.record_back) {
            finish();
        } else if (id == R.id.record_overall) {
            finish();
        } else {
            super.onUserInteraction(view);
        }
    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.record_listView);
        findViewById(R.id.record_back).setOnClickListener(getViewClickListener());
        RelativeLayout overallButton = (RelativeLayout) findViewById(R.id.record_overall);
        overallButton.setOnClickListener(getViewClickListener());

        if (((AgoraApplication) getApplication()).getIsInChannel()) {
            overallButton.setVisibility(View.VISIBLE);
            time = ((AgoraApplication) getApplication()).getChannelTime();
            overallTime = ((TextView) overallButton.findViewById(R.id.overall_time));
            setupTime();
        } else {
            overallButton.setVisibility(View.GONE);
        }
    }

    private void setupTime() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        if (time >= 3600) {
                            overallTime.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                        } else {
                            overallTime.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }

    private void setupListView() {
        RecordAdapter adapter = new RecordAdapter(this, records);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }

    private class RecordAdapter extends BaseAdapter {
        private Context context;
        private List<Record> items = new ArrayList<Record>();

        public RecordAdapter(Context context, List<Record> items) {
            super();
            this.context = context;
            //先清空再加载
            this.items.clear();
            this.items.addAll(items);
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public Object getItem(int position) {
            return this.items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //读取sp
            SharedPreferences sharedPreferences= getSharedPreferences("sp", Activity.MODE_PRIVATE);
            String strTime = sharedPreferences.getString("time","");
            Log.i("str",strTime+"WWWW");//strTime为null
            long mTime = Long.parseLong(strTime);

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.agora_activity_record_item, null);
                convertView.setTag(holder);

                holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.record_item_layout);
                holder.itemDate = (TextView) convertView.findViewById(R.id.record_item_date);
                holder.itemTime = (TextView) convertView.findViewById(R.id.record_item_time);
                holder.userUUID = (TextView) convertView.findViewById(R.id.userID);
                holder.durTime = (TextView) convertView.findViewById(R.id.dur_time);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itemDate.setText(records.get(position).getRecordValue().substring(0, records.get(position).getRecordValue().indexOf("/")));
//            holder.itemTime.setText(records.get(position).getRecordValue().substring(records.get(position).getRecordValue().indexOf("/") + 1, records.get(position).getRecordValue().indexOf("+") - 1));
            holder.itemTime.setText(records.get(position).getRecordValue().substring(records.get(position).getRecordValue().indexOf("/") + 1));
            holder.userUUID.setText(UUIDUtils.getUUID(RecordActivity.this));

            long mTime0 = Long.parseLong(records.get(position).getTotalTime(mTime));
            if (mTime0 >= 3600000) {
                holder.durTime.setText(String.format("%d:%02d:%02d", mTime0 / 3600000, (mTime0 % 3600000) / 60000, ((mTime0 % 60000))/1000));
            } else {
                holder.durTime.setText(String.format("%02d:%02d", (mTime0 % 3600000) / 60000, ((mTime0 % 60000)/1000)));
            }
            final int number = position;
            // 不做任何跳转
//
//            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent toWebView = new Intent(RecordActivity.this, WebActivity.class);
//                    toWebView.putExtra(WebActivity.EXTRA_KEY_URL, records.get(number).getRecordValue().substring(records.get(number).getRecordValue().indexOf("+") + 1));
//                    startActivity(toWebView);
//                }
//            });
            return convertView;
        }
    }

    private class ViewHolder {
        private RelativeLayout itemLayout;
        private TextView itemDate;
        private TextView itemTime;
        private TextView userUUID;
        private TextView durTime;
    }

}
