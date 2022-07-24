/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.Filing;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;
import com.infortel.slibrary.SString;

/**
 *
 * @author leon
 */
public class Cmd_adm_Users_Update {
//******************************************************************************
    public static final String TITLE="Update User";
//******************************************************************************
//******************************************************************************
    public Cmd_adm_Users_Update(GeneralParams p, PrintWriter out) {
        Cmd_adm_General.showTitle(out,"Users Update");

        String previousPrefix=p.request.getParameter(Cmd_adm_Users_Menu.PARAM_PREVIOUS_PREFIX);
        if (previousPrefix!=null) previousPrefix=previousPrefix.toLowerCase();
        
        Users.UserRec rec=new Users.UserRec();
        rec.login=p.request.getParameter(Users.TAG_USERS_ITEM_LOGIN);
        boolean ok=true;
        if (SString.isClear(rec.login)) {
            Cmd_adm_General.jobMessage(out,"Error: login not indicated");
            ok=false;
        }
        
        SString st=new SString(rec.login);
        if (st.getSymbolCount("_$")>0) {
            Cmd_adm_General.jobMessage(out,"Error: login has invalid characters.");
            ok=false;
        }
        
        if (ok) {
            rec.login=rec.login.toLowerCase();
            rec.name=p.request.getParameter(Users.TAG_USERS_ITEM_NAME);
            rec.password=p.request.getParameter(Users.TAG_USERS_ITEM_PASSWORD);
            rec.access=p.request.getParameter(Users.TAG_USERS_ITEM_ACCESS);

            int ipref1=-1;
            if (SString.hasChar(previousPrefix)) ipref1=Filing.get().users.userRecords.getRealIndex(previousPrefix);

            int ipref2=Filing.get().users.userRecords.getRealIndex(rec.login);
            if ((ipref2>=0) && (ipref1!=ipref2)) {
                Cmd_adm_General.jobMessage(out,"Error: this login already exists");
                ok=false;            
            }
            
            if (ok) {

                if (ipref1>=0) {
                    Filing.get().users.userRecords.remove(ipref1);
                    Cmd_adm_General.jobMessage(out,rec.login+" updated");
                } else {
                    Cmd_adm_General.jobMessage(out,rec.login+" created");
                }
                Filing.get().users.userRecords.add(rec.login,rec);
                Filing.get().users.saveData(p);
            }
        }

        
        Cmd_adm_General.gotoMainMenuLink(out);
        
        Cmd_adm_General.closeBody(out);
    }
//******************************************************************************    
}
