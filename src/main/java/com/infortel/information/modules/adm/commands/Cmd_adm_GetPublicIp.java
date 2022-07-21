/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.modules.adm.entry.Adm;
import com.infortel.slibrary.SGetWeb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author leon
 */
public class Cmd_adm_GetPublicIp {
//******************************************************************************
    public Cmd_adm_GetPublicIp(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Get Public Ip");
        
        try {
            String publicIp=getPublicIp();
            String nsIp=getNS1_Ip();
            out.println("Public IP = "+publicIp);
            out.println("<br>");
            out.println("NS1 IP = "+nsIp);
            out.println("<br>");
            out.println("<br>");
            out.println("<a href='?command="+Adm.COMMAND_GET_PUBLIC_IP+"'>Get Public IP</a>");
            out.println("<br>");
            } catch (Exception e) {
            out.println("Error obtaining public IP: "+e.getMessage());
        }
        
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    public static String getPublicIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//******************************************************************************
    public static String getNS1_Ip() {
        String result=null;
        try {
            SGetWeb web=new SGetWeb();
            web.addSiteProperty("X-NSONE-Key", API_KEY);
            result=web.connect("https://api.nsone.net/v1/zones", "GET", null);
            if (web.getErrors()!=null) result=web.getErrors();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
//******************************************************************************
    private final static String API_KEY="t8D5XL3zb2xvA66N4vvP";
    /*
    public static String getNS1_Ip() {
        String result=null;
        try {
          URL url=new URL("https://api.nsone.net/v1/zones");
          HttpsURLConnection conn = (HttpsURLConnection )url.openConnection();
          conn.setRequestProperty("X-NSONE-Key", API_KEY);
          conn.setConnectTimeout(1500); 
          conn.setRequestMethod("GET");
          conn.connect();
          int res=conn.getResponseCode();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return result;
    }
*/
//******************************************************************************
//******************************************************************************

}
