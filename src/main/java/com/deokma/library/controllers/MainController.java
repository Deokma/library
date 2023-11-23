package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.BooksCover;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksCoverRepository;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.services.RecommendationService;
import com.deokma.library.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private BooksRepository booksRepository;
    private BooksCoverRepository booksCoverRepository;
    private Authentication authentication;
    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;
    @Autowired
    public MainController(BooksRepository booksRepository, BooksCoverRepository booksCoverRepository) {
        this.booksRepository = booksRepository;
        this.booksCoverRepository = booksCoverRepository;
    }

    /**
     * Go to main page
     */
    @GetMapping("/")
    public String main(Model model, Authentication authentication) {
        Iterable<Books> books = booksRepository.findAll();
        Iterable<BooksCover> covers = booksCoverRepository.findAll();

        // Проверяем, существует ли имя пользователя в аутентификации
        String username = authentication != null ? authentication.getName() : null;

        if (username != null) {
            User user = userService.findByUsername(username);

            if (user != null && !user.getBooks_list().isEmpty()) {
                // Получите рекомендации для пользователя
                List<Books> recommendedBooks = recommendationService.getRecommendedBooks(user);

                // Передайте рекомендации в модель для отображения на странице
                model.addAttribute("recommendedBooks", recommendedBooks);
            }
        }

        model.addAttribute("books", books);
        model.addAttribute("cover", covers);

        //booksCoverRepository.createTempTable();

        return "home";
    }


}