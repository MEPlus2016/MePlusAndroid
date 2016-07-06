/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meplus.robot.activity;

import android.os.Bundle;

import com.meplus.avos.objects.AVOSRobot;
import com.meplus.robot.app.MPApplication;

import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * 蓝牙设备列表
 */
public class DeviceListActivity extends DeviceList {

    final AVOSRobot robot = MPApplication.getsInstance().getRobot();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        robot.setRobotOnline(false);
    }
}
