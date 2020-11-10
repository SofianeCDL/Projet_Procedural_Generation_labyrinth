import processing.core.PVector;

import java.util.Objects;

/**
 * Direction permets de trouver la position de la pièce voisine se situant au NORD/EST/SUD/OUEST à partir d'une position initiale.
 * NORD = 0, EST = 1, SUD = 2 et OUEST = 3.d
 *
 * Les direction ne fonctionne seulement pour le tableau à deux dimension des positions des pièces.
 *
 * Les position X et Y vont augmenter ou diminuer de 1 en fonction de la direction prise.
 *
 * EXEMPLE : position initiale = {5, 4}. Direction NORD {-1, 0} alors la pièce voisine au nord de celle-çi se trouve aux coordonnées = {4, 4} (car 5 - 1 = 4).
 *
 * On à donc :  NORD    = {-1, 0}
 *              EST     = {0, 1}
 *              SUD     = {1, 0}
 *              OUEST   = {0, -1}
 */
public class Direction {
    private String          directionText;      //Nom de la direction (NORD, EST, SUD et OUEST).

    private boolean         directionValide;    //True si la direction est valide, sinon false.

    private int             directionX;         //ajoute, retire 1 ou ne fait rien pour l'indice x.
    private int             directionY;         //ajoute, retire 1 ou ne fait rien pour l'indice y.

    private final PVector   directionXetY;      //PVector de la direction.

    private PVector         tailleCarte;        //Taille de la carte enregistré dans un PVector.

    public  Direction(String direction, PVector tailleCarte) {
        this.directionText      = direction;

        this.directionValide    = true;

        this.directionX         = 0;
        this.directionY         = 0;

        this.directionXetY      = new PVector(this.directionX, this.directionY);

        this.tailleCarte        = tailleCarte;

        this.initialisationDirection();
    }

    public Direction(int direction, PVector tailleCarte) {
        this.directionText      = this.directionString(direction);

        this.directionValide    = true;

        this.directionX         = 0;
        this.directionY         = 0;

        this.directionXetY      = new PVector(this.directionX, this.directionY);

        this.tailleCarte        = tailleCarte;

        this.initialisationDirection();
    }

    public Direction() {

        this.directionValide    = false;

        this.directionX         = 0;
        this.directionY         = 0;

        this.directionXetY      = new PVector(this.directionX, this.directionY);
    }

    /**
     * Initialiste la direction que doit prendre l'indice à partir du nom de la direction (NORD, EST, SUD et OUEST).
     */
    private void initialisationDirection() {

        switch (this.directionText) {
            case "NORD":
                this.directionY = -1;
                break;
            case "EST":
                this.directionX = 1;
                break;
            case "SUD":
                this.directionY = 1;
                break;
            case "OUEST":
                this.directionX = -1;
                break;
            case "NON VALIDE":
                this.directionNonValide();
                break;
        }

        this.directionXetY.add(new PVector(this.directionX, this.directionY));
    }

    /**
     * Convertie la direction en chiffre. (NORD = 0, EST = 1; SUD = 2 et OUEST = 3).
     * @return un entier correspondant à la direction.
     */
    public int directionChiffre() {

        switch (this.directionText) {
            case "NORD":
                return 0;
            case "EST":
                return 1;
            case "SUD":
                return 2;
            case "OUEST":
                return 3;
        }
        return -1;
    }

    /**
     * Convertis un direction en chiffre en string.
     * @param direction direction en chiffre.
     * @return direction en string.
     */
    public String directionString(int direction) {
        if (direction == 0) {
            return "NORD";
        } else if (direction == 1) {
            return "EST";
        } else if (direction == 2) {
            return "SUD";
        } else if (direction == 3){
            return "OUEST";
        }

        return "NON VALIDE";
    }

    /**
     * Renvois la direction opposé à l'objet. (NORD <-> SUD, EST <-> OUEST)
     * @return la direction opposé en chiffre.
     */
    public int directionOpposeChiffre() {
        switch (this.directionText) {
            case "NORD":
                return 2;
            case "EST":
                return 3;
            case "SUD":
                return 0;
            case "OUEST":
                return 1;
        }
        return -1;
    }

    public Direction directionOppose() {
        switch (this.directionText) {
            case "NORD":
                return new Direction("SUD", this.tailleCarte);
            case "EST":
                return new Direction("OUEST", this.tailleCarte);
            case "SUD":
                return new Direction("NORD", this.tailleCarte);
            case "OUEST":
                return new Direction("EST", this.tailleCarte);
        }
        return new Direction();
    }

    /**
     * Transforme en direction non valide.
     */
    public void directionNonValide() {
        this.directionX = 0;
        this.directionY = 0;
        this.directionValide = false;
    }

    /**
     * Vérifie si la direction ne sort pas des limites de la carte.
     * @param position position à vérifier.
     * @return "true" ou "false" si la direction est à l'interieur de la carte.
     */
    public boolean verificationDirection(PVector position) {
        PVector positionCopy = new PVector(position.x, position.y);
        positionCopy.add(this.directionXetY);

        if (positionCopy.x < 0) {
            return false;
        } else if (positionCopy.x > this.tailleCarte.x) {
            return false;
        } else if (positionCopy.y < 0) {
            return false;
        } else if (positionCopy.y > this.tailleCarte.y) {
            return false;
        }

        return true;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public boolean isDirectionValide() {
        return directionValide;
    }



    @Override
    public String toString() {
        return "Direction{" +
                "direction='" + directionText + '\'' +
                ", directionValide=" + directionValide +
                ", directionX=" + directionX +
                ", directionY=" + directionY +
                ", directionXetY=" + directionXetY +
                '}' + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return directionValide == direction.directionValide &&
                directionX == direction.directionX &&
                directionY == direction.directionY &&
                directionText.equals(direction.directionText) &&
                directionXetY.equals(direction.directionXetY) &&
                tailleCarte.equals(direction.tailleCarte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(directionText, directionValide, directionX, directionY, directionXetY, tailleCarte);
    }
}
