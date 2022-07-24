/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;
import com.infortel.information.lib.Filing;
import com.infortel.information.lib.GeneralParams;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;


/**
 *
 * @author leon
 */
public class Cmd_adm_ChangePassword {
//******************************************************************************
    public static final String TITLE="Change Password";
//******************************************************************************
    public Cmd_adm_ChangePassword(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Change Password Result");

        String newPassword1=p.request.getParameter(Cmd_adm_ChangePassword_menu.PARAM_NEW_PASSWORD1);
        String newPassword2=p.request.getParameter(Cmd_adm_ChangePassword_menu.PARAM_NEW_PASSWORD2);
        boolean ok=true;      
        if (SString.isClear(newPassword1)) {
            Cmd_adm_General.jobMessage(out,"Error: no password was indicated");
            ok=false;
        }
        
        if (!SString.equal(newPassword1,newPassword2)) {
            Cmd_adm_General.jobMessage(out,"Error: new password entries do not match");
            ok=false;
        }
              
        if (ok) {
            p.session.user.password=newPassword1;
            Filing.get().users.saveData(p);
            Cmd_adm_General.jobMessage(out,"Password Changed");
        }
        
        Cmd_adm_General.gotoMainMenuLink(out);
        Cmd_adm_General.closeBody(out);
}
//******************************************************************************
    
//******************************************************************************
}
