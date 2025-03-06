package ru.samokhin.labCheck.adapter.rest.teacher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherDto {
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
    private Long tgChatId;
}