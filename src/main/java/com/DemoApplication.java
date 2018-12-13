package com;

public class DemoApplication {

    public static void main(String[] args) {

        try {
            Thread.sleep(10000000000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
