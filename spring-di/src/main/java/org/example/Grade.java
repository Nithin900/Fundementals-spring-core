package org.example;

import org.springframework.stereotype.Component;

@Component
public class Grade {
    private String subject;
    private String grade;

    public Grade() {
    }

    public Grade(String subject, String grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "subject='" + subject + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
