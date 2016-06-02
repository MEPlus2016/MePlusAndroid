package com.meplus.client.avos;

import com.avos.avoscloud.AVException;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.schedulers.Schedulers;

public class Robot {
    @DebugLog
    public static void queryByUUID(String uuId) {
        Observable.just(uuId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(id -> {
                            try {
                                List<AVOSRobot> list = AVOSRobot.queryRobotByUUID(id);
                                EventUtils.postEvent(new QueryEvent<>(list));
                            } catch (AVException e) {
                                EventUtils.postEvent(new ErrorEvent(e));
                            }
                        },
                        throwable -> EventUtils.postEvent(new ErrorEvent(throwable)));
    }


}