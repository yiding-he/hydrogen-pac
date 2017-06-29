package com.hyd.hydrogenpac.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiding.he
 */
public class Pac {

    private List<PatternList> patternListSettings = new ArrayList<>();

    public List<PatternList> getPatternListSettings() {
        return patternListSettings;
    }

    public void setPatternListSettings(List<PatternList> patternListSettings) {
        this.patternListSettings = patternListSettings;
    }
}
