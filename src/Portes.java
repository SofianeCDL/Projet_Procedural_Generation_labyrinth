
import processing.core.PVector;

public abstract class  Portes {

        protected PVector position1;
        protected PVector position2;

        protected Piece piece;

        protected boolean porteNull;

        public Portes(PVector position1, PVector position2, Piece piece) {

            this.position1 = position1;
            this.position2 = position2;
            
            this.piece = piece;

            this.porteNull = false;
        }

        public Portes() {
            this.porteNull = true;
        }

        public abstract void initialisation();

        public void affichagePorte() {
            Main.processing.stroke(0,255,0);
            Main.processing.strokeWeight(10);

            Main.processing.line(this.position1.x, this.position1.y, this.position2.x, this.position2.y);
        }

        public void setPosition(PVector position1, PVector position2) {
            this.position1 = position1;
            this.position2 = position2;
        }

        public abstract boolean verification(PVector positionPersonnage);
}
