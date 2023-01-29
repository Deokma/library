package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.repo.UserRepository;
import com.deokma.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    private final BooksRepository booksRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public BooksController(BooksRepository booksRepository, UserRepository userRepository, UserService userService) {
        this.booksRepository = booksRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    @RequestMapping(value = "/books", method = RequestMethod.GET)
//    public String booksMain(Model model) {
//        Iterable<Books> books = booksRepository.findAll();
//        model.addAttribute("books", books);
//        return "books-main";
//    }

    /**
     * Go to Add Book Page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/books/add")
    public String booksAdd() {
        return "books-add";
    }

    /**
     * Method for Add Book
     *
     * @param name          Name of Book
     * @param author        Author of Book
     * @param cover         Cover of Book
     * @param view_link     Link for view Book
     * @param download_link Link for download Book
     * @param description   Description of Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/add")
    public String booksAddPost(@RequestParam String name,
                               @RequestParam String author,
                               @RequestParam String cover, @RequestParam String view_link,
                               @RequestParam String download_link,
                               @RequestParam String description) {
        Books books = new Books(name, author, cover, view_link, download_link, description);
        booksRepository.save(books);
        return "redirect:/";
    }

    /**
     * Go to Book page
     */
    @GetMapping("/books/{book_id}")
    public String bookDetails(@PathVariable(value = "book_id") long book_id, Model model) {
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-details";
    }

    /**
     * Go to Edit Book Page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/books/{book_id}/edit")
    public String bookEdit(@PathVariable(value = "book_id") long book_id, Model model) {
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-edit";
    }

    /**
     * Method for edit book parameters
     *
     * @param book_id       Book id
     * @param name          Name of Book
     * @param author        Author of Book
     * @param cover         Cover of Book
     * @param view_link     Link for view Book
     * @param download_link Link for download Book
     * @param description   Description of Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/{book_id}/edit")
    public String bookEditPost(@PathVariable(value = "book_id") long book_id,
                               @RequestParam String name, @RequestParam String author,
                               @RequestParam String cover, @RequestParam String view_link,
                               @RequestParam String download_link, @RequestParam String description) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        books.setName(name);
        books.setAuthor(author);
        books.setCover(cover);
        books.setView_link(view_link);
        books.setDownload_link(download_link);
        books.setDescription(description);
        booksRepository.save(books);
        return "redirect:/";
    }

    /**
     * Method for Delete Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/{book_id}/remove")
    public String bookDeletePost(@PathVariable(value = "book_id") long book_id) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        booksRepository.delete(books);
        return "redirect:/";
    }

    /**
     * Method for adding a book to a user's favorites
     * Add to User - atu
     *
     * @param book_id   Book id
     * @param principal The user who logged in
     * @param request   Current Page request
     */
    @PostMapping("/user/{book_id}/atu")
    public String addBookToUser(@PathVariable(value = "book_id") Long book_id, Principal principal,
                                HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        Books book = booksRepository.findById(book_id).orElseThrow();
        if (!user.getBooks_list().contains(book)) {
            user.getBooks_list().add(book);
        }
        userRepository.save(user);
        return "redirect:" + request.getHeader("referer");
    }

    /**
     * Method for delete a book from a user's favorites
     * Delete from User - dfu
     *
     * @param book_id   Book id
     * @param principal The user who logged in
     * @param request   Current Page request
     */
    @PostMapping("/user/{book_id}/dfu")
    public String DeleteBookFromUser(@PathVariable(value = "book_id") Long book_id, Principal principal,
                                     HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        Books book = booksRepository.findById(book_id).orElseThrow();
        if (user.getBooks_list().contains(book)) {
            user.getBooks_list().remove(book);
        }
        userRepository.save(user);
        return "redirect:" + request.getHeader("referer");
    }
}
