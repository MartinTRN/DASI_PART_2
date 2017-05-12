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
import metier.modele.Client;
import metier.modele.Commande;
import metier.modele.Drone;
import metier.modele.Livreur;
import metier.modele.Produit;
import metier.modele.Restaurant;
import metier.service.ServiceMetier;

/**
 *
 * @author martin
 */
public class ActionGestionnaire {
    static String validerLivraisonDrone(String parameter) throws Exception {
        System.out.println("des");
        
        ServiceMetier sm = new ServiceMetier();
                
        System.out.println(parameter);
        
        Commande c = sm.getCommandeById(Long.valueOf(parameter));
        
        sm.termineCommande(c);
        
        JsonObject racine = new JsonObject();

        racine.addProperty("idCommande",c.getId());
                
       Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
       String json = gsonOb.toJson(racine);
        
        return json;
    }
    
    
    static String recupererCommandesDrones() throws Exception {
                ServiceMetier sm = new ServiceMetier();
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                
                JsonArray tabCommandes = new JsonArray();
                
                if(livraisonsEnCours==null)
                {      
                    JsonObject racine = new JsonObject();
                    racine.add("commandes", tabCommandes);

                    Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                    String json = gsonOb.toJson(racine);
                    return json;
                
                }          
                for(Commande c : livraisonsEnCours)
                {
                    if(c.getLivreur() instanceof Drone)
                    {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("client", c.getClient().getNom() + " " + c.getClient().getPrenom());
                        obj.addProperty("adresse",c.getClient().getAdresse());
                        obj.addProperty("prix",ServiceMetier.getPrixTot(c));
                        obj.addProperty("id",c.getLivreur().getId());
                        obj.addProperty("idCommande",c.getId());
                        JsonArray jsonArray = new JsonArray();
                
                        for(Produit p : c.getListeProduit().keySet())
                        {
                            JsonObject jsonProduit = new JsonObject();

                            jsonProduit.addProperty("denomination",p.getDenomination());
                            jsonProduit.addProperty("nb",c.getListeProduit().get(p));
                            jsonArray.add(jsonProduit);
                        }
                        
                        obj.add("produits", jsonArray);
                        tabCommandes.add(obj);

                    }
                }
                
                JsonObject racine = new JsonObject();
                racine.add("commandes", tabCommandes);
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                return json;
    }
    
       public static String getClients() throws Exception
    {
        ServiceMetier sm = new ServiceMetier();
        List <Client> clients = sm.getClientsList();
        
        JsonArray jsonArray = new JsonArray();
        for(Client r : clients)
        {
            JsonObject jsonClients = new JsonObject();
            
            jsonClients.addProperty("id",r.getId());
            jsonClients.addProperty("nom",r.getNom() + " " + r.getPrenom());
            jsonClients.addProperty("mail",r.getMail());
            jsonClients.addProperty("adresse",r.getAdresse());
            jsonClients.addProperty("latitude",r.getLatitude());
            jsonClients.addProperty("longitude",r.getLongitude());
            
            jsonArray.add(jsonClients);
        }
        
        
        JsonObject racine = new JsonObject();
        racine.add("clients",jsonArray);
        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonOb.toJson(racine);
                    
       return json;
    }
       
               static String getItineraire(String idLivreur) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                Livreur livreur = sm.getLivreurById(Long.valueOf(idLivreur));
                Restaurant resto = livreur.getCommandeEnCours().getRestaurant();
                Client client = livreur.getCommandeEnCours().getClient();
                
                JsonArray jsonArray = new JsonArray();
                
                JsonObject jsonIti = new JsonObject();

                jsonIti.addProperty("id",livreur.getId());
                jsonIti.addProperty("latitudeResto",resto.getLatitude());
                jsonIti.addProperty("longitudeResto",resto.getLongitude());
                jsonIti.addProperty("latitudeClient",client.getLatitude());
                jsonIti.addProperty("longitudeClient",client.getLongitude());
                jsonIti.addProperty("latitudeLivreur",livreur.getLatitude());
                jsonIti.addProperty("longitudeLivreur",livreur.getLongitude());
                jsonArray.add(jsonIti);
                
                
                JsonObject racine = new JsonObject();
                racine.add("itineraire",jsonArray);
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;  
    }
    
                   
    public static String getLivreurs() throws Exception
    {
        ServiceMetier sm = new ServiceMetier();
        List <Livreur> livreurs = sm.getLivreurList();
        
        JsonArray jsonArray = new JsonArray();
        for(Livreur r : livreurs)
        {
            JsonObject jsonLivreurs = new JsonObject();
            
            jsonLivreurs.addProperty("id",r.getId());
            jsonLivreurs.addProperty("latitude",r.getLatitude());
            jsonLivreurs.addProperty("longitude",r.getLongitude());
            
            jsonArray.add(jsonLivreurs);
        }
        
        
        JsonObject racine = new JsonObject();
        racine.add("livreurs",jsonArray);
        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonOb.toJson(racine);
                    
       return json;
    }
    
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
            jsonResto.addProperty("latitude",r.getLatitude());
            jsonResto.addProperty("longitude",r.getLongitude());
            
            jsonArray.add(jsonResto);
        }
        
        
        JsonObject racine = new JsonObject();
        racine.add("restaurants",jsonArray);
        Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
        String json = gsonOb.toJson(racine);
                    
       return json;
    }
}
