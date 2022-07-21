/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.activation_library.main.Get_Key;
import com.infortel.information.lib.Constants;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_General {
//****************************************************************************** 
    public static void showTitle(PrintWriter out, String title) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>"+Constants.getVersionSummary()+"</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>");
        out.println("Infortel CA - Version "+Constants.getVersionSummary()+" <br>"+title);
        out.println("</h1>");
        out.println("<br>");
        out.println("<br><br>");
    }
//****************************************************************************** 
    public static void closeBody(PrintWriter out) {
        out.println("</body>");
    }
//****************************************************************************** 
    public static void jobMessage(PrintWriter out, String message) {
        //out.println("<br>");
        //out.println(message);
        //out.println("<br>");
        if (out!=null) {
            out.println("<table border='1' bgcolor='red'>");
            out.print("<tr bgcolor='CCCCCC'><td>");
            out.println(message);
            out.print("</td></tr></table>");
        }
    }
//****************************************************************************** 
    public static void gotoMainMenuLink(PrintWriter out) {
        if (out!=null) out.println("<br><br><a href='?'>Goto main menu...</a>");
    }
//****************************************************************************** 
}
