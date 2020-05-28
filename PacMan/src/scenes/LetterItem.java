package scenes;

import javafx.geometry.Pos;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LetterItem extends HBox{
	private static final Font FONT = Font.font("", FontWeight.BOLD, 18);
	private Text text;
	private Runnable script;

	public LetterItem(String name) {
		super(15);
		this.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
		        + "-fx-border-radius: 5;" + "-fx-border-color: red;");
		setAlignment(Pos.CENTER);

		text = new Text(name);
		text.setText(name);
		text.setFont(FONT);
		text.setEffect(new GaussianBlur(2));
		
		this.getChildren().add(text);

		setActive(false);
		setOnActivate(() -> System.out.println(name + " activated"));
	}

	public void setActive(boolean b) {
		text.setFill(b ? Color.WHITE : Color.GREY);
	}

	public void setOnActivate(Runnable r) {
		script = r;
	}

	public void activate() {
		if (script != null)
			script.run();
	}
}
