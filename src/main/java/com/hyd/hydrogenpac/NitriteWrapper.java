package com.hyd.hydrogenpac;

import com.hyd.hydrogenpac.config.DbConfig;
import org.dizitart.no2.Nitrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * (description)
 * created at 2017/8/11
 *
 * @author yidin
 */
@Component
public class NitriteWrapper {

    @Autowired
    private DbConfig dbConfig;

    private Nitrite nitrite;

    Nitrite getNitrite() {
        return nitrite;
    }

    @PostConstruct
    private void init() throws Exception {
        Path dbFilePath = Paths.get(dbConfig.getPath());
        Path parent = dbFilePath.getParent();

        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        this.nitrite = Nitrite.builder()
                .compressed()
                .filePath(dbFilePath.toFile())
                .openOrCreate();
    }

    @PreDestroy
    private void fin() {
        if (this.nitrite != null) {
            this.nitrite.close();
        }
    }
}
