/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import com.infortel.slibrary.*;
import javax.servlet.http.*;
import java.io.PrintWriter;
/**
 *
 * @author leon
 */
public class Session {
    
//******************************************************************************
    private SString errors=new SString();
    //**************************************************************************
    public void addError(String error) {
        errors.append(error+"\n");
    }
    //**************************************************************************
    public String getErrors() {
        if (errors.length()==0) return null;
        return errors.str();
    }
    //**************************************************************************
//******************************************************************************
//******************************************************************************
    public static final String K_SESSION_ATTRIBUTE_NAME="session_object";
    public Users.UserRec user;
//******************************************************************************
    public static void getSession(GeneralParams p) {
            HttpSession httpSession=p.request.getSession(true);
            httpSession.setMaxInactiveInterval(5*60); //5 min. session.
            p.session=(Session)httpSession.getAttribute(K_SESSION_ATTRIBUTE_NAME);
            if (p.session==null) {
                p.session=new Session();
                httpSession.setAttribute(K_SESSION_ATTRIBUTE_NAME,p.session);
            }
    }
//******************************************************************************
    public static int login_failure_count=0;
    public static String verifyLogin (GeneralParams p) {
        getSession(p);
        String error=null;
        
        if (p.session.user==null) {
            error="Invalid Login/Password";
            String login1=p.request.getParameter(Constants.ADM_PARAM_SYSTEM_LOGIN);
            String password1=p.request.getParameter(Constants.ADM_PARAM_SYSTEM_PASSWORD);
           
            if (Constants.TEST!=null) {
                login1="49";
                password1="catatumba";
            }
            
            p.session.user=Filing.get().users.getUser(login1);
            if (p.session.user!=null) {
                if (SString.equal(password1, p.session.user.password)) {
                    if (p.session.user.hasAccessTo(Users.Permit.RED_ADM)) {
                        error=null;
                    } else {
                        error="Access denied";
                        p.session.user=null;
                    }
                    
                } else {
                    p.session.user=null;
                    error="Invalid login/Password";
                }               
            }
            
            if (SString.isClear(login1)) error="";

            if ((SString.equal(login1,Constants.SYSTEM_LOGIN)) 
                    && (SString.equal(password1,Constants.SYSTEM_PASSWORD))) {
                error=null;
            }
    
            if (Constants.DEBUGGING) {
                //Disable login.
                error=null;
            }             
           
            
        } 
        
        if (login_failure_count>5) error="Login disabled due to repeated failures";
        
        if (error==null) {
            login_failure_count=0;
        } else {
            login_failure_count++;
        }
        
        return error;
    }
//******************************************************************************
    public static void logout(GeneralParams p) {
        getSession(p);
        returnLoginPage(p,null);
    }
//******************************************************************************
    public static void returnLoginPage(GeneralParams p, String loginError) {
        p.response.setContentType("text/html;charset=UTF-8");
        getSession(p);
        PrintWriter out=null;
        try {out = p.response.getWriter(); } catch (Exception e) {}
        try {
            // TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>"+Constants.getVersionSummary()+"</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("INFORTEL CA - "+Constants.getVersionSummary()+" - LOGIN REQUIRED");
            out.println("<br><br>");
            
            if (loginError!=null) {
                out.println(loginError+"<br><br>");
            }
            
            out.println("<form name='input' method='get'>");
            out.println("<table border='1' bgcolor='#cccccc'>");
            out.println("<tr><td>Login:</td><td><input type='text' name='"
                    +Constants.ADM_PARAM_SYSTEM_LOGIN+"'/></td></tr>");
            out.println("<tr><td>Password:</td><td><input type='password' name='"+
                    Constants.ADM_PARAM_SYSTEM_PASSWORD+"'/></td></tr>");
            out.println("</table>");
            out.println("<br><br>");
            out.println("<input type='submit' value='Submit' />");
            out.println("</form>");
            
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }
//******************************************************************************
    public static void returnError(GeneralParams p, String error) {
        p.response.setContentType("text/html;charset=UTF-8");
        getSession(p);
        PrintWriter out=null;
        try {out = p.response.getWriter(); } catch (Exception e) {}
        try {
            // TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>"+Constants.getVersionSummary()+"</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("Error: "+error);
            out.println("<br><br>");
                        
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }
//******************************************************************************
}
