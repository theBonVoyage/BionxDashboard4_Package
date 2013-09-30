package com.bionx.res.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Environment;

import com.bionx.res.background.RootUtils;

public class SystemHelper {
	/**
	 * Resource ID for the boolean value stored in framework-res for MIUI
	 */
	public static final int CONFIG_SHOW_NAVIGATION = 0x01110034;
	public static final String FILES_DIR = "/data/data/com.bionx.res/files";
	public static final String EXT_FILESDIR = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/us.miui.toolbox/files";

	public static boolean hasNavigationBar(Context context) {
		return context.getResources().getBoolean(CONFIG_SHOW_NAVIGATION);
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().smallestScreenWidthDp >= 600);
	}

	public static void restartApp(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningAppProcessInfo> apps = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo app : apps) {
			if (app.processName.equals(packageName)) {
				int pid = app.pid;
				try {
					RootUtils.execute("kill " + pid + "\n");
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void restartSystem() {
		try {
			String pid = RootUtils.executeWithResult("pidof system_server\n");
			if (pid != null && !pid.equals(""))
				RootUtils.execute(String.format("kill %s\n", pid));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void makeFileWorldReadable(String path) {
		try {
			RootUtils.execute("busybox chmod 0744 " + path + " \n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void removeFilesFromDir(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				if (c.isFile()) {
					c.delete();
				}
			}
		}
	}

	/**
	 * Remounts /system as RW
	 */
	public static void mountSystemRW() {
		try {
			RootUtils.execute("mount -o rw,remount /system\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remounts /system as RO
	 */
	public static void mountSystemRO() {
		try {
			RootUtils.execute("mount -o ro,remount /system\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean externalFolderExists() {
		File filesDir = new File(EXT_FILESDIR);
		return filesDir.exists();
	}

	public static void createExternalFolder() {
		if (externalFolderExists())
			return;
		File filesDir = new File(EXT_FILESDIR);
		filesDir.mkdirs();
	}

	public static void copyFileToExternalData(String path) {
		if (!externalFolderExists())
			createExternalFolder();
		String fileName = path.substring(path.lastIndexOf('/') + 1);
		String dest = EXT_FILESDIR + "/" + fileName;

		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(path));
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(dest));
			byte[] buffer = new byte[1024 * 1024];
			while (in.available() > 0) {
				int read = in.read(buffer);
				out.write(buffer, 0, read);
			}
			out.close();
			in.close();

			File f = new File(path);
			while (f.exists())
				f.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void restartSystemUI(Context context) {
		restartApp(context, "com.android.systemui");
	}
}
