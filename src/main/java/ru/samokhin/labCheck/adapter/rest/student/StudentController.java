package ru.samokhin.labCheck.adapter.rest.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.samokhin.labCheck.adapter.rest.student.dto.CreateStudentDto;
import ru.samokhin.labCheck.app.api.student.CreateStudentInbound;

@RestController
@RequestMapping("/lab-check/students")
@RequiredArgsConstructor
public class StudentController {
    private final CreateStudentInbound createStudentInbound;

    @PostMapping()
    public ResponseEntity<String> createStudent(@RequestBody CreateStudentDto createStudentDto) {
        boolean isStudentCreated = createStudentInbound.execute(
                createStudentDto.getFirstName(),
                createStudentDto.getPatronymic(),
                createStudentDto.getLastName(),
                createStudentDto.getGroupName(),
                createStudentDto.getEmail(),
                createStudentDto.getStudentCardNumber(),
                createStudentDto.getTgChatId());
        if (!isStudentCreated) {
            return ResponseEntity.badRequest().body("Student create error");
        }
        return ResponseEntity.ok("Student created!");
    }
}
