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
    public static final String TITLE="Main Menu";
//******************************************************************************
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
        out.println(TAB+"<a href='?command="+Cmd_adm_Logout.TITLE+"'>"+Cmd_adm_Logout.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_Users_List.TITLE+"'>"+Cmd_adm_Users_List.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_Users_Menu.TITLE+"'>"+Cmd_adm_Users_Menu.TITLE+"</a>"+NEWLINE);
        //out.println(TAB+"<a href='?command="+Cmd_adm_GetInfortelPassword.TITLE+"'>"+Cmd_adm_GetInfortelPassword.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_Setup_Edit.TITLE+"'>"+Cmd_adm_Setup_Edit.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"."+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_Log_User_Passwords.TITLE+"'>"+Cmd_adm_Log_User_Passwords.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_Log_General.TITLE+"'>"+Cmd_adm_Log_General.TITLE+"</a>"+NEWLINE);
        out.println(TAB+"."+NEWLINE);
        out.println(TAB+"<a href='?command="+Cmd_adm_GetPublicIp.TITLE+"'>"+Cmd_adm_GetPublicIp.TITLE+"</a>"+NEWLINE);
        out.println("</table>");
        out.println("<br>");
        
    }
//******************************************************************************
}
