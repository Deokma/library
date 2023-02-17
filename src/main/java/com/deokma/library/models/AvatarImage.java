//package com.deokma.library.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
///**
// * @author Denis Popolamov
// */
//@Data
//@Entity
//@Table(name = "avatar_image")
//public class AvatarImage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long user_id;
//
//    @Lob
//    private byte[] data;
//
//    @Column(name = "file_name")
//    private String fileName;
//
//    @Id
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    // Конструкторы, геттеры и сеттеры
//}
//
