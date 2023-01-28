package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.repo.UserRepository;
import com.deokma.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired

    private UserService userService;

//    @RequestMapping(value = "/books", method = RequestMethod.GET)
//    public String booksMain(Model model) {
//        Iterable<Books> books = booksRepository.findAll();
//        model.addAttribute("books", books);
//        return "books-main";
//    }

    @PreAuthorize("hasAuthority('ADMIN')")
    //@RequestMapping(value = "/books/add", method = RequestMethod.GET)
    @GetMapping("/books/add")
    public String booksAdd(Model model) {
        return "books-add";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    //@RequestMapping(value = "/books/add", method = RequestMethod.POST)
    @PostMapping("/books/add")
    public String booksAddPost(@RequestParam String name,
                               @RequestParam String author,
                               @RequestParam String cover, @RequestParam String view_link,
                               @RequestParam String download_link,
                               @RequestParam String description) {
        Books books = new Books(name, author, cover, view_link, download_link, description);

        booksRepository.save(books);
//        return "redirect:/books";
        return "redirect:/";
    }

    //@RequestMapping(value = "/books/{book_id}", method = RequestMethod.GET)
    @GetMapping("/books/{book_id}")
    public String bookDetails(@PathVariable(value = "book_id") long book_id, Model model) {
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-details";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    //@RequestMapping(value = "/books/{book_id}/edit", method = RequestMethod.GET)
    @GetMapping("/books/{book_id}/edit")
    public String bookEdit(@PathVariable(value = "book_id") long book_id, Model model) {
//        if (!booksRepository.existsById(book_id)) {
//            return "redirect:/error";
//        }
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // @RequestMapping(value = "/books/{book_id}/edit", method = RequestMethod.POST)
    @PostMapping("/books/{book_id}/edit")
    public String bookEditPost(@PathVariable(value = "book_id") long book_id,
                               @RequestParam String name, @RequestParam String author,
                               @RequestParam String cover, @RequestParam String view_link,
                               @RequestParam String download_link, Model model) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        books.setName(name);
        books.setAuthor(author);
        books.setCover(cover);
        books.setView_link(view_link);
        books.setDownload_link(download_link);
        booksRepository.save(books);
//        return "redirect:/books";
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    // @RequestMapping(value = "/books/{book_id}/remove", method = RequestMethod.POST)
    @PostMapping("/books/{book_id}/remove")
    public String bookRemovePost(@PathVariable(value = "book_id") long book_id, Model model) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        booksRepository.delete(books);
//        return "redirect:/books";
        return "redirect:/";
    }

    //Add book to account
    //Add to User - atu
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

    //Delete from User - dfu
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
