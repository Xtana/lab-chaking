package ru.samokhin.labCheck.adapter.rest.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.samokhin.labCheck.adapter.rest.teacher.dto.CreateTeacherDto;
import ru.samokhin.labCheck.app.api.teacher.CreateTeacherInbound;

@RestController
@RequestMapping("/lab-check/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final CreateTeacherInbound createTeacherInbound;

    @PostMapping()
    public ResponseEntity<String> createStudent(@RequestBody CreateTeacherDto createTeacherDto) {
        boolean isTeacherCreated = createTeacherInbound.execute(
                createTeacherDto.getFirstName(),
                createTeacherDto.getPatronymic(),
                createTeacherDto.getLastName(),
                createTeacherDto.getEmail(),
                createTeacherDto.getTgChatId());
        if (!isTeacherCreated) {
            return ResponseEntity.badRequest().body("Teacher create error");
        }
        return ResponseEntity.ok("Teacher created!");
    }

}
