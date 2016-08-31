package ru.andreymarkelov.interview.infobip.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@SuppressWarnings("unused")
@Converter(autoApply = true)
public class InstantPersistenceConverter implements AttributeConverter<Instant, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(Instant entityValue) {
        return entityValue == null ? null : Timestamp.from(entityValue);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp databaseValue) {
        return databaseValue == null ? null : databaseValue.toInstant();
    }
}
