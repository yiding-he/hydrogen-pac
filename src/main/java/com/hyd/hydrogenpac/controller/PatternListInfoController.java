package com.hyd.hydrogenpac.controller;

import com.hyd.fx.builders.ComboBoxBuilder;
import com.hyd.fx.cells.ListCellFactory;
import com.hyd.hydrogenpac.AppContext;
import com.hyd.hydrogenpac.model.PatternList;
import com.hyd.hydrogenpac.model.Proxy;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class PatternListInfoController {

  public TextField txtName;

  public ComboBox<Proxy> cmbProxy;

  private PatternList patternList;

  public void initialize() {
    this.txtName.textProperty().bindBidirectional(this.patternList.nameProperty());

    ComboBoxBuilder.of(this.cmbProxy)
        .setCellFactory(new ListCellFactory<Proxy>()
            .withTextProperty(Proxy::nameProperty)
        )
        .setItems(AppContext.APP_CONTEXT.getPacConfiguration().getProxyList())
        .setInitialValue(proxy -> proxy.getName().equals(patternList.getProxyName()))
        .setOnChange(proxy -> this.patternList.setProxyName(proxy.getName()));
  }

  public void setPatternList(PatternList patternList) {
    this.patternList = patternList;
  }

  public PatternList getPatternList() {
    return patternList;
  }
}
