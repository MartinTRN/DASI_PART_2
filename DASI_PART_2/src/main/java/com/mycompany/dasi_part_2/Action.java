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
import java.util.List;
import java.util.Objects;
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Employe;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

/**
 *
 * @author mtournier
 */
public class Action {
    
    public static String getRestos() throws Exception
    {
        ServiceMetier sm = new ServiceMetier();
        List <Restaurant> restos = sm.getRestaurantsList();
        
        JsonArray jsonArray = new JsonArray();
        for(Restaurant r : restos)
        {
            JsonObject jsonResto = new JsonObject();
            
            jsonResto.addProperty("id",r.getId());
            jsonResto.addProperty("denomination",r.getDenomination());
            jsonResto.addProperty("description",r.getDescription());
            jsonResto.addProperty("adresse",r.getAdresse());
            
            jsonArray.add(jsonResto);
        }
        
        
        JsonObject racine = new JsonObject();
        racine.add("restaurants",jsonArray);
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

    static String getClientByAdresse(String parameter) {
                ServiceMetier sm = new ServiceMetier();
                                
                System.out.println(parameter);
                
                Client connectClient = sm.connectClient(parameter);
                
                JsonObject racine = new JsonObject();
                racine.addProperty("idClient", connectClient.getId());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;
    }

    static String getLivreurByAdresse(String parameter) {
                ServiceMetier sm = new ServiceMetier();
                
                System.out.println(parameter);
                
                Livreur connectLivreur = sm.connectLivreur(parameter);
                
                JsonObject racine = new JsonObject();
                racine.addProperty("idLivreur", connectLivreur.getId());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;    }

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

    static String submitCommande(String idResto, String idClient, String lProduits) throws Exception {
              
        ServiceMetier sm = new ServiceMetier();
        
        Client client = sm.getClientById(Long.valueOf(idClient));
        
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

    static String cancelCommande(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                
                Commande c = sm.getCommandeById(Long.valueOf(parameter));
                
                sm.annuleCommande(c);
                
                JsonObject racine = new JsonObject();

                racine.addProperty("statut","ok");
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                 String json = gsonOb.toJson(racine);

                return json;    }

    static String getInfoCommandeLivreur(String parameter) throws Exception {
                 ServiceMetier sm = new ServiceMetier();
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                
                Livreur livreur = sm.getLivreurById(Long.valueOf(parameter));
                
                JsonObject obj = new JsonObject();;
                
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

    static String validerLivraisonLivreur(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                                
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                Livreur livreur = sm.getLivreurById(Long.valueOf(parameter));

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
    
}
