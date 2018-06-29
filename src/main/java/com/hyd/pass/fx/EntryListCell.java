package com.hyd.pass.fx;

import com.hyd.pass.model.Entry;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 * @author yiding.he
 */
public class EntryListCell extends ListCell<Entry> {

    private VBox root;

    public EntryListCell() {
        selectedProperty().addListener((_ob, _old, _new) -> {
            if (root != null) {
                if (_new) {
                    root.getStyleClass().add("selected");
                } else {
                    root.getStyleClass().remove("selected");
                }
            }
        });
    }

    @Override
    protected void updateItem(Entry item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.root = null;
            setGraphic(null);
        } else {
            this.root = new VBox();
            setGraphic(createGraphic(item));
            item.highlightedProperty().addListener((_ob, _old, _new) -> {
                if (_new) {
                    this.root.getStyleClass().add("highlighted");
                } else {
                    this.root.getStyleClass().remove("highlighted");
                }
            });
        }
    }

    private Node createGraphic(Entry item) {
        Label title = new Label();
        title.getStyleClass().add("entry-name");
        title.setText(item.getName());

        Label location = new Label();
        location.getStyleClass().add("entry-location");
        location.setText(item.getLocation());

        root.getChildren().setAll(title, location);
        root.getStyleClass().add("entry-card");
        return root;
    }
}
