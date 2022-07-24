/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.Filing;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_Users_Delete {
//******************************************************************************
    public static final String TITLE="Delete User";
//******************************************************************************
//******************************************************************************
    public Cmd_adm_Users_Delete(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Change Password Result");
        String prefix=p.request.getParameter(Users.TAG_USERS_ITEM_LOGIN);
        int i=Filing.get().users.userRecords.getRealIndex(prefix);
        if (i>=0) {
            Filing.get().users.userRecords.remove(i);
            Filing.get().users.saveData(p);
            Cmd_adm_General.jobMessage(out,prefix+" eliminado");
        } else {
            Cmd_adm_General.jobMessage(out,"Error: Could not find entry to delete");
        }
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
