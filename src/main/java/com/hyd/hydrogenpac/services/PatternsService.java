package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Patterns;
import com.hyd.hydrogenpac.beans.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PatternsService extends AbstractService {

    public List<Patterns> getPatterns(User user) {
        return Collections.emptyList();
    }

    public void addPatterns(User user, String name) {
        if (user == null) {
            return;
        }

        // TODO
    }
}
