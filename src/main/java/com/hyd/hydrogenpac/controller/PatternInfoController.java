package com.hyd.hydrogenpac.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.util.*;

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

    public void apply(ObservableList<String> list) {
        if (list == null) {
            return;
        }

        List<String> _list = new ArrayList<>(list);
        if (index >= 0 && index < _list.size()) {
            _list.remove(index);
        }

        // 去掉重复内容
        Set<String> distinctPatterns = new HashSet<>(_list);
        distinctPatterns.add(pattern.get());

        _list = new ArrayList<>(distinctPatterns);
        Collections.sort(_list);
        list.setAll(_list);
    }
}
