/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dasi_part_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;
import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

/**
 *
 * @author martin
 */
public class ActionClient {
    
    
    static String submitCommande(String idResto, HttpSession session, String lProduits) throws Exception {
              
        ServiceMetier sm = new ServiceMetier();
        
        Client client = (Client)session.getAttribute("client");
        
        Restaurant resto = sm.getRestaurantById(Long.valueOf(idResto));
        
        
        HashMap<Produit, Integer> hm = new HashMap<>();
        
        System.out.println("OKKKKKKKKKKKKK " + lProduits);
        
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(lProduits).getAsJsonObject();
        
        JsonArray array = obj.getAsJsonArray("array");
        
        for(JsonElement json : array)
        {
            JsonElement id = json.getAsJsonObject().get("id");
            
            Produit p = sm.getProduitById(id.getAsLong());
            
            int nb = json.getAsJsonObject().get("nb").getAsInt();
                        
            hm.put(p,nb);
            
        }
                
        Commande submitCommande = sm.submitCommande(hm, client, resto);
        
        JsonObject racine = new JsonObject();
        
        if(submitCommande!=null)
            racine.addProperty("idCom",submitCommande.getId());
        else
           racine.addProperty("idCom",-1);
        
        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonOb.toJson(racine);

        return json;
    }
    
    static String getInfoSurCommande(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                Commande com = sm.getCommandeById(Long.valueOf(parameter));
                
                JsonObject jsonResto = new JsonObject();
            
                jsonResto.addProperty("denomination",com.getRestaurant().getDenomination());
                jsonResto.addProperty("description",com.getRestaurant().getDescription());
                jsonResto.addProperty("adresse",com.getRestaurant().getAdresse());     
                
                
                
                JsonArray jsonArray = new JsonArray();
                
                for(Produit p : com.getListeProduit().keySet())
                {
                    JsonObject jsonProduit = new JsonObject();

                    jsonProduit.addProperty("id",p.getId());
                    jsonProduit.addProperty("denomination",p.getDenomination());
                    jsonProduit.addProperty("description",p.getDescription());
                    jsonProduit.addProperty("prix",p.getPrix());
                    jsonProduit.addProperty("nb",com.getListeProduit().get(p));
                    
                    
                    jsonArray.add(jsonProduit);
                }
                
                JsonObject jsonLivraison = new JsonObject();
                jsonLivraison.addProperty("poids",sm.getPoids(com.getListeProduit()));
                jsonLivraison.addProperty("temps",com.getDuree());
                               
                if(com.getLivreur() instanceof Drone)
                {
                    jsonLivraison.addProperty("livreur",com.getLivreur().getId());
                }
                else
                {
                    Employe e = (Employe) com.getLivreur();
                    jsonLivraison.addProperty("livreur",e.getNom() + " " + e.getPrenom());
                }   
                 
                        
                jsonLivraison.addProperty(parameter, Boolean.FALSE);


                JsonObject racine = new JsonObject();
                racine.add("restaurant",jsonResto);
                racine.add("produits",jsonArray);
                racine.add("livraison",jsonLivraison);
                
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);

               return json;    }

    static String confirmCommande(String parameter) throws Exception {

                ServiceMetier sm = new ServiceMetier();
                
                Commande c = sm.getCommandeById(Long.valueOf(parameter));
                
                sm.confirmCommande(c);
                
                JsonObject racine = new JsonObject();

                
                if(c!=null)
                    racine.addProperty("idCom",c.getId());
                 else
                    racine.addProperty("idCom",-1);
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                 String json = gsonOb.toJson(racine);

                return json;
    }
    
    
    static String menuResto(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                Restaurant resto = sm.getRestaurantById(Long.valueOf(parameter));
                
                JsonArray jsonArray = new JsonArray();
                for(Produit p : resto.getProduits())
                {
                    JsonObject jsonResto = new JsonObject();

                    jsonResto.addProperty("id",p.getId());
                    jsonResto.addProperty("denomination",p.getDenomination());
                    jsonResto.addProperty("description",p.getDescription());
                    jsonResto.addProperty("prix",p.getPrix());
                    jsonArray.add(jsonResto);
                }


                JsonObject racine = new JsonObject();
                racine.add("produits",jsonArray);
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);

               return json;
    }


    

    static String cancelCommande(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                
                Commande c = sm.getCommandeById(Long.valueOf(parameter));
                
                sm.annuleCommande(c);
                
                JsonObject racine = new JsonObject();

                racine.addProperty("statut","ok");
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                 String json = gsonOb.toJson(racine);

                return json;    }
    
        static String getNomPrenomClient(HttpSession session) {
        
                ServiceMetier sm = new ServiceMetier();
                                                                
                Client client = (Client)session.getAttribute("client");
                
                JsonObject racine = new JsonObject();
                racine.addProperty("nomClient", client.getPrenom() + " " + client.getNom());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;
    }
        
          static String getInfoSurResto(String parameter) throws Exception {
        ServiceMetier sm = new ServiceMetier();
        Restaurant res = sm.getRestaurantById(Long.valueOf(parameter));
        
        JsonObject jsonResto = new JsonObject();
            
        jsonResto.addProperty("denomination",res.getDenomination());
        jsonResto.addProperty("description",res.getDescription());
        jsonResto.addProperty("adresse",res.getAdresse());
            
        
        
        JsonObject racine = new JsonObject();
        racine.add("restaurants",jsonResto);
        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonOb.toJson(racine);
                    
       return json;
    }
}
