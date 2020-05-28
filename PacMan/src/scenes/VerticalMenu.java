package scenes;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class VerticalMenu {
	protected VBox menuBox;
	protected int currentItem = 0;
	protected Pane root;
	
	public VerticalMenu(double width, double height) {
		root = new Pane();
		root.setPrefSize(width, height);
		
		menuBox = new VBox(10);
		menuBox.setAlignment(Pos.TOP_CENTER);		
		menuBox.setTranslateX(100);
		menuBox.setTranslateY(100);
		menuBox.setPrefSize(width /2, height);
		menuBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
		        + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
		        + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
		
		root.getChildren().add(menuBox);
	}	
	
	public Parent getContent() {
		return root;
	}
	
	public MenuItem getMenuItem(int index) {
        return (MenuItem)menuBox.getChildren().get(index);
    }	
	
	public MenuItem getCurrentItem() {
		return (MenuItem)menuBox.getChildren().get(currentItem);
	}
	
	public void selectPreviousItem() {
		 if (currentItem > 0) {
             getMenuItem(currentItem).setActive(false);
             getMenuItem(--currentItem).setActive(true);
         }
	}
	
	public void selectNextItem() {
		if (currentItem < menuBox.getChildren().size() - 1) {
            getMenuItem(currentItem).setActive(false);
            getMenuItem(++currentItem).setActive(true);
        }
	}
	
	public void addMenuItem(String itemName, Runnable action) {
		MenuItem item = new MenuItem(itemName);
		item.setOnActivate(action);
		menuBox.getChildren().add(item);
		
		if (menuBox.getChildren().size() == 1) {
			getMenuItem(0).setActive(true);
		}
	}
	
	public void deleteMenuItem() {
		menuBox.getChildren().clear();
	}
}
