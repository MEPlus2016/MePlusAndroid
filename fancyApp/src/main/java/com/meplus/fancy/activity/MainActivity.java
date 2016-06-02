package com.meplus.fancy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meplus.fancy.Constants;
import com.meplus.fancy.R;
import com.meplus.fancy.events.BookEvent;
import com.meplus.fancy.events.ErrorEvent;
import com.meplus.fancy.events.LogEvent;
import com.meplus.fancy.events.ResponseEvent;
import com.meplus.fancy.events.ScannerEvent;
import com.meplus.fancy.events.SerialEvent;
import com.meplus.fancy.model.Response;
import com.meplus.fancy.model.entity.Book;
import com.meplus.fancy.model.entity.Code;
import com.meplus.fancy.presenters.ApiPresenter;
import com.meplus.fancy.utils.FIRUtils;
import com.meplus.fancy.utils.FileLog;
import com.meplus.fancy.utils.IntentUtils;
import com.meplus.fancy.utils.JsonUtils;
import com.topeet.serialtest.presenters.SerialPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.trinea.android.common.util.ToastUtils;

public class MainActivity extends BaseActivity {
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final String LibraryId = "41676";

    @Bind(R.id.data_edit)
    EditText mDataEdit;
    @Bind(R.id.library_edit)
    EditText mLibraryEdit;
    @Bind(R.id.user_edit)
    EditText mUserEdit;
    @Bind(R.id.log_text)
    TextView mLogText;

    private SerialPresenter mSerialPresenter = new SerialPresenter();
    private ApiPresenter mApiPresenter = new ApiPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileLog.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        FIRUtils.checkForUpdateInFIR(this);

        mSerialPresenter.start();

        mLibraryEdit.setText(LibraryId);

        final String Data = "{\"babyId\":\"0\",\"check\":\"3D0A6F20FFF74DF28E1D226A3B6C7E82\",\"parentsUserId\":\"0\",\"time\":\"0\"}";
        mDataEdit.setText(Data);
        mUserEdit.setText(getCheck(Data));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileLog.i(TAG, "onDestroy");

        mSerialPresenter.destroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogEvent(LogEvent event) {
        final String text = event.getText();
        mLogText.append(String.format("data: %1$s \r\n", text));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScannerEvent(ScannerEvent event) {
        FileLog.i(TAG, "onScannerEvent");
        final String data = event.getContent();
        mLogText.append(String.format("data: %1$s \r\n", data));
        if (data.startsWith("{") && data.endsWith("}")) { // JSON 格式
            final Code code = JsonUtils.readValue(data, Code.class);
            mDataEdit.setText(data);
            mUserEdit.setText(code.getCheck());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseEventBook(ResponseEvent<Book> event) {
        FileLog.i(TAG, "onResponseEventBook");

        final String method = event.getMethod();
        if (ApiPresenter.METHOD_BORROWBYROBOT.equals(method)) {
            final Response<Book> response = event.getResponse();
            if (response.isOk() && response.getResult().getResultNo() == 0) {//  0 代表成功 1 代表未预借 2 借出失败
                final int code = mSerialPresenter.demagnetize();
                if (code == 0) {
                    ToastUtils.show(this, "消磁成功！");
                }
                EventBus.getDefault().post(new SerialEvent(SerialEvent.TYPE_DEMAGNETIZE));
                mLogText.append(String.format("demagnetize code: %1$d \r\n", code));
            } else {
                response.showMessage(this);
            }
        } else if (ApiPresenter.METHOD_RETURNBYROBOT.equals(method)) {
            final Response<Book> response = event.getResponse();
            if (response.isOk() && response.getResult().getResultNo() == 0) {//  0 代表成功 1 代表未借阅 2 归还失败
                final int code = mSerialPresenter.magnetize();
                if (code == 0) {
                    ToastUtils.show(this, "充磁成功！");
                }
                EventBus.getDefault().post(new SerialEvent(SerialEvent.TYPE_MAGNETIZE));
                mLogText.append(String.format("magnetize code: %1$d \r\n", code));
            } else {
                response.showMessage(this);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        FileLog.i(TAG, "onErrorEvent");

        final String method = event.getMethod();
        if (ApiPresenter.METHOD_BORROWBYROBOT.equals(method)) {
            event.showError(this);
        } else if (ApiPresenter.METHOD_RETURNBYROBOT.equals(method)) {
            event.showError(this);
        } else if (SerialPresenter.METHOD_RUN.equals(method)) {
            event.showError(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBookEvent(BookEvent event) {
        final String action = event.getAction();
        final String ISBN = event.getISBN();
        final String UserId = mUserEdit.getText().toString();
        FileLog.i(TAG, "onBookEvent " + action + " " + ISBN + " " + UserId);

        if (BookEvent.ACTION_BORROW.equals(action)) {
            mApiPresenter.borrowbyrobot(ApiPresenter.METHOD_BORROWBYROBOT, UserId, ISBN, LibraryId);
        } else if (BookEvent.ACTION_RETURN.equals(action)) {
            mApiPresenter.returnbyrobot(ApiPresenter.METHOD_RETURNBYROBOT, UserId, ISBN, LibraryId);
        }
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6})
    public void onClick(View view) {
        final String Data = mDataEdit.getText().toString();
        final String LibraryId = mLibraryEdit.getText().toString();
        final String UserId = mUserEdit.getText().toString();

        Intent intent;
        switch (view.getId()) {
            case R.id.button1: // 用户信息
                intent = IntentUtils.generateIntent(this, UserActivity.class);
                intent.putExtra("LibraryId", LibraryId);
                intent.putExtra("Data", Data);
                startActivity(intent);
                break;

            case R.id.button2: // 借书
                intent = IntentUtils.generateIntent(this, BorrowBooksActivity.class);
                intent.putExtra("LibraryId", LibraryId);
                intent.putExtra("Data", Data);
                startActivity(intent);
                break;

            case R.id.button3: // 还书
                intent = IntentUtils.generateIntent(this, BorrowedBooksActivity.class);
                intent.putExtra("LibraryId", LibraryId);
                intent.putExtra("Data", UserId);
                startActivity(intent);
                break;

            case R.id.button4: // 扫描二维码
                intent = IntentUtils.generateIntent(this, CaptureActivity.class);
                startActivity(intent);
                break;

            case R.id.button5: // 启动多我程序
                intent = IntentUtils.generateIntent(this, Constants.MEPLUS_ROBOT_PACKAGENAME);
                if (intent == null) {
                    ToastUtils.show(this, "请先安装多我机器人程序！");
                } else {
                    startActivity(intent);
                }
                break;

            case R.id.button6: // 清除日志
                mLogText.setText("");
                break;

            default:
                break;
        }
    }

    private String getCheck(String data) {
        return JsonUtils.readValue(data, Code.class).getCheck();
    }

    static {
        System.loadLibrary("serialtest");
    }
}
