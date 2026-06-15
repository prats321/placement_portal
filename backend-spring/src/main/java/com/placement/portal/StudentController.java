package com.placement.portal;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the HTTP requests. @RestController means every method returns the
 * response body directly (here: plain text, exactly like the original server).
 *
 * @CrossOrigin(origins = "*") allows the React app (localhost:3000) to call us
 * (localhost:8080) — this one line replaces all the manual CORS headers.
 */
@RestController
@CrossOrigin(origins = "*")
public class StudentController {

    // Spring "injects" the repository for us (constructor injection).
    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    // ================= REGISTER =================
    // POST /register  with body:  name,cgpa,skills,email,contact
    // consumes = text/plain matches what the browser sends for a string fetch body.
    @PostMapping(value = "/register", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String register(@RequestBody String body) {
        String[] parts = body.split(",");

        Student s = new Student();
        s.setName(parts[0]);
        s.setCgpa(Double.parseDouble(parts[1]));
        s.setSkills(parts[2]);
        s.setEmail(parts[3]);
        s.setContact(parts[4]);

        repo.save(s);          // INSERT done for us by JPA
        return "Student Saved";
    }

    // ================= GET ALL =================
    // GET /students
    @GetMapping("/students")
    public String students() {
        return format(repo.findAll());
    }

    // ================= SEARCH =================
    // GET /search?skill=java&cgpa=8.0   (both params optional)
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String skill,
                        @RequestParam(defaultValue = "0") double cgpa) {
        return format(repo.search(skill, cgpa));
    }

    /**
     * Turns a list of students into the same pipe-delimited text the old
     * backend produced, so the existing frontend keeps working unchanged.
     */
    private String format(List<Student> list) {
        StringBuilder sb = new StringBuilder();
        for (Student s : list) {
            sb.append(s.getName())
              .append(" | CGPA: ").append(s.getCgpa())
              .append(" | Skills: ").append(s.getSkills())
              .append(" | Email: ").append(s.getEmail())
              .append(" | Contact: ").append(s.getContact())
              .append("\n");
        }
        return sb.toString();
    }
}
