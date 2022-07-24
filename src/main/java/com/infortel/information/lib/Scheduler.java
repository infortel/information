/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.lib;

import com.infortel.information.support.ns1.Ns1;
import com.infortel.slibrary.SConvert;
import com.infortel.slibrary.SDate;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Scheduler extends Thread {
//******************************************************************************
    public static Scheduler self;
    private static final int PERIOD=10;
    public long cycle_count=0;
    public String last_public_ip=null;
    public SDate last_public_ip_date=null;
    public String last_ns1_ip=null;
    public long last_ns1_ip_time=0;
    public SDate last_ns1_ip_date=null;
    public SDate last_updated_ip_date=null;
    private static final int NS1_IP_GET_PERIOD=8*(3600*1000);
//******************************************************************************
    public Scheduler() {
        self=this;
    }
//******************************************************************************
    private void reportError(String error) {
        if (!SString.equal(error, lastError)) {
            Log.add(error);
            lastError=error;
        }
    }
    private String lastError;
//******************************************************************************
    public void run() {
        while (true) {
            int period=SConvert.strToInt(Filing.get().setup.get(Setup.TAG.SCHEDULER_PERIOD_S));
            if (period<=0) period=PERIOD;
            try { Thread.sleep(period*1000); } catch (Exception e) {};
            cycle_count++;
            if (SString.isTrue(Filing.get().setup.get(Setup.TAG.NS1_ACTIVE))) {
                verify_public_ip();
            } else {
                last_ns1_ip_time=0;
            }
        }
    }
//******************************************************************************
    private String get_ns1_ip() {
        String error=null;
        String ip=null;
        try {
            ip=Ns1.get_ns1_Ip(null);
            error=Ns1.ipValid(ip);
        } catch (Exception e) {
            error="Could not get NS1 IP: "+e.getMessage();
        }
        if (error!=null) {
            reportError("Error getting NS1 IP: "+error);
            return null;
        } else {
            return ip;
        }
    }
//******************************************************************************
    private void verify_public_ip() {
        long now=System.currentTimeMillis();
        
        //Get ns1 IP if a long time has ellapsed.
        if (now-last_ns1_ip_time>NS1_IP_GET_PERIOD) {
            String ns1_ip=get_ns1_ip();
            if (Ns1.ipValid(ns1_ip)==null) {
                last_ns1_ip=ns1_ip;
                reportError("Obtaining NS1 IP: "+ns1_ip);
                last_ns1_ip_date=new SDate().setNow();
                last_ns1_ip_time=System.currentTimeMillis();
            }
        }
        
        //Get public IP
        
        String public_ip=null;
        try {
            public_ip=Ns1.get_public_ip();
        } catch (Exception e) {
            public_ip=null;
        }
        if (Ns1.ipValid(public_ip)==null) {
            last_public_ip=public_ip;
            last_public_ip_date=new SDate().setNow();
            if (!SString.equal(last_ns1_ip,public_ip)) {
                //Update Ns1 IP.
                String ns1_ip=null;
                try {
                    ns1_ip=Ns1.get_ns1_Ip(public_ip);
                    last_updated_ip_date=new SDate().setNow();
                    reportError("NS1 IP updated: "+ns1_ip+" -> "+public_ip);
                    last_ns1_ip=ns1_ip;
                } catch (Exception e) {
                    reportError("Could not update modified public IP: "+ns1_ip+" -> "+public_ip);
                }
            } else {
                //Public ip matches with ns1 ip.
            }
        } else {
            //Error obtaining public IP.
        }
        
    }
//******************************************************************************
}
