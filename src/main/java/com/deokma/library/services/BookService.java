//package com.deokma.library.services;
//
//import com.deokma.library.models.Books;
//import com.deokma.library.models.Image;
//import com.deokma.library.repo.BooksRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * @author Denis Popolamov
// */
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class BookService {
//    private final BooksRepository booksRepository;
//
//    public List<Books> listBooks(String name){
//        if (name != null) return booksRepository.findByName(name);
//        return booksRepository.findAll();
//    }
//
//    private Image toImageEntity(MultipartFile file) throws IOException {
//        Image image = new Image();
//        image.setName(file.getName());
//        image.setOriginalFileName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setSize(file.getSize());
//        image.setBytes(file.getBytes());
//        return image;
//    }
//
//    public void deleteProduct(Long id) {
//        booksRepository.deleteById(id);
//    }
//
//    public Books getProductById(Long id) {
//        return booksRepository.findById(id).orElse(null);
//    }
//}
