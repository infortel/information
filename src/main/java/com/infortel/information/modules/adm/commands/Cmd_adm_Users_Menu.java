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
import com.infortel.slibrary.StringSet;

/**
 *
 * @author leon
 */
public class Cmd_adm_Users_Menu {
//******************************************************************************
    //public final static String PARAM_PREFIX="prefix";
    //public final static String PARAM_URL="url";
    //public final static String PARAM_DESCRIPTION="description";
    //public final static String PARAM_TYPE="type";
    //public final static String PARAM_PASSWORD="password";
    public final static String PARAM_EDIT_PREFIX="edit_prefix1";
    public final static String PARAM_EDIT_THIS_PREFIX="edit_this_prefix";
    public final static String PARAM_PREVIOUS_PREFIX="previous_prefix";
//******************************************************************************
    private PrintWriter out;
    private GeneralParams p;
    String edit_this_prefix;
//******************************************************************************
    public Cmd_adm_Users_Menu(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        present();

    }
//******************************************************************************
    private void present() {
        
            edit_this_prefix=p.request.getParameter(PARAM_EDIT_PREFIX);
            String editThisPrefix=p.request.getParameter(PARAM_EDIT_THIS_PREFIX);
            if (!SString.isClear(editThisPrefix)) edit_this_prefix=editThisPrefix;
            
        Cmd_adm_General.showTitle(out,"User Administration");
        out.println("<form name='input' method='get'>");
    
        Cmd_adm_General.closeBody(out);
        //showCommands();


        listEditRecord();

        out.println("<br>");
        out.println("<br>");

        out.println("</form>");
    }
//******************************************************************************
    private void listEditRecord() {
        Users.UserRec rec=new Users.UserRec();
        StringSet set=Users.self.userRecords.get(edit_this_prefix);
        if (set!=null) rec=(Users.UserRec)set.object;

        out.println("<table border='1' bgcolor='#FFFFCC'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>Select</td>");
        out.print("<td>LOGIN</td>");
        out.print("<td>NAME</td>");
        out.print("<td>PASSWORD</td>");
        out.print("<td>ACCESS</td>");
        out.println("</tr>");
        
        out.println("<input type='hidden' name='"+PARAM_PREVIOUS_PREFIX+"' value='"+SString.nullToText(edit_this_prefix)+"'/>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<input type='submit' value='" + Adm.COMMAND_USER_UPDATE + "' name='command'/>");
        out.println("<input type='submit' value='" + Adm.COMMAND_USER_DELETE + "' name='command'/>");
        out.println("</td>");
        out.println("<td><input type='text' name='"+Users.TAG_USERS_ITEM_LOGIN+"' value='"+SString.nullToText(rec.login)+"'/></td>");
        out.println("<td><input type='text' name='"+Users.TAG_USERS_ITEM_NAME+"' value='"+SString.nullToText(rec.name)+"'/></td>");
        out.println("<td><input type='password' name='"+Users.TAG_USERS_ITEM_PASSWORD+"' value='"+SString.nullToText(rec.password)+"'/></td>");
        out.println("<td><input type='text' name='"+Users.TAG_USERS_ITEM_ACCESS+"' value='"+SString.nullToText(rec.access)+"'/></td>");
        out.println("</tr>");
        
        out.println("</table>");
        
        out.println("<br><br>");
        out.println("ACCESS DETAIL:<br><br>");
        out.println("Siscon=red/siscon<br>");
        out.println("Adm (este sistema)=red/adm<br>");
        
                
    }
//******************************************************************************
//******************************************************************************
//******************************************************************************
//******************************************************************************    
        
}
