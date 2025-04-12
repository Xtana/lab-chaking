package ru.samokhin.labCheck.app.api.test;

import ru.samokhin.labCheck.domain.test.Test;

public interface FindTestByIdInbound {
    Test execute(Long id);
}
