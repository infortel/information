/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.modules.adm.entry.Adm;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Cmd_adm_Users_List {
//******************************************************************************
    PrintWriter out;
    GeneralParams p;
//******************************************************************************
    public Cmd_adm_Users_List(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        Cmd_adm_General.showTitle(out,"User List");
        out.println("<form name='input' method='get'>");

        listRedirects(null);
        
        out.println("</form>");
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    public void listRedirects(Users.UserRec rec) {
        
        out.println("<br>");
        out.println("<table border='1' bgcolor='#ccFFff'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>LOGIN</td>");
        out.print("<td>NAME</td>");
        out.print("<td>PASSWORD</td>");
        out.print("<td>ACCESS</td>");
        out.println("</tr>");

        if (rec!=null) listItem(false,rec);
        else {
            for (int i=0; i<Users.self.userRecords.size(); i++) {
                rec=(Users.UserRec)Users.self.userRecords.get(i).object;
                listItem(true,rec);
            }
        }
        out.println("</table>");
                

    }
//*************************************
    private void listItem(boolean includeCommand, Users.UserRec rec) {
        out.print("<tr>");

        out.print("<td>"+textItem(rec,"login", rec.login)+"</td>");
        out.print("<td>"+textItem(rec,"name", rec.name)+"</td>");
        out.print("<td>"+textItem(rec,"password", "********")+"</td>");
        out.print("<td>"+textItem(rec,"access", rec.access)+"</td>");

        out.println("</tr>");
    }
//*************************************
    private String textItem(Users.UserRec rec, String name, String fieldData) {
        fieldData=SString.nullToText(fieldData);
        String result=fieldData;
        if (SString.equal(name, "login")) {
            result="<a href='?command="+Adm.COMMAND_USER_MENU
                +"&"+Cmd_adm_Users_Menu.PARAM_EDIT_THIS_PREFIX+"="+fieldData
                +"'>"+fieldData+"</a>"
                    ;
        }
        return result;
    }
//******************************************************************************
 //******************************************************************************    
}
