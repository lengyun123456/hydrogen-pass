package com.hyd.pass.fx;

import com.hyd.fx.cells.TreeViewHelper;
import com.hyd.pass.model.Category;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * (description)
 * created at 2018/2/6
 *
 * @author yidin
 */
public class CategoryTreeView extends TreeView<Category> {

    public CategoryTreeView() {
        init();
    }

    public CategoryTreeView(TreeItem<Category> root) {
        super(root);
        init();
    }

    private void init() {
        this.setCellFactory(tv -> new CategoryTreeCell());
        this.setEditable(true);
    }

    public void deleteTreeItem(TreeItem<Category> treeItem) {
        TreeViewHelper.iterate(this, item -> {
            if (item.getChildren().contains(treeItem)) {
                item.getChildren().remove(treeItem);
                return false;
            } else {
                return true;
            }
        });
    }
}
