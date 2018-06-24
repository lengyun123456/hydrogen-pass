package com.hyd.pass.fx;

import com.hyd.pass.model.Tag;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * @author yidin
 */
public class TagListView extends ListView<Tag> {

    public TagListView() {
        setSelectionModel(new NoSelectionModel<>());
        setCellFactory(lv -> new ListCell<Tag>() {
            @Override
            protected void updateItem(Tag item, boolean empty) {
                if (!empty) {
                    this.setGraphic(new Label(item.getName()));
                } else {
                    this.setText(null);
                    this.setGraphic(null);
                }
            }
        });
    }
}
