package com.easy.lsy.agent.core.demo;

public class Target {
    public static String intercept(String name){
        return "Hello "+name+"!";
    }
    public static String intercept(int i){
        return Integer.toString(i);
    }
    public static String intercept(Object o){
        return o.toString();
    }
}
