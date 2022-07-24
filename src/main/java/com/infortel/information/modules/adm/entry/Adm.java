/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.entry;

import com.infortel.information.modules.adm.commands.Cmd_adm_ChangePassword;
import com.infortel.information.modules.adm.commands.Cmd_adm_ChangePassword_menu;
import com.infortel.information.modules.adm.commands.Cmd_adm_GetInfortelPassword;
import com.infortel.information.modules.adm.commands.Cmd_adm_Log_User_Passwords;
import com.infortel.information.modules.adm.commands.Cmd_adm_MainMenu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Delete;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_List;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Menu;
import com.infortel.information.modules.adm.commands.Cmd_adm_Users_Update;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.modules.adm.commands.Cmd_adm_GetPublicIp;
import com.infortel.information.modules.adm.commands.Cmd_adm_Log_General;
import com.infortel.information.modules.adm.commands.Cmd_adm_Logout;
import com.infortel.information.modules.adm.commands.Cmd_adm_Setup_Edit;
import com.infortel.information.modules.adm.commands.Cmd_adm_Setup_Update;
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
    /*
    public final static String COMMAND_GET_INFORTEL_PASSWORD_SISCON="get_infortel_pass_siscon";
    public final static String COMMAND_CHANGE_PASSWORD_ADM_MENU="change_password_adm_menu";
    public final static String COMMAND_CHANGE_PASSWORD_ADM="change_password_adm";
    
    public final static String COMMAND_USER_MENU="user_menu";
    public final static String COMMAND_USER_DELETE="user_delete";
    public final static String COMMAND_USER_EDIT="user_edit";
    public final static String COMMAND_USER_UPDATE="user_update";
    public final static String COMMAND_USER_LIST="user_preparalist";
    
    public final static String COMMAND_LOG_LIST="log_preparalist";

    public final static String COMMAND_UPDATE_URL="update_url";
    public final static String COMMAND_LOGOUT="logout";
    public final static String COMMAND_GET_PUBLIC_IP="get_public_ip";
*/
//******************************************************************************
    public Adm(GeneralParams p) {
        
        p.response.setContentType("text/html;charset=UTF-8");
        try {out = p.response.getWriter(); } catch (Exception e) {}
        try {

            String command=p.request.getParameter("command");
            if (command!=null) command=command.toLowerCase();
            if (SString.equalIgnoreCase(command, Cmd_adm_Logout.TITLE))               new Cmd_adm_Logout(p,out);
            
            else if (SString.equalIgnoreCase(command, Cmd_adm_Users_Delete.TITLE))      new Cmd_adm_Users_Delete(p,out); 
            else if (SString.equalIgnoreCase(command, Cmd_adm_Users_Update.TITLE)) new Cmd_adm_Users_Update(p,out);        
            else if (SString.equalIgnoreCase(command, Cmd_adm_Users_List.TITLE)) new Cmd_adm_Users_List(p,out);        
            else if (SString.equalIgnoreCase(command, Cmd_adm_Users_Menu.TITLE))   new Cmd_adm_Users_Menu(p,out);   
            else if (SString.equalIgnoreCase(command, Cmd_adm_Setup_Edit.TITLE))   new Cmd_adm_Setup_Edit(p,out);   
            else if (SString.equalIgnoreCase(command, Cmd_adm_Setup_Update.TITLE))   new Cmd_adm_Setup_Update(p,out);   
            
            else if (SString.equalIgnoreCase(command, Cmd_adm_Log_User_Passwords.TITLE))   new Cmd_adm_Log_User_Passwords(p,out);    
            else if (SString.equalIgnoreCase(command, Cmd_adm_Log_General.TITLE))   new Cmd_adm_Log_General(p,out);    
            
            //else if (SString.equalIgnoreCase(command, Cmd_adm_Domain_UpdateURL_exe.TITLE))    new Cmd_adm_Domain_UpdateURL_exe(p,out);
            else if (SString.equalIgnoreCase(command, Cmd_adm_GetInfortelPassword.TITLE)) new Cmd_adm_GetInfortelPassword(p,out);
            
            else if (SString.equalIgnoreCase(command, Cmd_adm_ChangePassword_menu.TITLE)) new Cmd_adm_ChangePassword_menu(p,out);
            else if (SString.equalIgnoreCase(command, Cmd_adm_ChangePassword.TITLE)) new Cmd_adm_ChangePassword(p,out);
            else if (SString.equalIgnoreCase(command, Cmd_adm_GetPublicIp.TITLE)) new Cmd_adm_GetPublicIp(p,out);
            else new Cmd_adm_MainMenu(p,out);

        } finally {
            out.close();
        }
    }
//******************************************************************************
//******************************************************************************
    
}
