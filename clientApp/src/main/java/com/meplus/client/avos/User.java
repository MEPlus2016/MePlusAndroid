package com.meplus.client.avos;

import com.avos.avoscloud.AVException;
import com.meplus.avos.objects.AVOSRobot;
import com.meplus.avos.objects.AVOSUser;
import com.meplus.events.ErrorEvent;
import com.meplus.events.EventUtils;
import com.meplus.events.QueryEvent;
import com.meplus.events.SaveEvent;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.schedulers.Schedulers;

public class User {
    @DebugLog
    public static void addRobot(AVOSUser avosUser, AVOSRobot avosRobot) {
        Observable.just(avosUser)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        user -> {
                            try {
                                user.addRobot(avosRobot);
                                EventUtils.postEvent(new SaveEvent<>(avosRobot));
                            } catch (AVException e) {
                                EventUtils.postEvent(new ErrorEvent(e));
                            }
                        },
                        throwable -> EventUtils.postEvent(new ErrorEvent(throwable))
                );
    }

    @DebugLog
    public static void queryRobot(AVOSUser avosUser) {
        Observable.just(avosUser)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(user -> {
                            try {
                                final String robotUUId = avosUser.getRobotUUId();
                                final List<AVOSRobot> list = user.queryRobotByUUID(robotUUId);
                                EventUtils.postEvent(new QueryEvent<>(list));
                            } catch (AVException e) {
                                EventUtils.postEvent(new ErrorEvent(e));
                            }

                        },
                        throwable -> EventUtils.postEvent(new ErrorEvent(throwable))
                );
    }

}
