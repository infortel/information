/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infortel.information.modules.adm.entry;

import com.infortel.information.lib.Forwarding_Data;
import com.infortel.information.modules.adm.entry.Adm;
import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Constants;
import com.infortel.information.lib.Session;
import com.infortel.information.lib.Users;
import com.infortel.slibrary.SGeneral;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author leon
 */
//@WebServlet(name = "Entry", urlPatterns = {"/adm", "/adm/"})
@WebServlet(name = "Entry", urlPatterns = {"/adm"})
public class Entry extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        GeneralParams p=new GeneralParams();
        try {
            p.request=(HttpServletRequest)request;
            p.response=(HttpServletResponse)response;
            p.uri=p.request.getRequestURI();
            if (Constants.FLAGS_ON) System.out.println("******************** Entry uri="+p.uri);
            //Initialize the first time
            if (Users.self==null) Users.self=new Users(p);
            if (Forwarding_Data.self==null) Forwarding_Data.self=new Forwarding_Data(p);

            String loginError=Session.verifyLogin(p);
            if (loginError==null) {
                new Adm(p);
            } else Session.returnLoginPage(p,loginError);
        } catch (Throwable e) {
            String error=SGeneral.exceptionToString(e);
            p.session.returnError(p,error);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
