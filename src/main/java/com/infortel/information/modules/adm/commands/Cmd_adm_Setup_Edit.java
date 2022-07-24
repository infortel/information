/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.Filing;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Setup;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Cmd_adm_Setup_Edit {
//******************************************************************************
    public static final String TITLE="Edit Setup";
//******************************************************************************
//******************************************************************************
    private PrintWriter out;
    private GeneralParams p;
    String edit_this_prefix;
//******************************************************************************
    public Cmd_adm_Setup_Edit(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        present();

    }
//******************************************************************************
    private void present() {
        
        Cmd_adm_General.showTitle(out,TITLE);
        out.println("<form name='input' method='get'>");
    
        Cmd_adm_General.closeBody(out);
        //showCommands();


        listEditRecord();

        out.println("<br>");
        out.println("<br>");

        out.println("</form>");
        
        out.println("<br>");
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);    }
//******************************************************************************
    private void listEditRecord() {
        //Users.UserRec rec=new Users.UserRec();
        //StringSet set=Filing.get().users.userRecords.get(edit_this_prefix);
        //if (set!=null) rec=(Users.UserRec)set.object;

        out.println("<table border='1' bgcolor='#FFFFCC'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>FIELD</td>");
        out.print("<td>DATA</td>");
        out.println("</tr>");
        
        for (int i=0; i<Setup.TAGS.length; i++) row(Setup.TAGS[i]);
        
        out.println("</table>");
        
        out.println("<input type='submit' value='" + Cmd_adm_Setup_Update.TITLE + "' name='command'/>");

                
    }
//************************************
    private void row(String tag) {
        out.println("<tr>");
        out.println("<td>"+tag+"</td>");
        String type="text";
        if ((SString.equal(tag, Setup.TAG.DB_PASSWORD)) || (SString.equal(tag, Setup.TAG.NS1_API_KEY))) type="password";
        out.println("<td><input type='"+type+"' name='"+tag+"' value='"+SString.nullToText(Filing.get().setup.get(tag))+"'/></td>");
        
    }
//******************************************************************************
//******************************************************************************
//******************************************************************************    
        
}
