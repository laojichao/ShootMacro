package com.la0i6.shootmacro;

import com.alibaba.fastjson.JSON;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.sun.jna.platform.win32.WinDef;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class MacroApplication extends Application implements NativeMouseListener {

    private long activeDate = 1702544394471L;

    private Robot robot;
    //总开关
    private boolean enable = false;
    private boolean timeEnable = true;
    private Label label_status;
    private CheckBox cb_shoot;

    private USP uspThread = null;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MacroApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setAlwaysOnTop(true);
        stage.setTitle("CF瞬狙宏");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        initUI(scene);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    check();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000 * 5, 1000 * 60 * 10); // 延迟delay毫秒后，执行第一次task，然后每隔period毫秒执行一次task。
    }


    private void check() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp")
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String json = response.body().string();
            ServiceTime serviceTime = JSON.parseObject(json, ServiceTime.class);
            System.out.println(serviceTime.toString());
            long cur = Long.parseLong(serviceTime.getData().getT());
            System.out.println(cur);
            if (cur > activeDate) {
                timeEnable = false;
                enable = false;
                System.out.println("版本过期");
                //主线程更新UI
                updateStatus("版本过期，请添加qq909429920");
//                label_status.setText("版本过期，请添加qq909429920");
            }
        }
    }

    private void initUI(Scene scene)
    {
        Font font = Font.font("Arial", FontWeight.BOLD, 12);
        Label label_one = (Label)scene.lookup("#label_one");
        label_one.setFont(font);
        label_one.setText("鼠标滚轮按下开启或关闭");

        label_status = (Label)scene.lookup("#label_two");
        label_status.setFont(font);
        label_status.setTextFill(Color.RED);
        label_status.setText("运行状态：已关闭");

        Label label_author = (Label)scene.lookup("#label_three");
        label_author.setFont(font);
        label_author.setText("鼠标侧键分别为瞬狙和USP，右键释放架狙");

        Label label_four = (Label)scene.lookup("#label_four");
        label_four.setFont(font);
        label_four.setText("瞬狙和架狙有冲突，勾选瞬狙就不生效");
        cb_shoot = (CheckBox)scene.lookup("#cb_shoot");
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        robot = new Robot();
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        //鼠标点击监听器
        GlobalScreen.addNativeMouseListener(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException ex) {
            throw new RuntimeException(ex);
        }
        System.runFinalization();
        System.exit(0);
        robot = null;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        //侧键
        if (enable && getWindow()) {
            if (e.getButton() == NativeMouseEvent.BUTTON4 && uspThread == null) {
                uspThread = new USP();
                uspThread.start();
            } else if (e.getButton() == NativeMouseEvent.BUTTON5 && !cb_shoot.isSelected()) {
                //瞬狙和假狙有冲突
                snapshotMacro();
            }
        }
        //滚轮
        if (e.getButton() == NativeMouseEvent.BUTTON3 && timeEnable) {
            enable = !enable;
            if (enable) {
                System.out.println("开关已经打开");
                updateStatus("运行状态：已开启");

            } else  {
                System.out.println("开关已经关闭");
                updateStatus("运行状态：已关闭");
            }
        }
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        //右键释放
        if (e.getButton() == NativeMouseEvent.BUTTON2 && enable && cb_shoot.isSelected() && getWindow()) {
            shotMacro();
        } else if (e.getButton() == NativeMouseEvent.BUTTON4) {
            //侧键释放
            if (uspThread!= null && uspThread.isAlive()) {
                uspThread.stopMacro();
                uspThread = null;
            }
        }
    }


    private boolean getWindow() {
        // 寻找游戏窗口
        WinDef.HWND gameWindow = User32Extended.INSTANCE.FindWindow(null, "穿越火线");
        // 获取窗口位置和大小
//        int[] rect = new int[4];
//        User32Extended.INSTANCE.GetWindowRect(gameWindow, rect);
//        int x = rect[0];
//        int y = rect[1];
//        int width = rect[2] - x;
//        int height = rect[3] - y;
//        System.out.println("窗口位置：(" + x + ", " + y + ")");
//        System.out.println("窗口大小：" + width + "x" + height);
        if (gameWindow == null) {
            return false;
        }
        return true;
    }


    private void updateStatus(String text)
    {
        Platform.runLater(()->{
            label_status.setText(text);
        });
    }


    private void snapshotMacro() {
        System.out.println("-------------------瞬狙--------------------");
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        robot.delay(10);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_3);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_3);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_1);
    }

    private void shotMacro() {
        System.out.println("-------------------架狙--------------------");
//        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
//        robot.delay(10);
//        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
//        robot.delay(10);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_3);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_3);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyPress(KeyEvent.VK_1);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_1);
    }

}