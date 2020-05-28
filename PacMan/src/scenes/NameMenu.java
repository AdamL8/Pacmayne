package scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class NameMenu {
	protected HBox letterBox;
	protected int[] currentLetterIndexes = {0, 0, 0};
	protected int currentItem = 0;
	protected Pane root;
	
	private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	private String[] name = {"A", "A", "A"};
	
	
	public NameMenu(double width, double height) {
		root = new Pane();
		root.setPrefSize(width, height);
		
		letterBox = new HBox(10);
		letterBox.setPadding(new Insets(10, 10, 10, 10));
		letterBox.setSpacing(10);
		letterBox.setAlignment(Pos.TOP_CENTER);
		letterBox.setTranslateX(100);
		letterBox.setTranslateY(100);
		letterBox.setPrefSize(width/2, height);
		letterBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
		        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
		
		
		root.getChildren().add(letterBox);
	}
	
	public Parent getContent() {
		return root;
	}
	
	public MenuItem getMenuItem(int index) {
        return (MenuItem)letterBox.getChildren().get(index);
    }	
	
	public MenuItem getCurrentItem() {
		return (MenuItem)letterBox.getChildren().get(currentItem);
	}
	
	public void selectPreviousItem() {
		 if (currentItem > 0) {
             getMenuItem(currentItem).setActive(false);
             getMenuItem(--currentItem).setActive(true);
         }
	}
	
	public void selectNextItem() {
		if (currentItem < letterBox.getChildren().size() - 1) {
            getMenuItem(currentItem).setActive(false);
            getMenuItem(++currentItem).setActive(true);
        }
	}
	
	public void addMenuItem(String itemName, Runnable action) {
		MenuItem item = new MenuItem(itemName);
		item.setOnActivate(action);
		letterBox.getChildren().add(item);
		
		if (letterBox.getChildren().size() == 1) {
			getMenuItem(0).setActive(true);
		}
	}
		
	
	public String getLetter(int index) {
		return letters[index];
	}
	
	public void selectPreviousLetter() {
		if(currentLetterIndexes[currentItem] > 0) {
			currentLetterIndexes[currentItem]--;
		}
		getMenuItem(currentItem).changeText(letters[currentLetterIndexes[currentItem]]);
		name[currentItem] = letters[currentLetterIndexes[currentItem]];
	}

	public void selectNextLetter() {
		if(currentLetterIndexes[currentItem] < letters.length) {
			currentLetterIndexes[currentItem]++;
		}
        getMenuItem(currentItem).changeText(letters[currentLetterIndexes[currentItem]]);
        name[currentItem] = letters[currentLetterIndexes[currentItem]];
	}
	
	public String getName() {
		return name[0] + name[1] + name[2];
	}
	
}
