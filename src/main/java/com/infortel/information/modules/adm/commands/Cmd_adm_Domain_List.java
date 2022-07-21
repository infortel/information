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

/**
 *
 * @author leon
 */
public class Cmd_adm_Domain_List {
//******************************************************************************
    PrintWriter out;
    GeneralParams p;
//******************************************************************************
    public Cmd_adm_Domain_List(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        Cmd_adm_General.showTitle(out,"Domain List");
        out.println("<form name='input' method='get'>");

        listRedirects(null);
        
        out.println("</form>");
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************
    public void listRedirects(Forwarding_Data.RedirectRec rec) {
        
        out.println("<br>");
        out.println("<table border='1' bgcolor='#ccFFff'>");
        out.print("<tr bgcolor='CCCCCC'>");
        out.print("<td>PREFIX</td>");
        out.print("<td>URL</td>");
        out.print("<td>DESCRIPTION</td>");
        out.print("<td>PASSWORD</td>");
        out.print("<td>TYPE</td>");
        out.println("</tr>");

        if (rec!=null) listItem(false,rec);
        else {
            for (int i=0; i<Forwarding_Data.self.redirectRecords.size(); i++) {
                rec=(Forwarding_Data.RedirectRec)Forwarding_Data.self.redirectRecords.get(i).object;
                listItem(true,rec);
            }
        }
        out.println("</table>");
                

    }
//*************************************
    private void listItem(boolean includeCommand, Forwarding_Data.RedirectRec rec) {
        out.print("<tr>");

        out.print("<td>"+textItem(rec,"prefix", rec.prefix)+"</td>");
        out.print("<td>"+textItem(rec,"url", rec.url)+"</td>");
        out.print("<td>"+textItem(rec,"description", rec.description)+"</td>");
        out.print("<td>"+textItem(rec,"password", rec.password)+"</td>");
        out.print("<td>"+textItem(rec,"type", rec.type)+"</td>");

        out.println("</tr>");
    }
//*************************************
    private String textItem(Forwarding_Data.RedirectRec rec, String name, String data) {
        data=SString.nullToText(data);
        String result=data;
        if (SString.equal(name, "url")) {
            result="<a target='_blank' href='"+data+"'>"+data+"</a>";
        } else if (SString.equal(name, "prefix")) {
            result="<a href='?command="+Adm.COMMAND_DOMAIN_MENU
                +"&"+Cmd_adm_Domain_Menu.PARAM_EDIT_THIS_PREFIX+"="+data
                +"'>"+data+"</a>"
                    ;
        }
        return result;
    }
//******************************************************************************
 //******************************************************************************    
}
