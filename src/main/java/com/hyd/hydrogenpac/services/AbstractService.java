package com.hyd.hydrogenpac.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.mapper.JacksonMapper;
import org.dizitart.no2.mapper.NitriteMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yiding.he
 */
public abstract class AbstractService {

    @Autowired
    Nitrite nitrite;

    protected NitriteMapper mapper = new JacksonMapper();
}
