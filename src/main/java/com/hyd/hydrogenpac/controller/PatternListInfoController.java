package com.hyd.hydrogenpac.controller;

import com.hyd.hydrogenpac.model.PatternList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PatternListInfoController {

    public TextField txtName;

    public ComboBox cmbProxy;

    private PatternList patternList;

    public void initialize() {
        this.txtName.textProperty().bindBidirectional(this.patternList.nameProperty());
    }

    public void setPatternList(PatternList patternList) {
        this.patternList = patternList;
    }

    public PatternList getPatternList() {
        return patternList;
    }
}
