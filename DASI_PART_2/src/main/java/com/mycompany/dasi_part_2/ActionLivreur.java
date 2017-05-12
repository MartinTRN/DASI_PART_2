/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dasi_part_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.service.ServiceMetier;

/**
 *
 * @author martin
 */
public class ActionLivreur {
        static String getNomPrenomLivreur(HttpSession session) {
                                                                
                Livreur livreur = (Livreur)session.getAttribute("livreur");
                
                JsonObject racine = new JsonObject();
                
                if(livreur instanceof Drone)
                {
                    racine.addProperty("nomLivreur",livreur.getId());
                }
                else
                {
                    Employe e = (Employe)livreur;
                    racine.addProperty("nomLivreur",e.getNom() + " " + e.getPrenom());
                }   
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;    }
        
        
    static String validerLivraisonLivreur(HttpSession session) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                                
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                Livreur livreur = (Livreur)session.getAttribute("livreur");

                for(Commande c : livraisonsEnCours)
                {
                    if(Objects.equals(c.getLivreur().getId(), livreur.getId()))
                    {
                        sm.termineCommande(c);
                    }
                }

                JsonObject racine = new JsonObject();

                racine.addProperty("statut","ok");
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);

                return json;  
    }
    
    
    static String getInfoCommandeLivreur(HttpSession session) throws Exception {
                 ServiceMetier sm = new ServiceMetier();
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                
                Livreur livreur = (Livreur)session.getAttribute("livreur");
                
                JsonObject obj = new JsonObject();
                
                for(Commande c : livraisonsEnCours)
                {
                    if(Objects.equals(c.getLivreur().getId(), livreur.getId()))
                    {
                        obj.addProperty("client", c.getClient().getNom() + " " + c.getClient().getPrenom());
                        obj.addProperty("adresse",c.getClient().getAdresse());
                        obj.addProperty("prix",ServiceMetier.getPrixTot(c));
                        obj.addProperty("poids",sm.getPoids(c.getListeProduit()));
                        
                        JsonArray jsonArray = new JsonArray();
                
                        for(Produit p : c.getListeProduit().keySet())
                        {
                            JsonObject jsonProduit = new JsonObject();

                            jsonProduit.addProperty("denomination",p.getDenomination());
                            jsonProduit.addProperty("nb",c.getListeProduit().get(p));
                            jsonArray.add(jsonProduit);
                        }
                        
                        obj.add("produits", jsonArray);
                        
                        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                        String json = gsonOb.toJson(obj);
                        return json;
                    }
                }
                
                obj.addProperty("vide",0);
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                        String json = gsonOb.toJson(obj);
                        return json;
    }

}
