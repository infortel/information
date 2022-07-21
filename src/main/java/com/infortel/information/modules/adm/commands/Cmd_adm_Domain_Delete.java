/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Forwarding_Data;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_Domain_Delete {
//******************************************************************************
    public Cmd_adm_Domain_Delete(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Change Password Result");
        String prefix=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_PREFIX);
        int i=Forwarding_Data.self.redirectRecords.getRealIndex(prefix);
        if (i>=0) {
            Forwarding_Data.self.redirectRecords.remove(i);
            Forwarding_Data.self.saveData(p);
            Cmd_adm_General.jobMessage(out,prefix+" eliminado");
        } else {
            Cmd_adm_General.jobMessage(out,"Error: Could not find entry to delete");
        }
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
