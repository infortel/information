/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.modules.adm.entry.Adm;
import com.infortel.information.lib.GeneralParams;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_MainMenu {
//******************************************************************************
    PrintWriter out=null;
//******************************************************************************
//******************************************************************************
    public Cmd_adm_MainMenu(GeneralParams p, PrintWriter out) {
        this.out=out;
        Cmd_adm_General.showTitle(out,"Main Menu");
        
        presentMainMenu();
        
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    private void presentMainMenu() {
        final String TAB="<tr><td>";
        final String NEWLINE="</td></tr>";

        out.println("<table border='1' bgcolor='#FFFFCC'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.println(TAB+"<a href='?command="+Adm.COMMAND_LOGOUT+"'>Logout</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_DOMAIN_LIST+"'>List Domains</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_DOMAIN_MENU+"'>Setup Domain</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_USER_LIST+"'>List Users</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_USER_MENU+"'>Setup User</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_CHANGE_PASSWORD_ADM_MENU+"'>Change administration password</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_LOG_LIST+"'>Log Report</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Adm.COMMAND_GET_PUBLIC_IP+"'>Get Public IP</a>"+NEWLINE);
        out.println("</table>");
        out.println("<br>");
        
    }
//******************************************************************************
}
