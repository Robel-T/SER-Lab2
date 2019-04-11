package ch.heigvd.ser.labo2;

import ch.heigvd.ser.labo2.coups.Case;
import ch.heigvd.ser.labo2.coups.Deplacement;
import ch.heigvd.ser.labo2.coups.TypePiece;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class JDOM_Read {

    public static void main(String ... args) {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        PrintWriter pw;

        {
            try {
                doc = builder.build(new File("tournois_fse.xml"));
                Element rootElement = doc.getRootElement();

                Element tournois = rootElement.getChild("tournois");
                Element joueurs =  rootElement.getChild("joueurs");
                Element arbitres = rootElement.getChild("arbitres");

                List ListeTournoi = tournois.getChildren("tournoi");
                List ListeJoueur = joueurs.getChildren("joueur");
                List ListeArbitre = arbitres.getChildren("arbitre");


                for (int iTournoi = 0; iTournoi < ListeTournoi.size(); ++iTournoi) {

                    Element parties =((Element) ListeTournoi.get(iTournoi)).getChild("parties");

                    List ListePartie = parties.getChildren("partie");
                    for (int iPartie = 0; iPartie < ListePartie.size(); ++iPartie) {
                        pw = new PrintWriter(new FileWriter("src/Partie" + (iPartie+1) + "-tournoi" + (iTournoi+1) + ".pgn"));

                        Element coups = ((Element) ListePartie.get(iPartie)).getChild("coups");

                        List ListeCoup = coups.getChildren("coup");

                        for (int iCoups = 0; iCoups < ListeCoup.size(); ++iCoups) {

                            Element deplacement = ((Element)ListeCoup.get(iCoups)).getChild("deplacement");
/*                            String piece = deplacement.getAttributeValue("piece");
                            String case_arrivee = deplacement.getAttributeValue("case_arrivee");

                            char colonne = case_arrivee.charAt(0);
                            char ligne = case_arrivee.charAt(1);*/
                            pw.println(iCoups+1);
                            // Les fichiers parties sont créer avec les numeros de coups, il faut maintenent faire
                            // afficher le reste

                            /*
                            if (piece.equals(TypePiece.Pion.toString())){
                                Case depart = new Case(colonne, ligne);

                            }
                            pw.println((iCoups+1) + depart.notationPGN());
*/
                            // System.out.println(depart.notationPGN());
                            //Case arrivee = new Case(c, i);

                            //Deplacement deplacement = new Deplacement(pieceDeplacee, elimination, null, null, depart, arrivee);







                        }
                        pw.close();


                    }



               /* System.out.println("nom    : "  + menu.getChildText("nom"));
                System.out.println("pos    : "  + menu.getAttributeValue("position"));

                List listOption = menu.getChildren("option");
                for (int iOption = 0; iOption < listOption.size(); ++iOption) {
                    Element option = (Element) listOption.get(iOption);
                    System.out.println("option : "  + option.getText());
                }*/
                }


            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    private String pieceToNotation(TypePiece pieceDeplacee) {

        switch (pieceDeplacee) {

            case Fou:
                return "B";
            case Cavalier:
                return "N";
            case Dame:
                return "Q";
            case Roi:
                return "K";
            case Pion:
                return "";
            case Tour:
                return "R";

        }

        return null;

    }
}
