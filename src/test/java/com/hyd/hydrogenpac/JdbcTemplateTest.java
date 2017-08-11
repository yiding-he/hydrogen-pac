package com.hyd.hydrogenpac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTemplateTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void testSelectTables() throws Exception {
        List<Map<String, Object>> list = jdbcTemplate.query(

                "SELECT count(*) cnt " +
                        "FROM sqlite_master WHERE type='table' AND name='table_name'",

                (resultSet, i) -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("cnt", resultSet.getBigDecimal("cnt").intValue());
                    return result;
                });

        list.forEach(System.out::println);
    }
}
