/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.Application;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Scheduler;
import com.infortel.information.support.ns1.Ns1;
import com.infortel.slibrary.SDate;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_GetPublicIp {
//******************************************************************************
    public static final String TITLE="Get public IP";
    private PrintWriter out;
//******************************************************************************
    private String formatDate(SDate date) {
        if (date!=null) return date.getString("yyyy-MM-dd HH:mm:ss");
        else return "No date available";
    }
//******************************************************************************
    public Cmd_adm_GetPublicIp(GeneralParams p, PrintWriter out) {
        this.out=out;
        Cmd_adm_General.showTitle(out,"Get Public Ip");
        
        try {

            String javaVersion= System.getProperty("java.version");
            
            out.println("<table border='1' bgcolor='#ccFFff'>");
            
            try {
                String ip=Ns1.get_public_ip();
                report_row("Public IP",ip,"");
            } catch (Exception e) {
                report_row("Public IP","Error",e.getMessage());
            }
            try {
                String ip=Ns1.get_ns1_Ip(null);
                report_row("NS1 IP",ip,"");
            } catch (Exception e) {
                report_row("NS1 IP","Error",e.getMessage());
            }
            
            report_row("---FROM SCHEDULER---","----","");
            report_row("Last NS1 IP",Scheduler.self.last_ns1_ip,formatDate(Scheduler.self.last_ns1_ip_date));
            report_row("Last Public IP",Scheduler.self.last_public_ip,formatDate(Scheduler.self.last_public_ip_date));
            report_row("Last Time IP Updated into NS1","",formatDate(Scheduler.self.last_updated_ip_date));
            
            out.println("</table>");

            
            out.println("<br>");
            out.println("<br>");
            
            out.println("Java Version = "+javaVersion);
            out.print(" / ");
            if (Application.self.isLinux()) out.println("Linux"); else out.println("Windows");
            out.println("<br>");
            
            out.println("<br>");
            out.println("<br>");
            out.println("<a href='?command="+TITLE+"'>"+TITLE+"</a>");
            out.println("<br>");
            
            
            } catch (Exception e) {
            out.println("Error obtaining public IP: "+e.getMessage());
        }
        
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    private void report_row(String title, String value1, String value2) {
        out.println("<tr>");
                out.println("<td>"+title+"</td>");
                out.println("<td>"+value1+"</td>");
                out.println("<td>"+value2+"</td>");

        out.println("</tr>");
    }
//******************************************************************************
    /*
    public static String getNS1_Ip() {
        String result=null;
        try {
            SGetWeb web=new SGetWeb();
            web.addSiteProperty("X-NSONE-Key", API_KEY);
            result=web.connect("https://api.nsone.net/v1/zones", "GET", null);
            if (web.getErrors()!=null) result=web.getErrors();
        } catch (Throwable e) {
            e.printStackTrace();
            result=e.getMessage();
        }
        return result;
    }
    */
//******************************************************************************
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
        } catch (Throwable e) {
          e.printStackTrace();
          result=e.getMessage();
        }
        return result;
    }
    */

//******************************************************************************
//******************************************************************************

}
