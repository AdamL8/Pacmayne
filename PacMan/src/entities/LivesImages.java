package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class LivesImages {
	private final Image imagelife = new Image("file:ressource/sprites/pacman-r2.png");
	private HBox hbox;
	private ImageView[] livesImages;
	private int nblives;

	public int getNblives() {
		return nblives;
	}

	public void setNblives(int nblives) {
		this.nblives = nblives;
	}

	public LivesImages(Pane pane, int nblives) {
		this.hbox = new HBox();
		this.nblives = nblives;
		livesImages= new ImageView[nblives+1];
		for (int i = 0; i <nblives; i++) {
			livesImages[i] = new ImageView(imagelife);
			hbox.getChildren().add(livesImages[i]);
		}
		pane.getChildren().add(hbox);

	}
    public void addLife() {
     ++nblives;
	livesImages[nblives-1] = new ImageView(imagelife);
	this.hbox.getChildren().add(livesImages[nblives-1]);
	}
		
	public void removeLife() {
	--nblives;
	hbox.getChildren().remove(livesImages[nblives]);
	
	}

}