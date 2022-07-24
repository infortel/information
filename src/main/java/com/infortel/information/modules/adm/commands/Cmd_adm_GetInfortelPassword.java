/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.modules.adm.commands.getInfortelPassword.SystemDateProcess;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Constants;
import com.infortel.information.lib.Filing;
import com.infortel.information.lib.Log_Password;
import com.infortel.information.lib.Users;
import java.io.PrintWriter;
import com.infortel.slibrary.*;

/**
 *
 * @author leon
 */
public class Cmd_adm_GetInfortelPassword {
//******************************************************************************
    public final static String TITLE="get_infortel_pass_siscon";
//******************************************************************************
    Users.UserRec user;
    GeneralParams p;
//******************************************************************************
    public Cmd_adm_GetInfortelPassword(GeneralParams p, PrintWriter out) {
        this.p=p;
        String username=p.request.getParameter(Constants.Adm_param_get_siscon_password.USER_LOGIN);
        String code=p.request.getParameter(Constants.Adm_param_get_siscon_password.CODE);
        

        user=Filing.get().users.getUser(username);
        
        String result="";
        String result1="";
        String error="";
        
        if (user==null) {
            error="Login not found";
        } else if (!user.hasAccessTo(Users.Permit.RED_INFORTEL_PASS_SISCON)) {
            error="This user has no access to SISCON.";
            user=null;
        } else {
            result=encrypt_old(BASE_ENCRYPT_PASSWORD, user.password);
            result1=loginPassword_encrypt(user.password);
            addLog();
        }
        
        if (error.length()>0) error="<error>"+error+"</error>";
        if (result.length()>0) {
            String reference=encrypt_old(BASE_ENCRYPT_PASSWORD, code);
            result="<result>"+result+"</result>"
                    +"<result1>"+result1+"</result1>"
                    +"<reference>"+reference+"</reference>";
        }
        
        out.println("<return>"+error+result+"</return>"); 
    }
//******************************************************************************
    private void addLog() {
        String ip=p.request.getParameter(Constants.Adm_param_get_siscon_password.IP);
        String companyCode=p.request.getParameter(Constants.Adm_param_get_siscon_password.COMPANY_CODE);
        String companyName=p.request.getParameter(Constants.Adm_param_get_siscon_password.COMPANY_NAME);
        String date=new SDate().setNow().getString("dd/MM/yyyy HH:mm:ss");

        SString xml=new SString();
        xml.assign(
                 SXml.set(Constants.Adm_param_get_siscon_password.USER_LOGIN, user.login)
                +SXml.set(Constants.Adm_param_get_siscon_password.USER_NAME, user.name)
                +SXml.set(Constants.Adm_param_get_siscon_password.IP, ip)
                +SXml.set(Constants.Adm_param_get_siscon_password.COMPANY_CODE, companyCode)
                +SXml.set(Constants.Adm_param_get_siscon_password.COMPANY_NAME, companyName)
                +SXml.set(Constants.Adm_param_get_siscon_password.DATE, date)
                );
        
        Log_Password.addLog(xml.str());
    }
//******************************************************************************
    private static final int BASE_ENCRYPT_PASSWORD=2384732;
//******************************************************************************
    private String encrypt_old(int base, String text) {
        int res=0;
        if (text!=null) if (text.length()>0) {
            text=text.toUpperCase();
            res=base+(int)text.charAt(0)*1000000;
            int f=3;
            int L=16;
            if (text.length()>16) L=text.length();
            for (int x=0; x<L; x++) {
                int c;
                if (x<text.length()) c=(int)text.charAt(x); else c=256-x-1;
                res=res+(c*f);
                f=(f+res) % 10000;
            }
            return(""+res);
        }
        return(null);
    }
//******************************************************************************
    private final static byte[] LOGIN_PASSWORD_KEY_CODE={23,32+33,64-2,33,44,23+45,32,43};
    //private final static byte[] LOGIN_PASSWORD_KEY_CODE={(23+2),32,64,33,(99-33),(23-10),(44+3),(92-5)};

    private final static String LOGIN_PASSWORD_ENCRYPT_PREFIX="a";
    public static String loginPassword_encrypt(String text) {
        String result=SystemDateProcess.enTime(LOGIN_PASSWORD_KEY_CODE,text);
        result=LOGIN_PASSWORD_ENCRYPT_PREFIX+":"+result;
        return result;
    } 
//******************************************************************************
}
