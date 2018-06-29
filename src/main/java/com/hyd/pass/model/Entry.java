package com.hyd.pass.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yiding.he
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Entry extends OrderedItem {

    private StringProperty location = new SimpleStringProperty();

    private StringProperty comment = new SimpleStringProperty();

    private StringProperty note = new SimpleStringProperty();

    private BooleanProperty highlighted = new SimpleBooleanProperty();

    private ObservableList<Tag> tags = FXCollections.emptyObservableList();

    private ObservableList<Authentication> authentications = FXCollections.emptyObservableList();

    public Entry(String name, String location, String comment) {
        this.setName(name);
        this.location.set(location);
        this.comment.set(comment);
    }

    public boolean isHighlighted() {
        return highlighted.get();
    }

    public BooleanProperty highlightedProperty() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted.set(highlighted);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getComment() {
        return comment.get();
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = FXCollections.observableArrayList(tags);
    }

    public List<Authentication> getAuthentications() {
        return authentications;
    }

    public void setAuthentications(List<Authentication> authentications) {
        this.authentications = FXCollections.observableArrayList(authentications);
    }

    ////////////////////////////////////////////////////////////

    public void highlightIfMatch(String keyword) {
        setHighlighted(matchKeyword(keyword));
    }

    private boolean matchKeyword(String keyword) {
        return getName().contains(keyword) || getLocation().contains(keyword) || getComment().contains(keyword);
    }
}
