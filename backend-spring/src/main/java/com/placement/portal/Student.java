package com.placement.portal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A Student row in the database.
 *
 * @Entity tells JPA "this class maps to a database table".
 * @Table(name = "students") points it at the existing `students` table.
 * Each field becomes a column with the same name.
 */
@Entity
@Table(name = "students")
public class Student {

    // Primary key. IDENTITY = let PostgreSQL's SERIAL column generate the id.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double cgpa;
    private String skills;
    private String email;
    private String contact;

    // JPA needs a no-argument constructor.
    public Student() {
    }

    // Getters and setters — JPA uses these to read/write the fields.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
