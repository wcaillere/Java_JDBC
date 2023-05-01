package com.formation.poitiers;

import java.sql.Connection;
import java.sql.DriverManager;

// Exercice
//	- afficher employés avec pays, societes, pays de la societe, etc
//	- insérer un pays
//	- Insérer une nouvelle société dont le pays est le pays inséré
//	- Insérer un pays
//	- Insérer un nouvel employe dans le nouveau pays et la société
//	- Modifier cet employé
//	- Supprimer le dernier employé inséré, avant l'insertion
//	- Définir une procédure stockée capable de modifier le salaire et responsable d'un employe
//		-> Demandera de relancer le script, puis utiliser cette procédure dans le code
//	- Insérer 3 employés d'un coup avec une requête paramétrée
//	- insérer dans un env transactionnel un new pays, une new société, et un new employé

public class Exercice {

	public static void main(String[] args) {
		Connection cnx = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Erreur chargement du driver");
			System.out.println(e.getMessage());
			System.exit(5);
		}

		String connection_string = "jdbc:mysql://localhost/maBase?user=root&password=&noAccessToProcedureBodies=true&useSSL=false";

		try {
			cnx = DriverManager.getConnection(connection_string);
		} catch (Exception e) {
			System.out.println("Erreur création connexion");
			System.out.println(e.getMessage());
			System.exit(10);
		}
	}

}
