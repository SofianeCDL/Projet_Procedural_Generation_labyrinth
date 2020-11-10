import processing.core.PVector;

import java.util.ArrayList;

public class Personnage {

    private final float       positionX;
    private final float       positionY;

    private float       X;
    private float       Y;

    private final PVector     positionCentre;
    private final float vitesse;

    private int         indexPiece;
    private Piece       piece;
    private ArrayList<Piece> listePieces;
    private boolean[]   directionPrise;

    private final float taillePersonnage;

    public Personnage(PVector apparition, Piece piece, ArrayList<Piece> listePieces) {
        this.positionX          = apparition.x;
        this.positionY          = apparition.y;

        this.X                  = 0.f;
        this.Y                  = 0.f;

        this.positionCentre     = new PVector(apparition.x, apparition.y);

        this.indexPiece         = 0;
        this.piece              = piece;
        this.directionPrise     = this.piece.gettabDirectionPrise();

        this.vitesse            = (this.piece.getTaillePiece() * 1) / 100;

        this.taillePersonnage   = (this.piece.getTaillePiece() * 2) / 100;

        this.listePieces = listePieces;


    }

    public void miseAjourPosition(boolean upZ, boolean leftQ, boolean downS, boolean rightD, boolean pause) {

        String direction = "";
        if (upZ) {
            if (this.verificationPositionPiece(new PVector(this.positionX, this.positionY - this.vitesse - this.taillePersonnage))) {
                this.Y -= this.vitesse;

                direction += "HAUT\n";

            }
        }
        if (leftQ) {
            if (this.verificationPositionPiece(new PVector(this.positionX - this.vitesse - this.taillePersonnage, this.positionY))) {
                this.X -= this.vitesse;

                direction += "GAUCHE\n";

            }
        }
        if (downS) {
            if (this.verificationPositionPiece(new PVector(this.positionX, this.positionY + this.vitesse + this.taillePersonnage))) {
                this.Y += this.vitesse;

                direction += "BAS\n";

            }
        }
        if (rightD) {
            if (this.verificationPositionPiece(new PVector(this.positionX + this.vitesse + this.taillePersonnage, this.positionY))) {
                this.X += this.vitesse;

                direction += "DROITE\n";

            }
        }

        Main.processing.fill(0, 255, 0);
        Main.processing.textSize(30);
        Main.processing.text(direction, 25, 100);

        if (pause) {
            System.out.println("Debug");
        }
    }

    public void indexPiece() {
        if (this.positionY < this.piece.getPositionAffichage().y && this.directionPrise[0] && this.piece.gettabIndexPieceVoisine()[0] != -1) {
            this.setPiece(this.listePieces.get(this.piece.gettabIndexPieceVoisine()[0]));
            this.piece.gettabIndexPieceVoisine();

        } else if (this.positionX > this.piece.getPositionAffichage().x + this.piece.getTaillePiece() && this.directionPrise[1] && this.piece.gettabIndexPieceVoisine()[1] != -1) {
            this.setPiece(this.listePieces.get(this.piece.gettabIndexPieceVoisine()[1]));
            this.piece.gettabIndexPieceVoisine();

        } else if (this.positionY > this.piece.getPositionAffichage().y + this.piece.getTaillePiece() && this.directionPrise[2] && this.piece.gettabIndexPieceVoisine()[2] != -1) {
            this.setPiece(this.listePieces.get(this.piece.gettabIndexPieceVoisine()[2]));
            this.piece.gettabIndexPieceVoisine();

        } else if (this.positionX < this.piece.getPositionAffichage().x && this.directionPrise[3] && this.piece.gettabIndexPieceVoisine()[3] != -1) {
            this.setPiece(this.listePieces.get(this.piece.gettabIndexPieceVoisine()[3]));
            this.piece.gettabIndexPieceVoisine();
        }

    }



    public void affichagePersonnage() {
        Main.processing.noStroke();
        Main.processing.fill(0);
        Main.processing.circle( this.positionCentre.x, this.positionCentre.y, this.taillePersonnage * 2);

        Main.processing.fill(0, 0, 255);
        Main.processing.textSize(32);
        Main.processing.text(this.indexPiece + " | " + this.X + " / " + this.Y, 25, 50);

    }

    /*public boolean verificationPositionPiece(PVector position, float taillePiece) {

        PVector positionPiece = this.piece.getPositionAffichage();

        if (position.x < positionPiece.x && this.piece.gettabIndexPieceVoisine()[3] == -1) {
            return false;
        } else if (position.x > positionPiece.x + taillePiece && this.piece.gettabIndexPieceVoisine()[1] == -1) {

            return false;
        } else if (position.y < positionPiece.y && this.piece.gettabIndexPieceVoisine()[0] == -1) {

            return false;
        } else if (position.y > positionPiece.y + taillePiece && this.piece.gettabIndexPieceVoisine()[2] == -1) {

            return false;
        }

        /*if (position.x  < 0) {
            return false;
        } else if (position.x  > Main.processing.width) {

            return false;
        } else if (position.y  < 0) {

            return false;
        } else if (position.y  > Main.processing.height) {

            return false;
        }

        return true;
    }*/

    public boolean verificationPositionPiece(PVector position) {

        PVector positionPiece = this.piece.getPositionAffichage();
        float taillePiece = this.piece.getTaillePiece();

        if (position.x < positionPiece.x && this.piece.gettabIndexPieceVoisine()[3] == -1) {
            return false;
        } else if (position.x > positionPiece.x + taillePiece && this.piece.gettabIndexPieceVoisine()[1] == -1) {

            return false;
        } else if (position.y < positionPiece.y && this.piece.gettabIndexPieceVoisine()[0] == -1) {

            return false;
        } else if (position.y > positionPiece.y + taillePiece && this.piece.gettabIndexPieceVoisine()[2] == -1) {

            return false;
        }

        /*if (position.x  < 0) {
            return false;
        } else if (position.x  > Main.processing.width) {

            return false;
        } else if (position.y  < 0) {

            return false;
        } else if (position.y  > Main.processing.height) {

            return false;
        }*/

        return true;
    }

    public void setPiece(Piece nouvellePiece) {
        this.piece = nouvellePiece;
        this.directionPrise = this.piece.gettabDirectionPrise();
        this.indexPiece = this.piece.getIndex();
    }

    public Piece getPiece() {
        return piece;
    }

    public int getIndexPiece() {
        return indexPiece;
    }

    public void setListePieces(ArrayList<Piece> listePieces) {
        this.listePieces = listePieces;
    }

    public float getVitesse() {
        return vitesse;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getTaillePersonnage() {
        return taillePersonnage;
    }

}
