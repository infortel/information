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
public class Cmd_adm_ChangePassword_menu {
//******************************************************************************
    public static final String TITLE="Menu Change Password";
//******************************************************************************
    public final static String PARAM_NEW_PASSWORD1="new_password1";
    public final static String PARAM_NEW_PASSWORD2="new_password2";
    
//******************************************************************************
    public Cmd_adm_ChangePassword_menu(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Change Admin Password");
        out.println("<form name='input' method='get'>");
        out.println("Change password: ");
        out.println("<table border='1' bgcolor='#FFFFCC'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.println("<td>New Password:<br><input type='password' value='"+""+"' name='"+PARAM_NEW_PASSWORD1+"'/></td>");
        out.println("<td>New Password Confirmation:<br><input type='password' value='"+""+"' name='"+PARAM_NEW_PASSWORD2+"'/></td>");
        out.println("<td><input type='submit' value='"+Cmd_adm_ChangePassword.TITLE+"' name='command'/></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("</form>");
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
