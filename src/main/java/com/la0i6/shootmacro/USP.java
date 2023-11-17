package com.la0i6.purgatorymacro;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

/****
 *** author：lao
 *** package：com.la0i6.purgatorymacro
 *** project：PurgatoryMacro
 *** name：Macro
 *** date：2023/11/16  23:13
 *** filename：Macro
 *** desc：
 ***/

public class USP extends Thread{

    private Robot robot;

    private int left1 = 30;
    private int left2 = 60;

    private int right1 = 40;
    private int right2 = 70;

    public void setParameters(int left1, int left2, int right1, int right2) {
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
    }

    @Override
    public void run() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-------------------usp--------------------");
        while (true){
            Random random = new Random();
            int left = random.nextInt(left2 - left1) + left1;
            int right = random.nextInt(right2 - right1) + right1;
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(left);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(right);
        }
    }
}
