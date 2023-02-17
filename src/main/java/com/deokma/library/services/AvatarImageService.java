//package com.deokma.library.services;
//
//import com.deokma.library.models.AvatarImage;
//import com.deokma.library.repo.AvatarImageRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * @author Denis Popolamov
// */
//@Service
//public class AvatarImageService {
//
//    private final AvatarImageRepository avatarImageRepository;
//
//    @Autowired
//    public AvatarImageService(AvatarImageRepository avatarImageRepository) {
//        this.avatarImageRepository = avatarImageRepository;
//    }
//
//    public AvatarImage findById(Long id) {
//        return avatarImageRepository.findById(id).orElse(null);
//    }
//}