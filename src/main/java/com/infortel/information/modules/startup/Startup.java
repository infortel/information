/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.infortel.information.modules.startup;

import com.infortel.information.lib.Filing;
import com.infortel.information.lib.Log;
import com.infortel.information.lib.Log_Password;
import com.infortel.information.lib.Scheduler;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Startup implements ServletContextListener {
//******************************************************************************
    public static Scheduler scheduler;
//******************************************************************************
    @Override
    public void contextInitialized(ServletContextEvent event) {
        new Filing();
        scheduler=new Scheduler();
        scheduler.start();
        Log.add("System Started");
    }
//******************************************************************************
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Log.add("System Stopped");
    }
//******************************************************************************
}