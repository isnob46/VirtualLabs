package it.polito.ai.virtualLabs.controllers;

import it.polito.ai.virtualLabs.dtos.*;
import it.polito.ai.virtualLabs.dtos.CourseDTO;
import it.polito.ai.virtualLabs.dtos.StudentDTO;
import it.polito.ai.virtualLabs.dtos.VmModelDTO;
import it.polito.ai.virtualLabs.dtos.VmInstanceDTO;
import it.polito.ai.virtualLabs.exceptions.AssignmentNotFoundException;
import it.polito.ai.virtualLabs.exceptions.CourseNotFoundException;
import it.polito.ai.virtualLabs.exceptions.StudentNotFoundException;
import it.polito.ai.virtualLabs.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    TeamService teamService;

    @GetMapping({"", "/"})
    List<StudentDTO> all() {
        return teamService.getAllStudents()
                .stream()
                .map(studentDTO -> ModelHelper.enrich(studentDTO))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    StudentDTO getOne(@PathVariable("id") String id) {
        Optional<StudentDTO> student = teamService.getStudent(id);
        if (student.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, id);
        return ModelHelper.enrich(student.get());
    }


    // solo gli admin o gli user con username=id_studente possono accederci
    @GetMapping("/{id}/courses")
    List<CourseDTO> getCourses(@PathVariable("id") String id) {
        try{
            return teamService.getCourses(id)
                    .stream()
                    .map(courseDTO -> ModelHelper.enrich(courseDTO))
                    .collect(Collectors.toList());

        }catch(StudentNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id);
        }
    }

    @GetMapping("/{id}/course/{courseName}/assignments")
    List<StudentAssignmentDTO> getAllAssignmentsForCourseAndForStudent(@PathVariable("courseName") String courseName, @PathVariable("id") String studentId) {
        try{
            return teamService.getAllAssignmentsForCourseAndForStudent(courseName, studentId);
        } catch (CourseNotFoundException courseNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, courseName);
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }

    @GetMapping("/{id}/assignment/{assignmentId}")
    StudentAssignmentDTO getStudentAssignment(@PathVariable("assignmentId") Long assignmentId, @PathVariable("id") String studentId) {
        try {
            return this.teamService.getStudentAssignment(assignmentId, studentId);
        } catch (AssignmentNotFoundException assignmentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, assignmentId.toString());
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }

    @GetMapping("/{id}/assignment/{assignmentId}/paperSnapshots")
    List<PaperSnapshotDTO> getAllPaperSnapshotsForAssignmentAndForStudent(@PathVariable("assignmentId") Long assignmentId, @PathVariable("id") String studentId) {
        try {
            return teamService.getAllPapersnapshotsForAssignmentAndForStudent(assignmentId, studentId);
        } catch (AssignmentNotFoundException assignmentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, assignmentId.toString());
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }

    @PutMapping("/{id}/assignment/{assignmentId}/updatePaperStatus")
    PaperDTO updatePaperStatus(@PathVariable("id") String studentId, @PathVariable("assignmentId") Long assignmentId, @RequestBody String status) {
        try {
            return teamService.updatePaperStatus(assignmentId, studentId, status);
        } catch (AssignmentNotFoundException assignmentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, assignmentId.toString());
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }


    @GetMapping("/{id}/course/{courseName}/papers")
    List<PaperDTO> getAllPapersForCourseAndForStudent(@PathVariable("courseName") String courseName, @PathVariable("id") String studentId) {
        try {
            return teamService.getAllPaperForCourseAndForStudent(courseName, studentId);
        } catch (CourseNotFoundException courseNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, courseName);
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }

    @PostMapping("{id}/assignment/{assignmentId}/addPaperSnapshot")
    @ResponseStatus(HttpStatus.CREATED)
    PaperSnapshotDTO addPaperSnapshotForAssignmentAndForStudent(@PathVariable("id") String studentId, @PathVariable("assignmentId") Long assignmentId, @RequestBody PaperSnapshotDTO paperSnapshotDTO) {
        try {
            return teamService.addPaperSnapshotForAssignmentAndForStudent(paperSnapshotDTO, assignmentId, studentId);
        } catch (AssignmentNotFoundException assignmentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, assignmentId.toString());
        } catch (StudentNotFoundException studentNotFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, studentId);
        }
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    StudentDTO addStudent(@Valid @RequestBody StudentDTO studentDTO, BindingResult result){
        if(result.hasErrors())  throw new ResponseStatusException(HttpStatus.CONFLICT);
        if(!teamService.addStudent(studentDTO))
            throw new ResponseStatusException(HttpStatus.CONFLICT, studentDTO.getName());
        return ModelHelper.enrich(studentDTO);

    }

    @PostMapping({"/addAll"})
    @ResponseStatus(HttpStatus.CREATED)
    List<Boolean> addStudents(@Valid @RequestBody List<StudentDTO> students, BindingResult result){
        if(result.hasErrors())  throw new ResponseStatusException(HttpStatus.CONFLICT);

        List<Boolean> ris=teamService.addAll(students);

        return ris;
    }

    /* VMs */

    @GetMapping({"/{id}/{team}/vminstances"})
    List<VmInstanceDTO> getVmInstances(@PathVariable("id") String id, @PathVariable("team") String team) {
        return teamService.getVmInstances(id, team);
    }

    @PostMapping({"/{id}/{team}/createvminstance"})
    VmInstanceDTO createVmInstance(@PathVariable("id") String id, @PathVariable("team") String team, @Valid @RequestBody(required = true) VmInstanceDTO vmInstance) {
        return teamService.createVmInstance(id, team, vmInstance);
    }

    @GetMapping({"/{id}/{team}/startvminstance/{idvm}"})
    VmInstanceDTO startVmInstance(@PathVariable("id") String id, @PathVariable("team") String team,@PathVariable("idvm") String idvm) {
        long idvmL;
        try { idvmL = Long.parseLong(idvm); }
        catch(Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, idvm); }
        return teamService.startVmInstance(id, team, idvmL);
    }

    @GetMapping({"/{id}/{team}/stopvminstance/{idvm}"})
    VmInstanceDTO stopVmInstance(@PathVariable("id") String id, @PathVariable("team") String team,@PathVariable("idvm") String idvm) {
        long idvmL;
        try { idvmL = Long.parseLong(idvm); }
        catch(Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, idvm); }
        return teamService.stopVmInstance(id, team, idvmL);
    }

    @GetMapping({"/{id}/{team}/deletevminstance/{idvm}"})
    List<VmInstanceDTO> deleteVmInstance(@PathVariable("id") String id, @PathVariable("team") String team,@PathVariable("idvm") String idvm) {
        long idvmL;
        try { idvmL = Long.parseLong(idvm); }
        catch(Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, idvm); }
        return teamService.deleteVmInstance(id, team, idvmL);
    }

    @PostMapping({"/{id}/{team}/editvminstance/{idvm}"})
    VmInstanceDTO editVmInstance(@PathVariable("id") String id, @PathVariable("team") String team,@PathVariable("idvm") String idvm, @Valid @RequestBody(required = true) VmInstanceDTO vmInstance) {
        long idvmL;
        try { idvmL = Long.parseLong(idvm); }
        catch(Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, idvm); }
        return teamService.editVmInstance(id, team, idvmL, vmInstance);
    }

    @GetMapping("/{id}/{team}/vmmodel")
    VmModelDTO vmConfigurations(@PathVariable("id") String id, @PathVariable("team") String team) {
        return teamService.getVmModel(id, team);
    }

}
