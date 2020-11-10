import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

/**
 *  Regroupement de toutes les pièces. La carte enregistre la position de chaque pièce dans un
 *  tableau d'entier à deux dimensions (0 = pas de pièce, 1 = pièce).
 *
 *  Toutes les pièces sont aussi enregistré dans l'objet carte.
 */
public class Carte extends PApplet {

    private final float             taillePiece;            //Taille des pièces.
    private final int               nombrePieces;           //Nombre de pièce total.

    private final int               nombreColonnes;         //Nombre de pièces en colonne.
    private final int               nombreLignes;           //Nombre de pièces en lignes.

    private final int[][]           tabPiecesSimples;       //Tableau simplifié des pièces existantes (0 pas de pièces, 1 pièce présentes.)

    private final Piece             pieceCentrale;          //Génération de la première pièce.
    private final ArrayList<Piece>  listePieces;            //Liste des pièces.

    private ArrayList<Piece>        listePiecesAffichages;  //Liste des pièce qui seront affiché.

    private Personnage              personnage;

    public Carte(int nombreLignes, int nombreColonnes) {


        this.taillePiece                                                        = (float) (Main.processing.height * 90) / 100;

        this.nombrePieces                                                       = nombreColonnes * nombreLignes;

        this.nombreColonnes                                                     = nombreColonnes;
        this.nombreLignes                                                       = nombreLignes;

        this.tabPiecesSimples                                                   = new int[this.nombreColonnes][this.nombreLignes];

        for (int i = 0 ; i < this.tabPiecesSimples.length ; ++i) {
            for (int j = 0 ; j < this.tabPiecesSimples[i].length ; ++j) {
                this.tabPiecesSimples[j][i] = -1;
            }
        }
        
        this.tabPiecesSimples[this.nombreLignes / 2][this.nombreColonnes / 2]   = 0;    //Enregistrement de la position de la première piece dans le tableau simple.

        this.listePieces                                                        = new ArrayList<>();
        this.pieceCentrale                                                      = new Piece(this.nombreColonnes / 2, this.nombreLignes / 2, new PVector( (Main.processing.width * 25.f) / 100.f, (Main.processing.height * 5.f) / 100.f), this.taillePiece, new PVector(this.nombreColonnes - 1, this.nombreLignes - 1), this.tabPiecesSimples, 0, this.listePieces);
        this.listePieces.add(pieceCentrale);                                            //Enregistrement dans la liste de la première pièce créé précedement.

        this.pieceCentrale .setListePieces(this.listePieces);
        this.listePiecesAffichages                                              = new ArrayList<>();


    }

    /**
     * Initialise le placement des pièces. Génération aléatoire.
     *
     * TO DO : Optimiser la fonction et améliorer les commentaires.
     * Enlever les System.out.println
     */
    public void initialisationCarte() {

        ArrayList<Direction> directionsLibres = this.pieceCentrale.directionsLibres();


        for (Direction directionLibre : directionsLibres) {

            if (directionLibre.isDirectionValide()) { //Vérifie si la direction est valide.

                PVector nouvellePieceCoordonnee = new PVector((int) (this.pieceCentrale.getPositionPieceIndice().x + directionLibre.getDirectionX()), (int) (this.pieceCentrale.getPositionPieceIndice().y + directionLibre.getDirectionY())); //Coordonnée de la nouvelle pièce. Enregistré dans un PVector. Prends la position de la pièce p et calcule les nouvelles coordonnée à l'aide de la direction choisie.
                this.tabPiecesSimples[(int) nouvellePieceCoordonnee.x][(int) nouvellePieceCoordonnee.y] = this.listePieces.size(); //Enregistre la nouvelle pièces au coordonnées trouvé précédement.
                PVector pos = this.positionPiece(directionLibre, pieceCentrale.getPositionAffichage());
                Piece nouvPiece = new Piece(nouvellePieceCoordonnee, pos, this.taillePiece, new PVector(this.nombreColonnes - 1, this.nombreLignes - 1), this.tabPiecesSimples, this.listePieces.size(), directionLibre.directionOpposeChiffre(), this.pieceCentrale.getIndex(), this.listePieces);
                this.listePieces.add(nouvPiece); //Créer une nouvelle pièce
                nouvPiece.setListePieces(listePieces);

                this.pieceCentrale.miseAjourtabIndexPieceVoisine(directionLibre.directionChiffre(), this.listePieces.size() - 1);

                this.pieceCentrale.setTabDirectionPrise(directionLibre.directionChiffre()); //Mets à jours la pièce adjacente.

                this.pieceCentrale.acrementationNbPiecceAdjacente(); //Augmente d'un le nombre de pièce voisine de la pièce qui est en traitement.

            }
        }

        this.pieceCentrale.creationPorte();
    }

    public PVector positionPiece(Direction d, PVector pos) {
        if (d.directionChiffre() == 0) {
            return new PVector(pos.x, pos.y - this.taillePiece);
        } else if (d.directionChiffre() == 1) {
            return new PVector(pos.x + this.taillePiece, pos.y);
        } else if (d.directionChiffre() == 2) {
            return new PVector(pos.x, pos.y + this.taillePiece);
        } else {
            return new PVector(pos.x - this.taillePiece, pos.y);
        }
    }

    /**
     * Mets à jours les pièces disponible pour chaque pièce de la liste à partir des directions disponibles récupéré.
     */
    public void miseAjourCarte() {
        int indexPiece = this.personnage.getIndexPiece();

        Piece pieceDeLaListe = this.listePieces.get(indexPiece);


        if (!pieceDeLaListe.isPieceConstruite()) {

            ArrayList<Direction> directionsLibres = pieceDeLaListe.directionsLibres();

            for (Direction directionLibre : directionsLibres) {

                if (directionLibre.isDirectionValide()) { //Vérifie si la direction est valide.
                    PVector nouvellePieceCoordonnee = new PVector((int) (pieceDeLaListe.getPositionPieceIndice().x + directionLibre.getDirectionX()), (int) (pieceDeLaListe.getPositionPieceIndice().y + directionLibre.getDirectionY()));
                    PVector pos = this.positionPiece(directionLibre, pieceDeLaListe.getPositionAffichage());
                    //Coordonnée de la nouvelle pièce. Enregistré dans un PVector. Prends la position de la pièce p et calcule les nouvelles coordonnée à l'aide de la direction choisie.
                    this.tabPiecesSimples[(int) nouvellePieceCoordonnee.x][(int) nouvellePieceCoordonnee.y] = this.listePieces.size(); //Enregistre la nouvelle pièces au coordonnées trouvé précédement.
                    Piece nouvPiece = new Piece(nouvellePieceCoordonnee, pos, this.taillePiece, new PVector(this.nombreColonnes - 1, this.nombreLignes - 1), this.tabPiecesSimples, this.listePieces.size(), directionLibre.directionOpposeChiffre(), pieceDeLaListe.getIndex(), this.listePieces);
                    this.listePieces.add(nouvPiece); //Créer une nouvelle pièce
                    nouvPiece.setListePieces(this.listePieces);

                    pieceDeLaListe.miseAjourtabIndexPieceVoisine(directionLibre.directionChiffre(), this.listePieces.size() - 1);

                    pieceDeLaListe.setTabDirectionPrise(directionLibre.directionChiffre()); //Mets à jours la pièce adjacente.

                    pieceDeLaListe.acrementationNbPiecceAdjacente(); //Augmente d'un le nombre de pièce voisine de la pièce qui est en traitement.

                }
            }
            pieceDeLaListe.creationPorte();
            this.miseAjourVoisin();
            /*for (int i = 0 ; i < this.tabPiecesSimples.length ; ++i) {
                for (int j = 0 ; j < this.tabPiecesSimples[i].length ; ++j) {
                    System.out.print("\t" + this.tabPiecesSimples[i][j]);
                    //System.out.println();
                }
                System.out.println();
            }
            System.out.println();*/
        }

    }

    public PVector pointApparition() {

        return new PVector(Main.processing.width / 2.f, Main.processing.height / 2.f);

        //return new PVector(Main.processing.width / 2.f, Main.processing.height / 2.f);
    }

    public void setPersonnage(Personnage personnage) {
        this.personnage = personnage;
    }

    public void miseAjourPersonnage() {
        Piece piece = this.personnage.getPiece();

        int[] tabIndexPieceVoisine = piece.gettabIndexPieceVoisine();

        this.listePiecesAffichages = new ArrayList<>();
        this.listePiecesAffichages.add(this.listePieces.get(piece.getIndex()));

        for (int iterateurPieceVoisinIndex : tabIndexPieceVoisine) {
            if (iterateurPieceVoisinIndex != -1) {
                this.listePiecesAffichages.add(this.listePieces.get(iterateurPieceVoisinIndex));
            }
        }

        this.personnage.setListePieces(this.listePieces);
    }

    public void miseAjourVoisin() {
        for (Piece piece : this.listePieces) {

            PVector position = piece.getPositionPieceIndice();

            if (this.tabPiecesSimples[(int) (position.x + 1)][(int) position.y] != -1 && !piece.gettabDirectionPrise()[1]) {

                int voisinPiece = this.tabPiecesSimples[(int) (position.x + 1)][(int) position.y];
                Piece p = this.listePieces.get(voisinPiece);
                piece.setTabDirectionPrise(1);
                piece.setTabIndexPieceVoisine(1, p.getIndex());
                p.setTabDirectionPrise(3);
                p.setTabIndexPieceVoisine(3, piece.getIndex());
                piece.acrementationNbPiecceAdjacente();
                p.acrementationNbPiecceAdjacente();
                piece.supprimerDirectionLibre(1);
                p.supprimerDirectionLibre(3);
            }
            if (this.tabPiecesSimples[(int) (position.x - 1)][(int) position.y] != -1 && !piece.gettabDirectionPrise()[3]) {

                int voisinPiece = this.tabPiecesSimples[(int) (position.x - 1)][(int) position.y];
                Piece p = this.listePieces.get(voisinPiece);
                piece.setTabDirectionPrise(3);
                piece.setTabIndexPieceVoisine(3, p.getIndex());
                p.setTabDirectionPrise(1);
                p.setTabIndexPieceVoisine(1, piece.getIndex());
                piece.acrementationNbPiecceAdjacente();
                p.acrementationNbPiecceAdjacente();
                piece.supprimerDirectionLibre(3);
                p.supprimerDirectionLibre(1);
            }
            if ( this.tabPiecesSimples[(int) (position.x)][(int) position.y + 1] != -1 && !piece.gettabDirectionPrise()[2]) {

                int voisinPiece = this.tabPiecesSimples[(int) (position.x)][(int) position.y + 1];
                Piece p = this.listePieces.get(voisinPiece);
                piece.setTabDirectionPrise(2);
                piece.setTabIndexPieceVoisine(2, p.getIndex());
                p.setTabDirectionPrise(0);
                p.setTabIndexPieceVoisine(0, piece.getIndex());
                piece.acrementationNbPiecceAdjacente();
                p.acrementationNbPiecceAdjacente();
                piece.supprimerDirectionLibre(2);
                p.supprimerDirectionLibre(0);
            }
            if (this.tabPiecesSimples[(int) (position.x)][(int) position.y - 1] != -1 && !piece.gettabDirectionPrise()[0]) {

                int voisinPiece = this.tabPiecesSimples[(int) (position.x)][(int) position.y - 1];
                Piece p = this.listePieces.get(voisinPiece);
                piece.setTabDirectionPrise(0);
                piece.setTabIndexPieceVoisine(0, p.getIndex());
                p.setTabDirectionPrise(2);
                p.setTabIndexPieceVoisine(2, piece.getIndex());
                piece.acrementationNbPiecceAdjacente();
                p.acrementationNbPiecceAdjacente();
                piece.supprimerDirectionLibre(0);
                p.supprimerDirectionLibre(2);
            }
        }
    }

    /**
     * Dessine la carte.
     */
    public void affichageCarte() {

        this.personnage.getPiece().affichagePiece();
        /*for (Piece piece : this.listePieces) {
            piece.affichagePiece();
        }*/

        /*Piece piece = this.personnage.getPiece();

        ArrayList<Integer> l = this.pieceAffichage(piece);

        for (int i : l) {
            if (i != -1) {
                this.listePieces.get(i).affichagePiece();
            }
        }*/

        /*Piece piece = this.personnage.getPiece();
        piece.affichagePiece();
        for (int i = 0; i < piece.gettabIndexPieceVoisine().length; ++i) {
            if (piece.gettabIndexPieceVoisine()[i] != -1) {
                this.listePieces.get(piece.gettabIndexPieceVoisine()[i]).affichagePiece();
            }
        }*/
    }

    private ArrayList<Integer> pieceAffichage(Piece piece) {
        ArrayList<Integer> l = new ArrayList<>();
        PVector pos = piece.getPositionPieceIndice();
        l.add(this.tabPiecesSimples[(int) (pos.x - 1)][(int) (pos.y - 1)]);
        l.add(this.tabPiecesSimples[(int) (pos.x)][(int) (pos.y - 1)]);
        l.add(this.tabPiecesSimples[(int) (pos.x + 1)][(int) (pos.y - 1)]);

        l.add(this.tabPiecesSimples[(int) (pos.x - 1)][(int) (pos.y)]);
        l.add(this.tabPiecesSimples[(int) (pos.x)][(int) (pos.y)]);
        l.add(this.tabPiecesSimples[(int) (pos.x + 1)][(int) (pos.y)]);

        l.add(this.tabPiecesSimples[(int) (pos.x - 1)][(int) (pos.y + 1)]);
        l.add(this.tabPiecesSimples[(int) (pos.x)][(int) (pos.y + 1)]);
        l.add(this.tabPiecesSimples[(int) (pos.x + 1)][(int) (pos.y + 1)]);

        return l;
    }

    public Piece pieceParIndex(int index) {
        return this.listePieces.get(index);
    }

    public ArrayList<Piece> getListePieces() {
        return listePieces;
    }

    private void setPositionPieces(int[] listeVoisin) {
        Piece piecePerso = this.personnage.getPiece();
        for (int i = 0; i < listeVoisin.length; ++i) {
            if (listeVoisin[i] != -1) {
                Piece piece = this.listePieces.get(listeVoisin[i]);
                if (i == 0) {
                    piece.setPositionAffichage(new PVector(piecePerso.getPositionAffichage().x, piecePerso.getPositionAffichage().y - this.taillePiece));
                } else if (i == 1) {
                    piece.setPositionAffichage(new PVector(piecePerso.getPositionAffichage().x + this.taillePiece, piecePerso.getPositionAffichage().y));
                } else if (i == 2) {
                    piece.setPositionAffichage(new PVector(piecePerso.getPositionAffichage().x, piecePerso.getPositionAffichage().y + this.taillePiece));
                } else if (i == 3) {
                    piece.setPositionAffichage(new PVector(piecePerso.getPositionAffichage().x - this.taillePiece, piecePerso.getPositionAffichage().y));
                }
            }
        }
    }

    public void miseAjourPositionPiece(boolean upZ, boolean leftQ, boolean downS, boolean rightD) {
        Piece piece = this.personnage.getPiece();
        ArrayList<Integer> listePieceVoisine = piece.listePiecesDisponibles();

        this.setPositionPieces(piece.gettabIndexPieceVoisine());

        if (upZ) {
            PVector pos = new PVector(this.personnage.getPositionX(), this.personnage.getPositionY() - 1 - this.personnage.getTaillePersonnage());
            if (this.personnage.verificationPositionPiece(pos) && piece.rechercheTypePortes(0).verification(pos)) {
                PVector pos1 = new PVector(piece.getPositionAffichage().x, piece.getPositionAffichage().y + this.personnage.getVitesse());
                piece.setPositionAffichage(pos1);
                piece.setPortes();
                for (Integer pieceVoisineIndex : listePieceVoisine) {
                    Piece pieceVoisne = this.listePieces.get(pieceVoisineIndex);
                    PVector pos2 = new PVector(pieceVoisne.getPositionAffichage().x, pieceVoisne.getPositionAffichage().y + this.personnage.getVitesse());
                    pieceVoisne.setPositionAffichage(pos2);
                    pieceVoisne.setPortes();
                }
            }
        }
        if (leftQ) {
            PVector pos = new PVector(this.personnage.getPositionX() - 1 - this.personnage.getTaillePersonnage(), this.personnage.getPositionY());
            if (this.personnage.verificationPositionPiece(pos)  && piece.rechercheTypePortes(3).verification(pos)) {
                PVector pos1 = new PVector(piece.getPositionAffichage().x + this.personnage.getVitesse(), piece.getPositionAffichage().y);
                piece.setPositionAffichage(pos1);
                piece.setPortes();
                for (Integer pieceVoisineIndex : listePieceVoisine) {
                    Piece pieceVoisne = this.listePieces.get(pieceVoisineIndex);
                    PVector pos2 = new PVector(pieceVoisne.getPositionAffichage().x + this.personnage.getVitesse(), pieceVoisne.getPositionAffichage().y);
                    pieceVoisne.setPositionAffichage(pos2);
                    pieceVoisne.setPortes();
                }

            }
        }
        if (downS) {
            PVector pos = new PVector(this.personnage.getPositionX(), this.personnage.getPositionY() + 1 + this.personnage.getTaillePersonnage());
            if (this.personnage.verificationPositionPiece(pos)  && piece.rechercheTypePortes(2).verification(pos)) {
                PVector pos1 = new PVector(piece.getPositionAffichage().x, piece.getPositionAffichage().y - this.personnage.getVitesse());
                piece.setPositionAffichage(pos1);
                piece.setPortes();
                for (Integer pieceVoisineIndex : listePieceVoisine) {
                    Piece pieceVoisne = this.listePieces.get(pieceVoisineIndex);
                    PVector pos2 = new PVector(pieceVoisne.getPositionAffichage().x, pieceVoisne.getPositionAffichage().y - this.personnage.getVitesse());
                    pieceVoisne.setPositionAffichage(pos2);
                    pieceVoisne.setPortes();
                }
            }
        }
        if (rightD) {
            PVector pos = new PVector(this.personnage.getPositionX() + 1 + this.personnage.getTaillePersonnage(), this.personnage.getPositionY());
            if (this.personnage.verificationPositionPiece(pos)  && piece.rechercheTypePortes(1).verification(pos)) {
                PVector pos1 = new PVector(piece.getPositionAffichage().x - this.personnage.getVitesse(), piece.getPositionAffichage().y);
                piece.setPositionAffichage(pos1);
                piece.setPortes();
                for (Integer pieceVoisineIndex : listePieceVoisine) {
                    Piece pieceVoisne = this.listePieces.get(pieceVoisineIndex);
                    PVector pos2 = new PVector(pieceVoisne.getPositionAffichage().x - this.personnage.getVitesse(), pieceVoisne.getPositionAffichage().y);
                    pieceVoisne.setPositionAffichage(pos2);
                    pieceVoisne.setPortes();
                }
            }
        }
    }
}


