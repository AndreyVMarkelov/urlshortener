package ru.andreymarkelov.interview.infobip.util;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.h2.jdbc.JdbcSQLException;

import java.util.stream.Stream;

public class DatabaseUtil {
    private static final String UNIQUE_VIOLATION_STATE = "23505";

    private DatabaseUtil() {
    }

    public static boolean isDatabaseConstraintViolationException(Throwable th) {
        return Stream.of(ExceptionUtils.getThrowables(th))
                .filter(e -> e.getClass() == JdbcSQLException.class)
                .map(e -> (JdbcSQLException) e)
                .filter(e -> UNIQUE_VIOLATION_STATE.equals(e.getSQLState()))
                .findFirst()
                .isPresent();
    }
}
