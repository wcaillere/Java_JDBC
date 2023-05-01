package com.formation.poitiers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

public class MonProgramme {

	public static void main(String[] args) {

		Connection cnx = null;

		// Charge le pilote
		// pilote : classe qui assure les échanges d'information entre l'application et
		// la base (transmet ordres SQL, et remonte les données ou les exceptions

		// Le pilote MYSQL se trouve dans la bibliothèque mysql-connector-java-xx.jar
		// Cette archive jar doit être intégrée dans le Build Path du projet
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Echec chargement pilote");
			System.out.println(e);
			System.exit(5);
		}

		// créer une connexion
		String connection_string = "jdbc:mysql://localhost/etude_jdbc?user=root&password=&noAccessToProcedureBodies=true&useSSL=false";

		try {
			cnx = DriverManager.getConnection(connection_string);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Echec création connexion");
			System.out.println(e);
			System.exit(10);
		}

		// créer le cadre nécessaire pour passer des ordres SQL (éxecuter des requêtes)
		Statement stmt = null;

		try {
			stmt = cnx.createStatement();
		} catch (SQLException ex) {
			System.out.println("Echec création statement");
			System.out.println(ex.getMessage());
			System.exit(15);
		}

		// sélectionner les articles de la table Personnes
		ResultSet rs = null;

		try {
			// rs = stmt.executeQuery("select * from Personnes");
			rs = stmt.executeQuery(
					"select IdPersonne, Nom as \"Nom personne\", Prenom as \"Prenom personne\", Adresse, Cp, Ville, Telephone, Email, DateNaissance from Personnes");
		} catch (SQLException e) {
			System.out.println("Echec création ResultSet");
			System.out.println(e.getMessage());
			System.exit(20);
		}

		// Dans le jeu de résultat retourné on ne trouve pas que les données
		// sélectionnées mais aussi des infos sur ces données (des méta-données)
		try {
			// récupérons le nom de la base d'où vient une certaine colonne

			// Comme toutes les colonnes viennent de la même base de données,
			// il suffit de trouver la base d'où vient la première
			String nom_base = rs.getMetaData().getCatalogName(1);
			System.out.println("Le nom de la base de données : " + nom_base);

			// récupérons le nom de la table

			// Comme toutes les colonnes viennent de la même table,
			// il suffit de trouver la table d'où vient la première
			System.out.println("Le nom de la table : " + rs.getMetaData().getTableName(1));

			// récupérer le nombre de colonnes, leur nom, taille, ...
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				System.out.println("Colonne " + i + ", nom : " + rs.getMetaData().getColumnName(i) + ", alias : "
						+ rs.getMetaData().getColumnLabel(i) + ", type SQL : " + rs.getMetaData().getColumnTypeName(i)
						+ ", taille : " + rs.getMetaData().getColumnDisplaySize(i));
			}

		} catch (SQLException e) {
			System.out.println("Echec lecture métadonnées");
			System.out.println(e.getMessage());
			System.exit(25);
		}

		// Lire les données
		System.out.println("\nLes données sélectionnées :");

		// Je récupère les données avec la méthode next() du ResultSet.
		// l'objet ResultSet pointe juste avant la 1ere ligne
		// Donc pour arriver sur premiere => il faut faire un next()
		// Le rs NE CONTIENT PAS toutes les lignes sélectionnées,
		// se comporte comme un curseur SQL, in ne contient que la ligne courante
		// Next() retourne true si autre ligne après la ligne courante
		try {
			while (rs.next()) {
				// 2 façons de récupérer les données d'une colonne
				// - par la position (ordinal) de la colonne
				// - par le nom de la colonne

				// Par position (rappel : commence par 1)
				int id = rs.getInt(1);

				// Par le nom
				String nom = rs.getString("nom personne");
				String prenom = rs.getString("prenom personne");
				String adresse = rs.getString("Adresse");
				String CP = rs.getString("Cp");
				String ville = rs.getString("Ville");
				String telephone = rs.getString("Telephone");
				String email = rs.getString("Email");
				Date dateNaissance = rs.getDate("DateNaissance");

				System.out.print("IdPersonne : " + id);
				System.out.print(", nom : " + nom);
				System.out.print(", prenom : " + prenom);
				System.out.print(", adresse : " + adresse);
				System.out.print(", CP : " + CP);
				System.out.print(", ville : " + ville);
				System.out.print(", telephone : " + telephone);
				System.out.print(", email : " + email);
				System.out.print(", dateNaissance : " + dateNaissance);
				System.out.println("");

			}
		} catch (SQLException e) {
			System.out.println("Echec lecture ResultSet");
			System.out.println(e.getMessage());
			System.exit(30);
		}

		// fermer le ResultSet
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			System.out.println("Echec close ResultSet");
			System.out.println(e.getMessage());
			System.exit(35);
		}

		String rqt;

		// delete
		rqt = "delete from Personnes where Nom like 'lenom%' and Prenom like 'leprenom%'";

		try {
			int nb = stmt.executeUpdate(rqt);
			System.out.println("Nb lignes supprimées : " + nb);
		} catch (SQLException e) {
			System.out.println("Echec delete");
			System.out.println(e.getMessage());
			System.exit(40);
		}

		// insérer :
		/*
		 * rqt =
		 * "insert into Personnes (nom, Prenom) values ('lenom1', 'leprenom1'), ('lenom2', 'leprenom2')"
		 * ;
		 * 
		 * try { int nb = stmt.executeUpdate(rqt);
		 * System.out.println("Nb lignes insérées : " + nb); } catch (SQLException e) {
		 * System.out.println("Echec insert"); System.out.println(e.getMessage());
		 * System.exit(45); }
		 */

		// update :
		rqt = "update Personnes set Nom=concat(Nom, 'xx'), Prenom=concat(Prenom, 'yy') WHERE Nom = 'lenom2' AND Prenom= 'leprenom2'";

		try {
			int nb = stmt.executeUpdate(rqt);
			System.out.println("Nb lignes modifiées : " + nb);
		} catch (SQLException e) {
			System.out.println("Echec modification");
			System.out.println(e.getMessage());
			System.exit(50);
		}

		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
		}

		// CallableStatement
		// Interface utilisée pour appeler les procédures stockées et les fonctions

		// Avec une fonction
		CallableStatement callableStmt = null;
		int idInsertion = 0;

		try {
			callableStmt = cnx.prepareCall("{? = call get_id_insertion()}");
			// où ? représente un paramètre doit être donné avt execution requête
			// => requête paramétrée
			// System.out.println("\n" + callableStmt);

			// Configurer la requête paramétrée
			callableStmt.registerOutParameter(1, Types.INTEGER);

			// éxecuter les callableStmt
			callableStmt.execute();

			// récupérer la valeur retournée
			idInsertion = callableStmt.getInt(1);
			System.out.println("l'Id de la dernière insertion est " + idInsertion);

		} catch (SQLException e) {
			System.out.println("Echec création CallableStmt (fonction)");
			System.out.println(e.getMessage());
			System.exit(55);
		}

		// fermer le callableStmt
		try {
			if (callableStmt != null)
				callableStmt.close();
		} catch (SQLException e) {
		}

		// Avec une procédure stockée
		try {
			callableStmt = cnx.prepareCall("call modifier_telephone_email(?, ?, ?)");

			// Préciser les valeurs des paramètres (par leur position)
			callableStmt.setInt(1, idInsertion);
			callableStmt.setString(2, "1111112222");
			callableStmt.setString(3, "email" + idInsertion + "@gmail.com");

			callableStmt.execute();

			// fermer le callable statement
			try {
				if (callableStmt != null)
					callableStmt.close();
			} catch (SQLException e) {
			}

		} catch (SQLException e) {
			System.out.println("Echec création CallableStmt (procédure stockée)");
			System.out.println(e.getMessage());
			System.exit(60);
		}

		/*
		 * L'interface PreparedStatement hérite Statement et on l'utilise pour des
		 * ordres paramétrés. Cette interface diffère de Statement sur 2 points : Les
		 * instances de PreparedStatement contiennent une instruction SQL déjà compilée
		 * (d'où le terme prepared), et les instructions SQL d'un objet
		 * PreparedStatement contiennent un ou plusieurs paramètres d'entrée, qui ne
		 * sont pas spécifiés lors de la création de l'instruction. Ces paramètres sont
		 * représentés par des points d'interrogation. Ils doivent être spécifiés avant
		 * l'exécution de l'ordre SQL.
		 */

		rqt = "insert into Personnes (Nom, Prenom) values (?, ?)";

		PreparedStatement prepStmt = null;

		try {
			prepStmt = cnx.prepareStatement(rqt);
		} catch (SQLException e) {
			System.out.println("Echec PreparedStatement");
			System.out.println(e.getMessage());
			System.exit(65);
		}

		// Passer les paramètres et éxecuter le PreparedStatement
		try {
			for (int i = 1; i <= 3; i++) {
				prepStmt.setString(1, "lenom" + i);
				prepStmt.setString(2, "leprenom" + i);
				prepStmt.executeUpdate();
			}

			prepStmt.close();

		} catch (Exception e) {
			System.out.println("Echec boucle paramètres");
			System.out.println(e.getMessage());
			System.exit(70);
		}

		// Refaire la même chose mais en insérant les lignes en 1 seule requête
		int nbInsertions = 10;
		rqt = "insert into Personnes (Nom, Prenom, email) values ";
		// Créer la partie contenant les valeurs à l'aide d'une bouble
		for (int i = 0; i < nbInsertions; i++) {
			if (i > 0) {
				rqt += ", ";
			}
			rqt += "(?, ?, ?)";
		}

		System.out.println(rqt);
		// Injecter les valeurs des paramètres et executer la requête
		try {
			prepStmt = cnx.prepareStatement(rqt);

			int noArgument = 1;
			for (int i = 1; i <= nbInsertions; i++) {
				prepStmt.setString(noArgument++, "lenom" + (100 + i));
				prepStmt.setString(noArgument++, "leprenom" + (100 + i));
				prepStmt.setString(noArgument++, "email" + (100 + i));
			}

			int nbLignes = prepStmt.executeUpdate();
			System.out.println(nbLignes + " ont été insérées");

			prepStmt.close();

		} catch (SQLException e) {
			System.out.println("Echec PreparedStatement2");
			System.out.println(e.getMessage());
			System.exit(75);
		}

		// Transactions

		// Par défaut, les connexions ouvertes sont en mode auto-commit
		// => chaque instruction SQL est considérée comme transaction indépendante
		// et est commitée (validée) après son exécution.

		// Pour assurer un env transctionnel, on doit gérer nous-même les transactions.
		try {
			cnx.setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println("Echec setAutocommit");
			System.out.println(e.getMessage());
			System.exit(80);
		}

		// rappel : l'ancien statement a été fermé (mis à null pour ne pas travailler
		// par erreur avec un statement fermé)
		stmt = null;

		// Création d'un nouveau statement
		try {
			stmt = cnx.createStatement();
		} catch (SQLException e) {
			System.out.println("Echec création Statement pour env transactionnel");
			System.out.println(e.getMessage());
			System.exit(85);
		}

		// Exécuter les 3 requêtes suivantes dont la dernière ne passera pas

		try {
			// Supprimer tous les articles de la table Personnes
			rqt = "delete FROM Personnes";
			stmt.executeUpdate(rqt);

			// Insérer un article
			rqt = "insert into Personnes (Nom, Prenom) values ('lenom200', 'leprenom200')";
			stmt.executeUpdate(rqt);

			// Insérer ce même article une deuxième fois => provoquera erreur
			// Prochain commentaire = tout passe bien donc transaction marche
			// rqt = "insert into Personnes (Nom, Prenom) values ('lenom200_',
			// 'leprenom200')";
			stmt.executeUpdate(rqt);

			// Si tout va bien (pas d'exeption), je comite l'état final
			cnx.commit();

		} catch (Exception e) {
			System.out.println("Echec transaction");
			System.out.println(e.getMessage());
			// retrouver l'état initiale de la base avec rollback()
			try {
				cnx.rollback();
				System.out.println("La transaction a été annulée");
			} catch (SQLException e1) {
				System.out.println("Echec rollback");
				System.out.println(e1.getMessage());
				System.exit(90);
			}
		}

		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
		}

		// Fermer la connexion à la base
		try {
			cnx.close();
		} catch (SQLException e) {
		}

		System.out.println("Fin exécution");
	}

}

/*
 * Exercice
 */
