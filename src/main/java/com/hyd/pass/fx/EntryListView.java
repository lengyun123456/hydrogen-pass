package com.hyd.pass.fx;

import com.hyd.pass.model.Entry;
import javafx.scene.control.ListView;


/**
 * @author yiding.he
 */
public class EntryListView extends ListView<Entry> {

    public EntryListView() {
        setCellFactory(lv -> new EntryListCell());
    }
}
