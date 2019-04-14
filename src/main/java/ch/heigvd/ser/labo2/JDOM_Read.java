package ch.heigvd.ser.labo2;

import ch.heigvd.ser.labo2.coups.*;
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

    SAXBuilder builder = new SAXBuilder();
    Document doc;
    PrintWriter pw;

    public void read() {
        try {
            doc = builder.build(new File("tournois_fse.xml"));
            Element rootElement = doc.getRootElement();

            Element tournois = rootElement.getChild("tournois");
            //Element joueurs = rootElement.getChild("joueurs");
            Element arbitres = rootElement.getChild("arbitres");

            List ListeTournoi = tournois.getChildren("tournoi");
            //List ListeJoueur = joueurs.getChildren("joueur");
            //List ListeArbitre = arbitres.getChildren("arbitre");


            for (int iTournoi = 0; iTournoi < ListeTournoi.size(); ++iTournoi) {

                System.out.println("\nTournoi" + (iTournoi + 1));
                Element parties = ((Element) ListeTournoi.get(iTournoi)).getChild("parties");

                List ListePartie = parties.getChildren("partie");
                for (int iPartie = 0; iPartie < ListePartie.size(); ++iPartie) {
                    System.out.println("\nPartie" + (iPartie + 1) + "\n");
                    System.out.printf("_______________________________\n\n");

                    pw = new PrintWriter(new FileWriter("src/Partie" + (iPartie + 1) + "-tournoi" + (iTournoi + 1) + ".pgn"));

                    Element coups = ((Element) ListePartie.get(iPartie)).getChild("coups");

                    List ListeCoup = coups.getChildren("coup");


                    String tour = "";
                    int i = 1;
                    int tourcoups = 1;
                    for (int iCoups = 0; iCoups < ListeCoup.size(); ++iCoups) {
                        String typeRoque      = "";
                        String piece          = "";
                        String case_arrivee   = "";
                        String case_depart    = "";
                        String e              = "";
                        String promo          = "";
                        TypeRoque tr          = null;
                        CoupSpecial cp;
                        TypePiece p           = null;
                        TypePiece elimination = null;
                        TypePiece promotion   = null;
                        Case depart           = null;
                        Case arrivee          = null;

                        Element deplacement = ((Element) ListeCoup.get(iCoups)).getChild("deplacement");
                        Element roque = ((Element) ListeCoup.get(iCoups)).getChild("roque");

                        String coupSpecial = ((Element) ListeCoup.get(iCoups)).getAttributeValue("coup_special");
                        if (coupSpecial == null) {
                            coupSpecial = "";
                        }
                        cp = toSpecial(coupSpecial);

                        if (roque != null) {
                            typeRoque = roque.getAttributeValue("type");
                            tr = sizeRoque(typeRoque);
                        }

                        if (deplacement != null) {
                            piece = deplacement.getAttributeValue("piece");
                            case_arrivee = deplacement.getAttributeValue("case_arrivee");
                            case_depart = deplacement.getAttributeValue("case_depart");
                            e = deplacement.getAttributeValue("elimination");

                            if(case_depart != null){
                                char cD = case_depart.charAt(0);
                                char lD = case_depart.charAt(1);
                                depart = new Case(cD, lD - 48);
                            }

                            if (e == null) {
                                e = "";
                            }
                            elimination = pieceToNotation(e);


                            char cA = case_arrivee.charAt(0);
                            char lA = case_arrivee.charAt(1);
                            arrivee = new Case(cA, lA - 48);

                            promo = deplacement.getAttributeValue("promotion");

                            if (promo == null) {
                                promo = "";
                            }
                            promotion = pieceToNotation(promo);
                            p = pieceToNotation(piece);
                        }

                        if (tr != null) {
                            Roque rq = new Roque(cp, tr);
                            tour += rq.notationPGN();
                        } else {
                            Deplacement dp = new Deplacement(p, elimination, promotion, cp, depart, arrivee);
                            tour += dp.notationPGN();
                        }

                        if (i % 2 == 0 || i == (ListeCoup.size())) {
                            tour = tourcoups + " " + tour;
                            System.out.println(tour);
                            pw.println(tour);
                            tour = "";
                            tourcoups++;
                        } else {
                            tour += " ";
                        }

                        i++;
                    }
                    pw.println(tour);
                    pw.close();


                }

            }


        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private TypePiece pieceToNotation(String s) {

        if (s.equals("Fou"))
            return TypePiece.Fou;
        else if (s.equals("Cavalier"))
            return TypePiece.Cavalier;
        else if (s.equals("Dame"))
            return TypePiece.Dame;
        else if (s.equals("Roi"))
            return TypePiece.Roi;
        else if (s.equals("Pion"))
            return TypePiece.Pion;
        else if (s.equals("Tour"))
            return TypePiece.Tour;
        else
            return null;

    }

    private TypeRoque sizeRoque(String s) {

        if (s.equals("petit_roque"))
            return TypeRoque.PETIT;
        else if (s.equals("grand_roque"))
            return TypeRoque.GRAND;
        else
            return null;

    }

    private CoupSpecial toSpecial(String s) {

        if (s.equals("mat"))
            return CoupSpecial.MAT;
        else if (s.equals("echec"))
            return CoupSpecial.ECHEC;
        else
            return CoupSpecial.NULLE;

    }
}
