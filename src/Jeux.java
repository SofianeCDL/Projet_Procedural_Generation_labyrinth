public class Jeux {
    private final Carte         labyrinth;

    private final Personnage    perso;

    public Jeux() {
        this.labyrinth  = new Carte(100,100);

        this.perso      = new Personnage(this.labyrinth.pointApparition(), this.labyrinth.pieceParIndex(0), this.labyrinth.getListePieces());
    }

    public void initialisation() {
        this.labyrinth.initialisationCarte();
        this.labyrinth.setPersonnage(this.perso);
    }

    public void miseAjours(boolean upZ, boolean leftQ, boolean downS, boolean rightD, boolean pause) {

        this.labyrinth.miseAjourCarte();
        this.perso.miseAjourPosition(upZ, leftQ, downS, rightD, pause);
        this.perso.indexPiece();
        this.labyrinth.miseAjourPersonnage();
        this.labyrinth.miseAjourPositionPiece(upZ, leftQ, downS, rightD);
       //this.labyrinth.miseAjourCarte();

    }

    public void affichageJeux(boolean pause) {
        this.labyrinth.affichageCarte();
        this.perso.affichagePersonnage();
    }
}
