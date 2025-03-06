package ru.samokhin.labCheck.adapter.rest.student.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudentDto {
    private String firstName;
    private String patronymic;
    private String lastName;
    private String email;
    private String studentCardNumber;
    private String groupName;
    private Long tgChatId;
}
