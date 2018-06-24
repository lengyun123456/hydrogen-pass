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

    @Override
    protected void updateItem(Entry item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(createGraphic(item));
        }
    }

    private Node createGraphic(Entry item) {
        Label title = new Label();
        title.getStyleClass().add("entry-name");
        title.setText(item.getName());

        Label location = new Label();
        location.getStyleClass().add("entry-location");
        location.setText(item.getLocation());

        VBox vBox = new VBox(title, location);
        vBox.getStyleClass().add("entry-card");
        return vBox;
    }
}
