package com.gg.mafia.global.util;

import org.hibernate.dialect.MariaDBDialect;

public class CustomMariaDBDialect extends MariaDBDialect {
    @Override
    public String getCurrentSchemaCommand() {
        return "select 1";
    }
}
