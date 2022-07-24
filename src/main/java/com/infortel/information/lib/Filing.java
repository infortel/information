/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.lib;

/**
 *
 * @author leon
 */
public class Filing {
    //**************************************************************************
    private static Filing self;
    public Users users;
    public Setup setup;
    //**************************************************************************
    public Filing() {
        users=new Users(null);  
        setup=new Setup();
        self=this;
    }
    //**************************************************************************
    public static Filing get() {
        if (self==null) {
            self=new Filing();
        }
        return self;
    }
    //**************************************************************************
}
