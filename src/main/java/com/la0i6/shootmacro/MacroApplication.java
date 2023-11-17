package com.la0i6.shootmacro;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.Random;

public class HelloApplication extends Application implements NativeMouseListener {

    private long activeDate = 1702544394471L;

    private Robot robot;
    //总开关
    private boolean enable = false;
    private boolean purgatory = true;
    private boolean usp = true;
    Label label_status;

    @Override
    public void start(Stage stage) throws IOException, AWTException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));


        Background background = new Background(new BackgroundFill(Color.RED, null, null));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Font font = Font.font("Arial", FontWeight.BOLD, 12);
        Label label_one = (Label)scene.lookup("#label_one");
        label_one.setFont(font);
        label_one.setText("鼠标滚轮按下开或关\n侧键为炼狱和USP");


        label_status = (Label)scene.lookup("#label_two");
        label_status.setFont(font);
        label_status.setText("运行状态：已关闭");

        Label label_author = (Label)scene.lookup("#label_three");
        label_author.setFont(font);
        label_author.setTextFill(Color.RED);
        label_author.setText("作者qq909429920");


        Label label_four = (Label)scene.lookup("#label_four");
        label_four.setFont(font);
        label_four.setText("售后群");


        stage.setTitle("CF瞬狙宏");

        stage.setScene(scene);
        stage.setResizable(false);
        NativeMouseListener mouseListener = this;
        robot = new Robot();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        //鼠标点击监听器
        GlobalScreen.addNativeMouseListener(this);

        System.out.println("show");
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
        System.out.println("close");
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent e) {
        System.out.println("press");
        Thread thread = null;
        if (enable) {
            if (e.getButton() == NativeMouseEvent.BUTTON4) {
                usp = true;
                thread = new Thread(uspMacro);
                thread.start();
            } else if (e.getButton() == NativeMouseEvent.BUTTON5) {
                purgatory = true;
                thread = new Thread(purgatoryMacro);
                thread.start();
            }
        }

        if (e.getButton() == NativeMouseEvent.BUTTON3) {
            enable = !enable;
            if (enable) {
                System.out.println("开关已经打开");
                updateStatus("运行状态：已开启");

            } else  {
                System.out.println("开关已经关闭");
                updateStatus("运行状态：已关闭");

            }
            long currentTime = System.currentTimeMillis();
//            long active = System.currentTimeMillis() + (long)1000 * 60 * 60 * 24 *30;
            System.out.println(currentTime);
//            System.out.println(active);
//            activeDate = 1699952394471L;
            if ( currentTime > activeDate || currentTime < 1699952394471L)
            {
                enable = false;
                System.out.println("版本过期");
                updateStatus("版本过期，请重新获取新版本");
            }
        }
    }

    private void updateStatus(String text)
    {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                label_status.setText(text);
            }
        });
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent e) {
        System.out.println("release");
        if (e.getButton() == NativeMouseEvent.BUTTON4) {
            usp = false;
        } else if (e.getButton() == NativeMouseEvent.BUTTON5) {
            purgatory = false;
        }
    }



    //炼狱宏
    private Runnable purgatoryMacro = new Runnable() {
        @Override
        public void run() {
            while (purgatory) {
                System.out.println("-------------------炼狱--------------------");
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Random random = new Random();
                int left = random.nextInt(30) + 100;
                System.out.println(left);
                robot.delay(left);
                int right = random.nextInt(20) + 15;
                System.out.println(right);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                robot.delay(right);
            }
        }
    };

    //usp宏
    private Runnable uspMacro = new Runnable() {
        @Override
        public void run() {
            System.out.println("-------------------usp--------------------");
            while (usp) {
                Random random = new Random();
                int left = random.nextInt(20) + 25;
                int right = random.nextInt(20) + 25;
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.delay(left);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                robot.delay(right);
            }
        }
    };
}