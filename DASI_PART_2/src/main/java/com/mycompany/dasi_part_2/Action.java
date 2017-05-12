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
import javax.servlet.http.HttpSession;
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

    static String cancelCommande(String parameter) throws Exception {
                ServiceMetier sm = new ServiceMetier();
                
                Commande c = sm.getCommandeById(Long.valueOf(parameter));
                
                sm.annuleCommande(c);
                
                JsonObject racine = new JsonObject();

                racine.addProperty("statut","ok");
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                 String json = gsonOb.toJson(racine);

                return json;    }

    static String getInfoCommandeLivreur(HttpSession session) throws Exception {
                 ServiceMetier sm = new ServiceMetier();
                List<Commande> livraisonsEnCours = sm.getLivraisonsEnCours();
                
                Livreur livreur = (Livreur)session.getAttribute("livreur");
                
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

    static String getNomPrenomClient(HttpSession session) {
        
                ServiceMetier sm = new ServiceMetier();
                                                                
                Client client = (Client)session.getAttribute("client");
                
                JsonObject racine = new JsonObject();
                racine.addProperty("nomClient", client.getPrenom() + " " + client.getNom());
                
                Gson gsonOb = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonOb.toJson(racine);
                
                return json;
    }

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
    
}
