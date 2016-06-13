package com.meplus.robot;

public class FireflyGPIO {

	static {
		System.loadLibrary("firefly_gpio_jni");
	}

	public native static int open(String device);
	public native static int close(int fd);
	public native static int ioctl(int fd, int cmd);
}
