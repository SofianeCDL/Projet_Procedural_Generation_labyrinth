import processing.core.PApplet;

public class Main extends PApplet {
    public static PApplet processing;
    private Jeux videoGames;
    boolean upZ = false;
    boolean leftQ = false;
    boolean downS = false;
    boolean rightD = false;
    boolean pause = false;

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    public void settings() {
        //size(800,800);
        fullScreen();
    }

    public void setup() {
        processing =  this;
        background(0);

        this.videoGames = new Jeux();
        this.videoGames.initialisation();
    }

    public void draw() {
        background(0);
        this.videoGames.miseAjours(upZ, leftQ, downS, rightD, pause);
        this.videoGames.affichageJeux(pause);

        //System.out.println("Z : " + this.upZ + " / " + "Q : " + this.leftQ + " / " + "s : " + this.downS + " / " + "d : " + this.rightD);


    }

    public void keyPressed() {
        if (key == 'z') this.upZ = true;
        if (key == 'q') this.leftQ = true;
        if (key == 's') this.downS = true;
        if (key == 'd') this.rightD = true;
        if (key == 'p') this.pause = true;

    }

    public void keyReleased() {
        if (key == 'z') this.upZ = false;
        if (key == 'q') this.leftQ = false;
        if (key == 's') this.downS = false;
        if (key == 'd') this.rightD = false;
        if (key == 'p') this.pause = false;
    }
}
