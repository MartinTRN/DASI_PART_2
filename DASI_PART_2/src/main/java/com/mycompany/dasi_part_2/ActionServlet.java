    /*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.mycompany.dasi_part_2;

import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mtournier
 */
@WebServlet(name="ActionServlet",urlPatterns={"/ActionServlet"})
public class ActionServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //super.service(req, resp);
        
        System.out.println("FEFSEFESFSE");
        
        try {
            resp.setContentType("application/json");
            
            String json = null;
            
            switch (req.getParameter("action"))
            {
                case("listRestos") :
                    json = Action.getRestos();
                    break;
                    
                case("listeMenu") :
                    json = Action.menuResto(req.getParameter("idResto"));
                    break;    
                    
                    
                case("recupInfosResto") :
                    json = Action.getInfoSurResto(req.getParameter("idResto"));
                    break; 
                 
                case("recupClient") :
                    json = Action.getClientByAdresse(req.getParameter("adresse"),req.getSession());
                    break;
                    
                case("recupNomPrenom") :
                   json = Action.getNomPrenomClient(req.getSession());
                    break;
                    
                case("recupNomPrenomLivreur") :
                   json = Action.getNomPrenomLivreur(req.getSession());
                    break;
                    
                case("recupLivreur") :
                    json = Action.getLivreurByAdresse(req.getParameter("adresse"),req.getSession());
                    break; 
                    
                case("submitCommande") :
                    json = Action.submitCommande(req.getParameter("idResto"),req.getSession(),req.getParameter("produit"));
                    break;   
                    
                case("getInfosCommande") :
                    json = Action.getInfoSurCommande(req.getParameter("idCommande"));
                    break;
                    
                case("confirmCommande") :
                    json = Action.confirmCommande(req.getParameter("idCommande"));
                break;
                
                case("cancelCommande") :
                    json = Action.cancelCommande(req.getParameter("idCommande"));
                break;
                
                case("recupCommandeLivreur") :
                    json = Action.getInfoCommandeLivreur(req.getSession());
                break;
                
                case("confirmerLivraisonLivreur") :
                    json = Action.validerLivraisonLivreur(req.getSession());
                break;
                
                default: 
                    break;
            }
            
            PrintWriter out = resp.getWriter();
            out.println(json);
            //out.println("test22");
            
            
        }   catch (Exception ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        JpaUtil.init();
    }
    
     @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        JpaUtil.destroy();
    }
    
    
}
