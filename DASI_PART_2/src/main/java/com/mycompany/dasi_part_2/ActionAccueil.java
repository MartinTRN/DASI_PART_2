/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dasi_part_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Livreur;
import metier.service.ServiceMetier;

/**
 *
 * @author martin
 */
public class ActionAccueil {
    
    static String getLivreurByAdresse(String parameter,HttpSession session) {
                ServiceMetier sm = new ServiceMetier();
                
                System.out.println(parameter);
                
                Livreur connectLivreur = sm.connectLivreur(parameter);
                
                session.setAttribute("livreur", connectLivreur);
                
                JsonObject racine = new JsonObject();
                racine.addProperty("idLivreur", connectLivreur.getId());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;    }
    
     static String inscrireUtilsiateur(String adresse, String nom, String prenom, String mail) throws Exception {

                ServiceMetier sm = new ServiceMetier();
                
                Client c = sm.submitSubscription(nom, prenom, mail, adresse);
                
                JsonObject racine = new JsonObject();

                
                if(c!=null)
                    racine.addProperty("idClient",c.getId());
                 else
                    racine.addProperty("idClient",-1);
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);

                return json;
       }
     
         static String getClientByAdresse(String parameter,HttpSession session) {
                ServiceMetier sm = new ServiceMetier();
                                
                System.out.println(parameter);
                
                Client connectClient = sm.connectClient(parameter);
                
                session.setAttribute("client", connectClient);
                
                JsonObject racine = new JsonObject();
                racine.addProperty("idClient", connectClient.getId());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;
    }
    
}
