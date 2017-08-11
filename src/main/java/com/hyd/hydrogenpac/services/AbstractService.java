package com.hyd.hydrogenpac.services;

import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yiding.he
 */
public abstract class AbstractService {

    @Autowired
    Nitrite nitrite;
}
