//package com.softserve.itacademy.model;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//@Table(name = "subjects")
//public class Subject {
//
//    @Id
//    @GeneratedValue
//    private long id;
//
//    @NotBlank(message = "The 'title' cannot be empty")
//    @Column(name = "title", nullable = false)
//    private String title;
//
//
//}
