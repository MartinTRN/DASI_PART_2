package metier.modele;

import java.util.Date;
import java.util.HashMap;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import metier.modele.Client;
import metier.modele.Livreur;
import metier.modele.Restaurant;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:59:03")
@StaticMetamodel(Commande.class)
public class Commande_ { 

    public static volatile SingularAttribute<Commande, Restaurant> restaurant;
    public static volatile SingularAttribute<Commande, Double> duree;
    public static volatile SingularAttribute<Commande, Client> client;
    public static volatile SingularAttribute<Commande, Long> id;
    public static volatile SingularAttribute<Commande, Date> dateCommande;
    public static volatile SingularAttribute<Commande, Livreur> livreur;
    public static volatile SingularAttribute<Commande, Integer> etat;
    public static volatile SingularAttribute<Commande, HashMap> listeProduit;
    public static volatile SingularAttribute<Commande, Date> dateReception;

}