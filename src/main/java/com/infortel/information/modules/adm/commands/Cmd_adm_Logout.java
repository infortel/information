/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infortel.information.modules.adm.commands;

import com.infortel.information.lib.GeneralParams;
import com.infortel.information.lib.Session;
import java.io.PrintWriter;

/**
 *
 * @author leon
 */
public class Cmd_adm_Logout {
    public static final String TITLE="Logout";
    public Cmd_adm_Logout(GeneralParams p, PrintWriter out) {
        Session.logout(p);
    }
}
