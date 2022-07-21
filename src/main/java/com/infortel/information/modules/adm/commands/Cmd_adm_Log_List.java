/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Constants;
import com.infortel.information.lib.Log;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;
import com.infortel.slibrary.ListString;
import com.infortel.slibrary.SString;
import com.infortel.slibrary.SXml;

/**
 *
 * @author leon
 */
public class Cmd_adm_Log_List {
//******************************************************************************
    PrintWriter out;
    GeneralParams p;
//******************************************************************************
    public Cmd_adm_Log_List(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        Cmd_adm_General.showTitle(out,"Log List");
        out.println("<form name='input' method='get'>");

        list(null);
        
        out.println("</form>");
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    public void list(Users.UserRec rec) {
        
        out.println("<br>");
        out.println("<table border='1' bgcolor='#ccFFff'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>USERNAME</td>");
        out.print("<td>NAME</td>");
        out.print("<td>COMPANY_CODE</td>");
        out.print("<td>COMPANY_NAME</td>");
        out.print("<td>DATE</td>");
        out.print("<td>IP</td>");
        out.println("</tr>");

        ListString list=Log.getLogContext();
        SString xml=new SString();
        for (int i=0; i<list.size(); i++) {
            xml.assign(list.get(i));
            listItem(xml);
            if (i>100) break;
        }
        
        out.println("</table>");
                

    }
//*************************************
    private void listItem(SString xml) {
        out.print("<tr>");

        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.USER_LOGIN)+"</td>");
        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.USER_NAME)+"</td>");
        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.COMPANY_CODE)+"</td>");
        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.COMPANY_NAME)+"</td>");
        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.DATE)+"</td>");
        out.print("<td>"+textItem(xml,Constants.Adm_param_get_siscon_password.IP)+"</td>");

        out.println("</tr>");
    }
//*************************************
    private String textItem(SString xml, String tag) {
        return SString.nullToText(SXml.get(xml, tag));
    }
//******************************************************************************
 //******************************************************************************    
}
