/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.lib;

import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Application {
    private static boolean isInLinux;
    private static boolean initialized;
    private static String operating_system;
//******************************************************************************
    public static Application self=new Application();
//******************************************************************************
    public void Application() {
        initialize();
    }
//****************************************************************************** 
    public void initialize() {
        if (!initialized) {
            operating_system=System.getProperty("os.name");
            if (operating_system.equalsIgnoreCase("Linux")) {
                isInLinux=true;
            } else {
                isInLinux=false;
            }  
            initialized=true;
        }

    }
//****************************************************************************** 
    public boolean isLinux() {
        initialize();
        return isInLinux;
    }
//****************************************************************************** 
    public boolean isWindows() {
        initialize();
        return (!isInLinux);
    }
//******************************************************************************    
   
}
