import processing.core.PVector;
import java.util.ArrayList;
import java.util.Arrays;

public class Piece {

    private final PVector               positionPieceIndice;        //Coordonnées des indices du tableau à deux dimensions de la carte pour la pièce.

    private PVector                     positionAffichage;          //position de la pièce sur l'affichage graphique.

    private final float                 taillePiece;                //Enregistrement de la taille de la pièce.
    private final PVector               tailleCarte;                //Enregistrement de la taille maximum de la carte dans un PVector. PVector.x = 4 et PVcetor.y = 5 signifie qye la carte fait du 4 * 5.

    private int                         nombrePieceAdjacent;

    private final int[][]               tabPieceSimple;             //Carte des pièces.

    private final ArrayList<Direction>  listeDirectionDisponible;   //liste des directions restantes diponibles.
    private final boolean[]             tabDirectionPrise;          //Tableau des pièces disponible ou pas. Chaque indice corespond à une direction (0 = NORD, EST = 1, SUD = 2 et OUEST + 3). True signifie que la pièce est prise.

    private final int                   index;                      //Enregistrement de l'indice de la pièce dans la liste de pièce de carte.
    private final int[]                 tabIndexPieceVoisine;       //Tableau des index des pièces voisines.

    private boolean                     pieceConstruite;

    private ArrayList<Piece>        listePieces;  //Liste des pièce qui seront affiché.

    private ArrayList<Portes>        listePortes;

    public Piece(PVector positionPiece, PVector positionPieceAffichage, float taillePiece, PVector tailleCarte, int[][] tabPieceSimple, int index, int tabDirectionPrise, int indexPieceOrigine, ArrayList<Piece> listePieces) {

        this.positionPieceIndice                        = positionPiece;

        this.positionAffichage                          = positionPieceAffichage;
        //this.positionAffichage                          = new PVector(this.positionPieceIndice.x * taillePiece, this.positionPieceIndice.y * taillePiece);
        //this.positionAffichage                          = new PVector((Main.processing.width * 5) / 100, (Main.processing.height * 5) / 100);
        this.taillePiece                                = taillePiece;
        this.tailleCarte                                = tailleCarte;

        this.nombrePieceAdjacent                        = 1; //1 pour la pièce qui a fait apparaître la pièce "this".
        this.tabPieceSimple                             = tabPieceSimple;

        this.listeDirectionDisponible                   = new ArrayList<>();
        this.tabDirectionPrise                          = new boolean[4];
        this.tabDirectionPrise[tabDirectionPrise]       = true; //Direction de la pièce originale en "true".

        this.index                                      = index;
        this.tabIndexPieceVoisine                       = new int[4];

        //initialisation à -1 et non 0 pour ne pas confondre l'indice 0.
        this.tabIndexPieceVoisine[0]                    = -1;
        this.tabIndexPieceVoisine[1]                    = -1;
        this.tabIndexPieceVoisine[2]                    = -1;
        this.tabIndexPieceVoisine[3]                    = -1;

        this.tabIndexPieceVoisine[tabDirectionPrise]    = indexPieceOrigine;

        this.pieceConstruite                            = false;

        this.listePieces                                = listePieces;

        this.listePortes                                = new ArrayList<>();

        this.initialisationPiece();
    }

    public Piece(int positionX, int positionY, PVector positionPieceAffichage, float taillePiece, PVector tailleCarte, int[][] tabPieceSimple, int index, ArrayList<Piece> listePieces) {

        this.positionPieceIndice        = new PVector(positionX, positionY);

        this.positionAffichage          = positionPieceAffichage;

        this.taillePiece                = taillePiece;
        this.tailleCarte                = tailleCarte;

        this.nombrePieceAdjacent        = 0; //0 car pièce d'irgine et donc n'a pas de pièce voisine.

        this.tabPieceSimple             = tabPieceSimple;

        this.listeDirectionDisponible   = new ArrayList<>();
        this.tabDirectionPrise          = new boolean[4];

        this.index                      = index;
        this.tabIndexPieceVoisine       = new int[4];

        //initialisation à -1 et non 0 pour ne pas confondre l'indice 0.
        this.tabIndexPieceVoisine[0]    = -1;
        this.tabIndexPieceVoisine[1]    = -1;
        this.tabIndexPieceVoisine[2]    = -1;
        this.tabIndexPieceVoisine[3]    = -1;

        this.pieceConstruite            = false;

        this.listePieces                = listePieces;

        this.listePortes                = new ArrayList<>();

        this.initialisationPiece();
    }

    /**
     * Mets à jours les pièces disponibles si les pièces se trouvent sur une bordure. Par exemple si les coordonées de la pièces sont [0 , 2]
     * alors la pièces du nord n'est pas disponible car c'est une bordure. Il faut mettre true.
     */
    private void initialisationPiece() {

            System.out.println();
        for (int iterateurDirection = 0 ; iterateurDirection < 4 ; ++iterateurDirection) {
            if (this.verificationDirection(iterateurDirection)) {
                this.tabDirectionPrise[iterateurDirection] = true;
                //this.nombrePieceAdjacent++;
            } else {
                this.listeDirectionDisponible.add(new Direction(iterateurDirection, this.tailleCarte));
            }
        }
    }

    /**
     * Renvoie une liste des directions possibles (Pas de bordure ou de pièce déja prise).
     * @return une liste de direction possible.
     */
    public ArrayList<Direction> directionsLibres() {
        ArrayList<Direction> directionsLibres = new ArrayList<>();

        for (int i = 0 ; i < 4 ; ++i) {
            Direction direction = this.directionLibre();
            if (direction.isDirectionValide()){
                directionsLibres.add(direction);
            }
        }

        this.pieceConstruite = true;

        return directionsLibres;
    }

    /**
     * Donnes un direction libre de la pièce.
     *
     * @return un entier qui correcspond à une direction. 0 = NORD, 1 = EST, 2 = SUD et 3 = OUEST. -1 = Aucune pièce libre.
     */
    public Direction directionLibre() {

        int nombreDirectionLibre    = this.listeDirectionDisponible.size();

        Direction directionLibre = new Direction();

        if (nombreDirectionLibre != 0) {
            int aleatoireDirectionLibre = (int) Main.processing.random(nombreDirectionLibre);
            directionLibre              = this.listeDirectionDisponible.get(aleatoireDirectionLibre);
            PVector position = new PVector(this.positionPieceIndice.x + directionLibre.getDirectionX(), this.positionPieceIndice.y + directionLibre.getDirectionY());
            int nombrePieceVoisine      = this.nombrePieceVoisine(position);

            if (!this.aleatoireCreationPiece(nombrePieceVoisine, position) || this.listeDirectionDisponible.size() == 0) {
                directionLibre.directionNonValide();
            }
            this.listeDirectionDisponible.remove(aleatoireDirectionLibre);
        }
        return directionLibre;
    }

    private boolean aleatoireCreationPiece(int nombrePieceVoisine, PVector position) {
        float probabilite;

        if (nombrePieceVoisine == 0) {
            probabilite = (float) 1.0;
        } else if (nombrePieceVoisine == 1) {
            probabilite = (float) 0.8;
        } else if (nombrePieceVoisine == 2) {
            probabilite = (float) 0.3;
        } else if (nombrePieceVoisine == 3) {
            probabilite = (float) 0.05;
        } else {
            probabilite = (float) 0.01;
        }

        for (int directionNESO = 0 ; directionNESO < 4 ; ++directionNESO) {
            Direction direction = new Direction(directionNESO, this.tailleCarte);
            if (direction.verificationDirection(position)) {
                PVector positionTampon = new PVector(position.x + direction.getDirectionX(), position.y + direction.getDirectionY());
                if (this.tabPieceSimple[(int) positionTampon.x][(int) positionTampon.y] != -1){
                    if (this.listePieces.get(this.tabPieceSimple[(int) positionTampon.x][(int) positionTampon.y]).isPieceConstruite()) {
                        probabilite = (float) 0.0;
                    }
                }
            }
        }

        float aleatoire = Main.processing.random(1);

        return probabilite > aleatoire;
    }

    /**
     * Calcul le nombre de pièce voisine dans chaque direction.
     * @param position Position de recherche.
     * @return Le nombre de pièce voisine (minimum 1)
     */
    private int nombrePieceVoisine(PVector position) {
        int cptVoisin = 0;
        for (int directionNESO = 0 ; directionNESO < 4 ; ++directionNESO) {
            Direction direction = new Direction(directionNESO, this.tailleCarte);
            if (direction.verificationDirection(position)) {
                PVector positionTampon = new PVector(position.x + direction.getDirectionX(), position.y + direction.getDirectionY());
                if (this.tabPieceSimple[(int) positionTampon.x][(int) positionTampon.y] != -1){
                    cptVoisin++;
                }
            }
        }
        return cptVoisin;
    }

    /**
     * Convertie un direction donnée en chiffre en coordonée de bousole.
     * @param directionChiffre direction en chiffre (0 = NORD, 1 = EST, 2 = SUD et 3 = OUEST).
     * @return Une chaîne de caractère qui correspond à une direction de bousole.
     */
    private String convertionDirectionChiffreString(int directionChiffre) {
        String directionString = "NON VALIDE";

        if (directionChiffre == 0) {
            directionString = "NORD";
        } else if (directionChiffre == 1) {
            directionString = "EST";
        } else if (directionChiffre == 2) {
            directionString = "SUD";
        } else if (directionChiffre == 3){
            directionString = "OUEST";
        }

        return directionString;
    }

    /**
     * Vérifie si la direction donnée en entrée ne sort pas des limites du tableau à deux dimension de la carte.
     * @param direction en chiffre.
     * @return un booleen, true la direction est possible, false la direction est impossible.
     */
    private boolean verificationDirection(int direction) {
        if (direction == 0) {
            if (this.positionPieceIndice.y - 1 > 0) {
                return this.tabPieceSimple[(int) this.positionPieceIndice.x][(int) (this.positionPieceIndice.y - 1)] != -1;
            }
        } else if (direction == 1) {
            if (this.positionPieceIndice.x + 1 < this.tailleCarte.x) {
                return this.tabPieceSimple[(int) (this.positionPieceIndice.x + 1)][(int) (this.positionPieceIndice.y)] != -1;
            }
        } else if (direction == 2) {
            if (this.positionPieceIndice.y + 1 < this.tailleCarte.y) {
                return this.tabPieceSimple[(int) this.positionPieceIndice.x][(int) (this.positionPieceIndice.y + 1)] != -1;
            }
        } else if (direction == 3) {
            if (this.positionPieceIndice.x - 1 > 0) {
                return this.tabPieceSimple[(int) (this.positionPieceIndice.x - 1)][(int) (this.positionPieceIndice.y)] != -1;
            }
        }
        return false;
    }

    public void miseAjourtabIndexPieceVoisine(int direction, int index) {
        this.tabIndexPieceVoisine[direction] = index;
    }

    public ArrayList<Integer> listePiecesDisponibles() {
        ArrayList<Integer> listePiecesDisponibles = new ArrayList<>();

        for (int j : this.tabIndexPieceVoisine) {
            if (j != -1) {
                listePiecesDisponibles.add(j);
            }
        }

        return listePiecesDisponibles;
    }



    public void acrementationNbPiecceAdjacente() {
        this.nombrePieceAdjacent++;
    }

    public void affichagePiece() {

        Main.processing.strokeWeight(1);
        //if (this.pieceConstruite) {
            Main.processing.fill(255,0,0);
            Main.processing.stroke(255);
        /*} else {
            Main.processing.fill(255);
            Main.processing.stroke(0);
        }*/
        Main.processing.square(this.positionAffichage.x, this.positionAffichage.y, this.taillePiece);

        for (Portes p : this.listePortes) {
            p.affichagePorte();
        }
        /*for (int i = 0 ; i < this.tabDirectionPrise.length ; ++i) {
            if (this.tabIndexPieceVoisine[i] != -1 && i == 0) {
                Main.processing.line(this.positionAffichage.x + (this.taillePiece * 40) / 100, this.positionAffichage.y, this.positionAffichage.x + (this.taillePiece * 60) / 100, this.positionAffichage.y);
            } else if (this.tabIndexPieceVoisine[i] != -1 && i == 1) {
                Main.processing.line(this.positionAffichage.x + this.taillePiece , this.positionAffichage.y + (this.taillePiece * 40) / 100, this.positionAffichage.x + this.taillePiece, this.positionAffichage.y + (this.taillePiece * 60) / 100);
            } else if (this.tabIndexPieceVoisine[i] != -1 && i == 2) {
                Main.processing.line(this.positionAffichage.x + (this.taillePiece * 40) / 100 , this.positionAffichage.y + this.taillePiece , this.positionAffichage.x + (this.taillePiece * 60) / 100, this.positionAffichage.y + this.taillePiece);
            } else if (this.tabIndexPieceVoisine[i] != -1 && i == 3) {
                Main.processing.line(this.positionAffichage.x , this.positionAffichage.y + (this.taillePiece * 40) / 100, this.positionAffichage.x, this.positionAffichage.y + (this.taillePiece * 60) / 100);
            }
        }*/

    }

    @Override
    public String toString() {
        return "Piece{" +
                "positionPieceIndice=" + positionPieceIndice +
                ", nombrePieceAdjacent=" + nombrePieceAdjacent +
                ", tabDirectionPrise=" + Arrays.toString(tabDirectionPrise) +
                ", tailleCarte=" + tailleCarte +
                ", INDEX = " + this.index +
                '}' + "\n" +
                this.affichage();
    }

    private String affichage() {
        StringBuilder str = new StringBuilder();
        for (int i = 0 ; i < 4 ; ++i) {
            str.append(this.tabIndexPieceVoisine[i]).append(" / ");
        }

        str.append("\n");
        return str.toString();
    }

    public void creationPorte() {
        PVector p1, p2;
        Portes porte;
        for (int i = 0 ; i < this.tabIndexPieceVoisine.length ; ++i) {
            if (this.tabIndexPieceVoisine[i] != -1) {
                if (i == 0) {
                    p1 = new PVector(this.positionAffichage.x + (this.taillePiece * 40) / 100, this.positionAffichage.y);
                    p2 = new PVector(this.positionAffichage.x + (this.taillePiece * 60) / 100, this.positionAffichage.y);
                    porte = new PortesNord(p1, p2, this);
                    this.listePortes.add(porte);
                } else if (i == 1) {
                    p1 = new PVector(this.positionAffichage.x + this.taillePiece , this.positionAffichage.y + (this.taillePiece * 40) / 100);
                    p2 = new PVector(this.positionAffichage.x + this.taillePiece, this.positionAffichage.y + (this.taillePiece * 60) / 100);
                    porte = new PortesEst(p1, p2, this);
                    this.listePortes.add(porte);
                } else if (i == 2) {
                    p1 = new PVector(this.positionAffichage.x + (this.taillePiece * 40) / 100 , this.positionAffichage.y + this.taillePiece);
                    p2 = new PVector(this.positionAffichage.x + (this.taillePiece * 60) / 100, this.positionAffichage.y + this.taillePiece);
                    porte = new PortesSud(p1, p2, this);
                    this.listePortes.add(porte);
                } else if (i == 3) {
                    p1 = new PVector(this.positionAffichage.x , this.positionAffichage.y + (this.taillePiece * 40) / 100);
                    p2 = new PVector(this.positionAffichage.x, this.positionAffichage.y + (this.taillePiece * 60) / 100);
                    porte = new PortesOuest(p1, p2, this);
                    this.listePortes.add(porte);
                }

            }
        }
    }

    public void setTabDirectionPrise(int direction) {
        this.tabDirectionPrise[direction] = true;
    }

    public void setTabIndexPieceVoisine(int direction, int index) {
        this.tabIndexPieceVoisine[direction] = index;
    }

    public PVector getPositionPieceIndice() {
        return positionPieceIndice;
    }

    public int getIndex() {
        return index;
    }

    public PVector getPositionAffichage() {
        return positionAffichage;
    }

    public boolean[] gettabDirectionPrise() {
        return tabDirectionPrise;
    }

    public int[] gettabIndexPieceVoisine() {
        return tabIndexPieceVoisine;
    }

    public float getTaillePiece() {
        return taillePiece;
    }

    public boolean isPieceConstruite() {
        return pieceConstruite;
    }

    public void supprimerDirectionLibre(int direction) {
        Direction direc = null;
        for (Direction d : this.listeDirectionDisponible) {

            if (d.directionChiffre() == direction) {
                direc = d;
            }
        }

        if (direc != null) {
            this.listeDirectionDisponible.remove(direc);
        }
    }

    public void setListePieces(ArrayList<Piece> listePieces) {
        this.listePieces = listePieces;
    }

    public void setPositionAffichage(PVector positionAffichage) {
        this.positionAffichage = positionAffichage;
    }


    public void setPortes() {
        for (Portes porte : this.listePortes) {
            porte.initialisation();
        }
    }

    public Portes rechercheTypePortes(int direction) {
        for (Portes p : this.listePortes) {
            if (direction == 0 && p instanceof PortesNord) {
                return p;
            } else  if (direction == 1 && p instanceof PortesEst) {
                return p;
            } else  if (direction == 2 && p instanceof PortesSud) {
                return p;
            } else  if (direction == 3 && p instanceof PortesOuest) {
                return p;
            }
        }
        return new PortesNull();
    }
}
