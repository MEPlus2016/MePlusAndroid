package com.meplus.client.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.client.R;
import com.meplus.client.app.MPApplication;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavHeaderViewHolder {
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.content)
    TextView mContent;
    @Bind(R.id.text)
    TextView mText;

    public NavHeaderViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void updateHeader() {
       /* final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
        final AVOSRobot robot = MPApplication.getsInstance().getRobot();
        final String uuId = robot == null ? "" : robot.getUUId();
        mTitle.setText(String.format("  用户名:%1$s", user.getUsername()));
        mContent.setText(TextUtils.isEmpty(uuId) ? "  未绑定多我机器人" : String.format("  机器人:%1$s", uuId));
        mText.setText(String.format("  邮箱:%1$s", user.getEmail()));*/

        final AVOSUser user = AVOSUser.getCurrentUser(AVOSUser.class);
        AVQuery<AVOSUser> query = new AVQuery<AVOSUser>("_User");
        query.whereEqualTo(AVOSUser.KEY_ROBOT_UUID,user.getRobotUUId());
        query.findInBackground(new FindCallback<AVOSUser>() {
            @Override
            public void done(List<AVOSUser> list, AVException e) {
                if(e == null){
                    String uuId = user.getRobotUUId();
                    mContent.setText(TextUtils.isEmpty(uuId) ? "  未绑定多我机器人" : String.format("  机器人:%1$s", uuId));
                }
            }
        });
        mTitle.setText(String.format("  用户名:%1$s", user.getUsername()));
        mText.setText(String.format("  邮箱:%1$s", user.getEmail()));
    }
}