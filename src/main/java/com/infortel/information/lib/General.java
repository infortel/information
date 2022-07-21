/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.lib;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;

/**
 *
 * @author leon
 */
public class General {
//******************************************************************************
    public static void sendMessage(GeneralParams p, String message) throws ServletException, IOException {
        p.response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = p.response.getWriter();
        try {
            // TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>"+Constants.getVersionSummary()+"</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>"+Constants.getVersionSummary()+"</h1><br><br>");
            out.println("<h1>"+message+"</h1>");
            out.println("</body>");
            out.println("</html>");

        } finally {
            out.close();
        }
    }
//******************************************************************************
    public static void sendMessage0(GeneralParams p, String message) {
        try {
            sendMessage(p,message);

        } catch (Exception e) {
            
        }
    }
//******************************************************************************
}
