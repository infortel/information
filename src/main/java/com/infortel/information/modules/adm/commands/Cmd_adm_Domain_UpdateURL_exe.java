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
public class Cmd_adm_Domain_UpdateURL_exe {
//******************************************************************************
    public Cmd_adm_Domain_UpdateURL_exe(GeneralParams p, PrintWriter out) {
        String result="0=Nothing was done";
        String prefix=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_PREFIX);
        prefix=prefix.toLowerCase();
        String url=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_URL);
        String password=p.request.getParameter(Forwarding_Data.TAG_REDIRECTS_ITEM_PASSWORD);
        
        int i=Forwarding_Data.self.redirectRecords.getRealIndex(prefix);
             
        if (i>=0) {
            Forwarding_Data.RedirectRec rec=(Forwarding_Data.RedirectRec)Forwarding_Data.self.redirectRecords.get(i).object;
            if ((SString.equal(password, rec.password))) {
                if (SString.equal(url, rec.url)) result="1=No change was required";
                else {
                    rec.url=url;
                    Forwarding_Data.self.saveData(p);
                    result="2=Url Updated";
                }
            } else {
                result="-1=Password invalid";
            }
        } else {
            result="-2=Prefix not found";
        }
        out.println("<return><result>"+result+"</result></return>");
    }
//******************************************************************************    
//******************************************************************************    
}
