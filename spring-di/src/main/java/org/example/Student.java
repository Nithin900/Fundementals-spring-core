package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;

@Component  // this annotation is use for java config. for component scanning
public class Student {
    private int sid;
    private String studentName;
//     Field injection using java config.
//    @Autowired
    private AddressService addressService;

//    @Autowired
    private List<Course> courses;       // Collection List injection
//    @Autowired
    private Set<String> hobbies;        // Collection Set injection
//    @Autowired
    private Map<String, Grade> grades;  // Collection Map injection
//    @Autowired
    private Friend friend;             // Reference type injection

    public Student() {
    }

//    @Autowired(required = false)
    public Student(int sid, String studentName, AddressService addressService, List<Course> courses, Set<String> hobbies, Map<String, Grade> grades, Friend friend) {
        this.sid = sid;
        this.studentName = studentName;
        this.addressService = addressService;
        this.courses = courses;
        this.hobbies = hobbies;
        this.grades = grades;
        this.friend = friend;
    }

    public Student(int sid, String studentName) {
        this.sid = sid;
        this.studentName = studentName;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    public String getAddress() {
        return addressService.getAddress();
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Autowired
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Set<String> getHobbies() {
        return hobbies;
    }

    @Autowired
    public void setHobbies(Set<String> hobbies) {
        this.hobbies = hobbies;
    }

    public Map<String, Grade> getGrades() {
        return grades;
    }

    @Autowired
    public void setGrades(Map<String, Grade> grades) {
        this.grades = grades;
    }

    public Friend getFriend() {
        return friend;
    }

    @Autowired
    public void setFriend(Friend friend) {
        this.friend = friend;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SID: ").append(sid).append("\n");
        stringBuilder.append("Name: ").append(studentName).append("\n");
        stringBuilder.append("Address: ").append(addressService != null ? addressService.getAddress() : "null").append("\n");
        stringBuilder.append("Hobbies: ").append(hobbies != null ? hobbies.toString() : "null").append("\n");
        stringBuilder.append("Courses: ").append(courses != null ? courses.toString() : "null").append("\n");
        stringBuilder.append("Grades: ").append(grades != null ? grades.toString() : "null").append("\n");
        stringBuilder.append("Friend: ").append(friend != null ? friend.toString() : "null");
        return stringBuilder.toString();
    }

}
