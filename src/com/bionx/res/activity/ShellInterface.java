package com.bionx.res.activity;

import android.util.Log;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ShellInterface {
	
  private static final String TAG = "ShellInterface";

  private static String shell;

  // uid=0(root) gid=0(root)
  private static final Pattern UID_PATTERN = Pattern.compile("^uid=(\\d+).*?");
  
  //CPU String Directories
  private final static String cpufreq_sys_dir = "/sys/devices/system/cpu/cpu0/cpufreq/";
  private final static String scaling_governor = cpufreq_sys_dir
			+ "scaling_governor";
  private final static String scaling_max_freq = cpufreq_sys_dir
			+ "scaling_max_freq";
  private final static String scaling_min_freq = cpufreq_sys_dir
			+ "scaling_min_freq";

  enum OUTPUT {
    STDOUT,
    STDERR,
    BOTH
  }

  private static final String EXIT = "exit\n";

  private static final String[] SU_COMMANDS = new String[]{
    "su",
    "/system/xbin/su",
    "/system/bin/su"
  };

  private static final String[] TEST_COMMANDS = new String[]{
    "id",
    "/system/xbin/id",
    "/system/bin/id"
  };

  public static synchronized boolean isSuAvailable() {
    if (shell == null) {
      checkSu();
    }
    return shell != null;
  }

  public static synchronized void setShell(String shell) {
    ShellInterface.shell = shell;
  }

  private static boolean checkSu() {
    for (String command : SU_COMMANDS) {
      shell = command;
      if (isRootUid()) return true;
    }
    shell = null;
    return false;
  }

  private static boolean isRootUid() {
    String out = null;
    for (String command : TEST_COMMANDS) {
      out = getProcessOutput(command);
      if (out != null && out.length() > 0) break;
    }
    if (out == null || out.length() == 0) return false;
    Matcher matcher = UID_PATTERN.matcher(out);
    if (matcher.matches()) {
      if ("0".equals(matcher.group(1))) {
        return true;
      }
    }
    return false;
  }

  public static String getProcessOutput(String command) {
    try {
      return _runCommand(command, OUTPUT.STDERR);
    } catch (IOException ignored) {
      return null;
    }
  }

  public static boolean runCommand(String command) {
    try {
      _runCommand(command, OUTPUT.BOTH);
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  private static String _runCommand(String command, OUTPUT o) throws IOException {
    DataOutputStream os = null;
    Process process = null;
    try {
      process = Runtime.getRuntime().exec(shell);
      os = new DataOutputStream(process.getOutputStream());
      InputStreamHandler sh = sinkProcessOutput(process, o);
      os.writeBytes(command + '\n');
      os.flush();
      os.writeBytes(EXIT);
      os.flush();
      process.waitFor();
      if (sh != null) {
        String output = sh.getOutput();
        Log.d(TAG, command + " output: " + output);
        return output;
      } else {
        return null;
      }
    } catch (Exception e) {
      final String msg = e.getMessage();
      Log.e(TAG, "runCommand error: " + msg);
      throw new IOException(msg);
    } finally {
      try {
        if (os != null) {
          os.close();
        }
        if (process != null) {
          process.destroy();
        }
      } catch (Exception ignored) {}
    }
  }

  public static InputStreamHandler sinkProcessOutput(Process p, OUTPUT o) {
    InputStreamHandler output = null;
    switch (o) {
      case STDOUT:
        output = new InputStreamHandler(p.getErrorStream(), false);
        new InputStreamHandler(p.getInputStream(), true);
        break;
      case STDERR:
        output = new InputStreamHandler(p.getInputStream(), false);
        new InputStreamHandler(p.getErrorStream(), true);
        break;
      case BOTH:
        new InputStreamHandler(p.getInputStream(), true);
        new InputStreamHandler(p.getErrorStream(), true);
        break;
    }
    return output;
  }

  private static class InputStreamHandler extends Thread {
    private final InputStream stream;
    private final boolean     sink;
    StringBuffer output;

    public String getOutput() {
      return output.toString();
    }

    InputStreamHandler(InputStream stream, boolean sink) {
      this.sink = sink;
      this.stream = stream;
      start();
    }

    @Override
    public void run() {
      try {
        if (sink) {
          while (stream.read() != -1) {}
        } else {
          output = new StringBuffer();
          BufferedReader b = new BufferedReader(new InputStreamReader(stream));
          String s;
          while ((s = b.readLine()) != null) {
            output.append(s);
          }
        }
      } catch (IOException ignored) {}
    }
  }
  
  public static boolean hasSysfs() {
		String[] requiredFiles = { scaling_governor, scaling_max_freq,
				scaling_min_freq };
		for (String requiredFile : requiredFiles) {
			if (!(new File(requiredFile)).exists()) {
				return false;
			}
		}
		return true;
	}
  
  public static String getSUbinaryPath() {
		String s = "/system/bin/su";
		File f = new File(s);
		if (f.exists()) {
			return s;
		}
		s = "/system/xbin/su";
		f = new File(s);
		if (f.exists()) {
			return s;
		}
		return null;
	}

public static boolean isRooted() {
	return getSUbinaryPath() != null;
}
}
