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
public class Cmd_adm_Setup_Update {
//******************************************************************************
    public static final String TITLE="Update Setup";
//******************************************************************************
//******************************************************************************
    private PrintWriter out;
    private GeneralParams p;
    String edit_this_prefix;
//******************************************************************************
    public Cmd_adm_Setup_Update(GeneralParams p, PrintWriter out) {
        this.out=out;
        this.p=p;
        present();

    }
//******************************************************************************
    private void present() {
        
        Cmd_adm_General.showTitle(out,TITLE);

        for (int i=0; i<Setup.TAGS.length; i++) {
            String tag=Setup.TAGS[i];
            String value=SString.nullToText(p.request.getParameter(tag));
            Filing.get().setup.set(tag,value);
        }
        Filing.get().setup.commit();
        
        out.println("<br>");
        out.println("<br>");

        out.println("Setup updated");
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);    
    }
//******************************************************************************
//******************************************************************************
//******************************************************************************    
        
}
