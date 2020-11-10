import processing.core.PVector;

public class PortesSud extends Portes{
    public PortesSud(PVector position1, PVector position2, Piece piece) {
        super(position1, position2, piece);

    }

    public void initialisation() {
        PVector position1 = new PVector(super.piece.getPositionAffichage().x + (super.piece.getTaillePiece() * 40) / 100, super.piece.getPositionAffichage().y + super.piece.getTaillePiece());
        PVector position2 = new PVector(super.piece.getPositionAffichage().x + (super.piece.getTaillePiece() * 60) / 100, super.piece.getPositionAffichage().y + super.piece.getTaillePiece());

        super.setPosition(position1, position2);
    }

    public boolean verification(PVector positionPersonnage) {
        return positionPersonnage.x > super.position1.x && positionPersonnage.x < super.position2.x || positionPersonnage.y < super.position1.y;
    }
}
