package com.hyd.hydrogenpac.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

import java.util.Collections;
import java.util.List;

/**
 * @author yiding_he
 */
public class PatternInfoController {

    public TextField txtPattern;

    private int index;

    private StringProperty pattern = new SimpleStringProperty();

    public void initialize() {
        txtPattern.textProperty().bindBidirectional(this.pattern);
    }

    public void setPattern(int index, String pattern) {
        this.index = index;
        this.pattern.set(pattern);
    }

    public void apply(List<String> list) {
        if (list == null) {
            return;
        }

        if (index == -1) {
            list.add(pattern.get());
        } else {
            list.set(index, pattern.get());
        }

        Collections.sort(list);
    }
}
