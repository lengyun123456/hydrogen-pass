package com.hyd.pass.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiding.he
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Entry extends OrderedItem {

    private String location;

    private String comment;

    private String note;

    private List<Tag> tags = new ArrayList<>();

    private List<Authentication> authentications = new ArrayList<>();

    public Entry(String name, String location, String comment) {
        this.setName(name);
        this.location = location;
        this.comment = comment;
    }

}
