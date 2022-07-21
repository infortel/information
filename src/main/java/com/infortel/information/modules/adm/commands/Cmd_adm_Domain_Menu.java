/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.modules.adm.entry.Adm;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Forwarding_Data;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;
import com.infortel.slibrary.StringSet;

/**
 *
 * @author leon
 */
public class Cmd_adm_Domain_Menu {
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
    public Cmd_adm_Domain_Menu(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        present();

    }
//******************************************************************************
    private void present() {
        
            edit_this_prefix=p.request.getParameter(PARAM_EDIT_PREFIX);
            String editThisPrefix=p.request.getParameter(PARAM_EDIT_THIS_PREFIX);
            if (!SString.isClear(editThisPrefix)) edit_this_prefix=editThisPrefix;
            
        Cmd_adm_General.showTitle(out,"Domain Administration");
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
        Forwarding_Data.RedirectRec rec=new Forwarding_Data.RedirectRec();
        StringSet set=Forwarding_Data.self.redirectRecords.get(edit_this_prefix);
        if (set!=null) rec=(Forwarding_Data.RedirectRec)set.object;

        out.println("<table border='1' bgcolor='#FFFFCC'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>Select</td>");
        out.print("<td>PREFIX</td>");
        out.print("<td>URL</td>");
        out.print("<td>DESCRIPTION</td>");
        out.print("<td>PASSWORD</td>");
        out.print("<td>TYPE</td>");
        out.println("</tr>");
        
        out.println("<input type='hidden' name='"+PARAM_PREVIOUS_PREFIX+"' value='"+SString.nullToText(edit_this_prefix)+"'/>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<input type='submit' value='" + Adm.COMMAND_DOMAIN_UPDATE + "' name='command'/>");
        out.println("<input type='submit' value='" + Adm.COMMAND_DOMAIN_DELETE + "' name='command'/>");
        out.println("</td>");
        out.println("<td><input type='text' name='"+Forwarding_Data.TAG_REDIRECTS_ITEM_PREFIX+"' value='"+SString.nullToText(rec.prefix)+"'/></td>");
        out.println("<td><input type='text' name='"+Forwarding_Data.TAG_REDIRECTS_ITEM_URL+"' value='"+SString.nullToText(rec.url)+"'/></td>");
        out.println("<td><input type='text' name='"+Forwarding_Data.TAG_REDIRECTS_ITEM_DESCRIPTION+"' value='"+SString.nullToText(rec.description)+"'/></td>");
        out.println("<td><input type='text' name='"+Forwarding_Data.TAG_REDIRECTS_ITEM_PASSWORD+"' value='"+SString.nullToText(rec.password)+"'/></td>");
        out.println("<td><input type='text' name='"+Forwarding_Data.TAG_REDIRECTS_ITEM_TYPE+"' value='"+SString.nullToText(rec.type)+"'/></td>");
        out.println("</tr>");
        
        out.println("</table>");
        
        //out.println("<br>");
        //out.println("<input type='submit' value='"+COMMAND_DOMAIN_LIST+"' name='command'/>");       

                
    }
//******************************************************************************
//******************************************************************************
//******************************************************************************
//******************************************************************************    
        
}
