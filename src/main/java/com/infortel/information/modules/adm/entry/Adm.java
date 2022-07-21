/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.entry;

import com.infortel.information.modules.adm.commands.Cmd_adm_ChangePassword;
import com.infortel.information.modules.adm.commands.Cmd_adm_ChangePassword_menu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Domain_Delete;
import com.infortel.information.modules.adm.commands.Cmd_adm_Domain_List;
import com.infortel.information.modules.adm.commands.Cmd_adm_Domain_Menu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Domain_Update;
import com.infortel.information.modules.adm.commands.Cmd_adm_Domain_UpdateURL_exe;
import com.infortel.information.modules.adm.commands.Cmd_adm_GetInfortelPassword;
import com.infortel.information.modules.adm.commands.Cmd_adm_Log_List;
import com.infortel.information.modules.adm.commands.Cmd_adm_MainMenu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Delete;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_List;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Menu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Update;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Session;
import com.infortel.information.modules.adm.commands.Cmd_adm_GetPublicIp;
import com.infortel.slibrary.SString;
import java.io.PrintWriter;


/**
 *
 * @author leon
 */
public class Adm {
//******************************************************************************
    PrintWriter out=null;
    //final private static String BLANK_PREFIX="*";
//******************************************************************************
    public final static String COMMAND_GET_INFORTEL_PASSWORD_SISCON="get_infortel_pass_siscon";
    public final static String COMMAND_CHANGE_PASSWORD_ADM_MENU="change_password_adm_menu";
    public final static String COMMAND_CHANGE_PASSWORD_ADM="change_password_adm";
    
    public final static String COMMAND_DOMAIN_MENU="domain_menu";
    public final static String COMMAND_DOMAIN_DELETE="domain_delete";
    public final static String COMMAND_DOMAIN_EDIT="domain_edit";
    public final static String COMMAND_DOMAIN_UPDATE="domain_update";
    public final static String COMMAND_DOMAIN_LIST="domain_list";

    public final static String COMMAND_USER_MENU="user_menu";
    public final static String COMMAND_USER_DELETE="user_delete";
    public final static String COMMAND_USER_EDIT="user_edit";
    public final static String COMMAND_USER_UPDATE="user_update";
    public final static String COMMAND_USER_LIST="user_preparalist";
    
    public final static String COMMAND_LOG_LIST="log_preparalist";

    public final static String COMMAND_UPDATE_URL="update_url";
    public final static String COMMAND_LOGOUT="logout";
    public final static String COMMAND_GET_PUBLIC_IP="get_public_ip";
//******************************************************************************
    public Adm(GeneralParams p) {
        
        p.response.setContentType("text/html;charset=UTF-8");
        try {out = p.response.getWriter(); } catch (Exception e) {}
        try {

            String command=p.request.getParameter("command");
            if (command!=null) command=command.toLowerCase();
            if (SString.equalIgnoreCase(command, COMMAND_LOGOUT))      Session.logout(p); 
            
            else if (SString.equalIgnoreCase(command, COMMAND_DOMAIN_DELETE))      new Cmd_adm_Domain_Delete(p,out); 
            else if (SString.equalIgnoreCase(command, COMMAND_DOMAIN_UPDATE)) new Cmd_adm_Domain_Update(p,out);        
            else if (SString.equalIgnoreCase(command, COMMAND_DOMAIN_LIST)) new Cmd_adm_Domain_List(p,out);        
            else if (SString.equalIgnoreCase(command, COMMAND_DOMAIN_MENU))   new Cmd_adm_Domain_Menu(p,out);    
            
            else if (SString.equalIgnoreCase(command, COMMAND_USER_DELETE))      new Cmd_adm_Users_Delete(p,out); 
            else if (SString.equalIgnoreCase(command, COMMAND_USER_UPDATE)) new Cmd_adm_Users_Update(p,out);        
            else if (SString.equalIgnoreCase(command, COMMAND_USER_LIST)) new Cmd_adm_Users_List(p,out);        
            else if (SString.equalIgnoreCase(command, COMMAND_USER_MENU))   new Cmd_adm_Users_Menu(p,out);   
            
            else if (SString.equalIgnoreCase(command, COMMAND_LOG_LIST))   new Cmd_adm_Log_List(p,out);    
            
            else if (SString.equalIgnoreCase(command, COMMAND_LOGOUT))        Session.logout(p);
            else if (SString.equalIgnoreCase(command, COMMAND_UPDATE_URL))    new Cmd_adm_Domain_UpdateURL_exe(p,out);
            else if (SString.equalIgnoreCase(command, COMMAND_GET_INFORTEL_PASSWORD_SISCON)) new Cmd_adm_GetInfortelPassword(p,out);
            
            else if (SString.equalIgnoreCase(command, COMMAND_CHANGE_PASSWORD_ADM_MENU)) new Cmd_adm_ChangePassword_menu(p,out);
            else if (SString.equalIgnoreCase(command, COMMAND_CHANGE_PASSWORD_ADM)) new Cmd_adm_ChangePassword(p,out);
            else if (SString.equalIgnoreCase(command, COMMAND_GET_PUBLIC_IP)) new Cmd_adm_GetPublicIp(p,out);
            else new Cmd_adm_MainMenu(p,out);

        } finally {
            out.close();
        }
    }
//******************************************************************************
//******************************************************************************
    
}
