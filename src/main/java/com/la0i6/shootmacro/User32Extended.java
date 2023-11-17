package com.la0i6.shootmacro;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.win32.W32APIOptions;

/****
 *** author：lao
 *** package：com.la0i6.shootmacro
 *** project：ShootMacro
 *** name：User32Extended
 *** date：2023/11/17  16:08
 *** filename：User32Extended
 *** desc：JNA调用User32.dll中的函数
 ***/

public interface User32Extended extends User32 {
    User32Extended INSTANCE = Native.load("user32", User32Extended.class, W32APIOptions.DEFAULT_OPTIONS);
    HWND FindWindow(String lpClassName, String lpWindowName);
    int GetWindowRect(HWND handle, int[] rect);
    int GetWindowLong(HWND hWnd, int nIndex);
}
