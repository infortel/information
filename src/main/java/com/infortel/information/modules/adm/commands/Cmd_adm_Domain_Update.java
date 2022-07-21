/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Forwarding_Data;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Cmd_adm_Domain_Update {
//******************************************************************************
    public Cmd_adm_Domain_Update(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Domain Update");

        String previousPrefix=p.request.getParameter(Cmd_adm_Domain_Menu.PARAM_PREVIOUS_PREFIX);
        if (previousPrefix!=null) previousPrefix=previousPrefix.toLowerCase();
        
        Forwarding_Data.RedirectRec rec=new Forwarding_Data.RedirectRec();
        rec.prefix=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_PREFIX);
        boolean ok=true;
        if (SString.isClear(rec.prefix)) {
            Cmd_adm_General.jobMessage(out,"Error: prefix not indicated");
            ok=false;
        }
        
        SString st=new SString(rec.prefix);
        if (st.getSymbolCount("_$")>0) {
            Cmd_adm_General.jobMessage(out,"Error: prefix has invalid characters.");
            ok=false;
        }
        
        if (ok) {
            rec.prefix=rec.prefix.toLowerCase();
            rec.type=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_TYPE);
            rec.url=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_URL);
            rec.description=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_DESCRIPTION);
            rec.password=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_PASSWORD);

            int ipref1=-1;
            if (SString.hasChar(previousPrefix)) ipref1=Forwarding_Data.self.redirectRecords.getRealIndex(previousPrefix);

            int ipref2=Forwarding_Data.self.redirectRecords.getRealIndex(rec.prefix);
            if ((ipref2>=0) && (ipref1!=ipref2)) {
                Cmd_adm_General.jobMessage(out,"Error: this prefix already exists");
                ok=false;            
            }
            
            if (ok) {

                if (ipref1>=0) {
                    Forwarding_Data.self.redirectRecords.remove(ipref1);
                    Cmd_adm_General.jobMessage(out,rec.prefix+" updated");
                } else {
                    Cmd_adm_General.jobMessage(out,rec.prefix+" created");
                }
                Forwarding_Data.self.redirectRecords.add(rec.prefix,rec);
                Forwarding_Data.self.saveData(p);
            }
        }

        
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
