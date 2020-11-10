import processing.core.PVector;

public class PortesOuest extends Portes{
    public PortesOuest(PVector position1, PVector position2, Piece piece) {
        super(position1, position2, piece);

    }

    public void initialisation() {
        PVector position1 = new PVector(super.piece.getPositionAffichage().x, super.piece.getPositionAffichage().y + (super.piece.getTaillePiece() * 40) / 100);
        PVector position2 = new PVector(super.piece.getPositionAffichage().x , super.piece.getPositionAffichage().y + (super.piece.getTaillePiece() * 60) / 100);

        super.setPosition(position1, position2);
    }

    public boolean verification(PVector positionPersonnage) {
        return positionPersonnage.y > super.position1.y && positionPersonnage.y < super.position2.y || positionPersonnage.x > super.position1.x;
    }
}
