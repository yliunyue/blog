package com.utils;

import com.entity.Admin;


public class AdminThreadLocal {
    private AdminThreadLocal(){}

    private static final ThreadLocal<Admin> LOCAL=new ThreadLocal<>();

    public static void put(Admin admin){
        LOCAL.set(admin);
    }
    public static Admin get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }


}
