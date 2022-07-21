/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_Users_Delete {
//******************************************************************************
    public Cmd_adm_Users_Delete(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Change Password Result");
        String prefix=p.request.getParameter(Users.TAG_USERS_ITEM_LOGIN);
        int i=Users.self.userRecords.getRealIndex(prefix);
        if (i>=0) {
            Users.self.userRecords.remove(i);
            Users.self.saveData(p);
            Cmd_adm_General.jobMessage(out,prefix+" eliminado");
        } else {
            Cmd_adm_General.jobMessage(out,"Error: Could not find entry to delete");
        }
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
