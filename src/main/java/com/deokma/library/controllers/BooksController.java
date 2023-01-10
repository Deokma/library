package com.deokma.library.controllers;

import com.deokma.library.models.Books;
import com.deokma.library.repo.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    @Autowired
    private BooksRepository booksRepository;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String booksMain(Model model) {
        Iterable<Books> books = booksRepository.findAll();
        model.addAttribute("books", books);
        return "books-main";
    }

    @RequestMapping(value = "/books/add", method = RequestMethod.GET)
    public String booksAdd(Model model) {
        return "books-add";
    }

    @RequestMapping(value = "/books/add", method = RequestMethod.POST)
    public String booksAddPost(@RequestParam String name, @RequestParam String author,
                               @RequestParam String cover, @RequestParam String view_link,
                               @RequestParam String download_link, Model model) {
        Books books = new Books(name, author, cover, view_link, download_link);
        booksRepository.save(books);
        return "redirect:/books";
    }

    @RequestMapping(value = "/books/{book_id}", method = RequestMethod.GET)
    public String bookDetails(@PathVariable(value = "book_id") long book_id, Model model) {
        if (!booksRepository.existsById(book_id)) {
            return "redirect:/error";
        }
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-details";
    }

    @RequestMapping(value = "/books/{book_id}/edit", method = RequestMethod.GET)
    public String bookEdit(@PathVariable(value = "book_id") long book_id, Model model) {
        if (!booksRepository.existsById(book_id)) {
            return "redirect:/error";
        }
        Optional<Books> book = booksRepository.findById(book_id);
        ArrayList<Books> res = new ArrayList<>();
        book.ifPresent(res::add);
        model.addAttribute("book", res);
        return "book-edit";
    }

    @RequestMapping(value = "/books/{book_id}/edit", method = RequestMethod.POST)
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
        return "redirect:/books";
    }

    @RequestMapping(value = "/books/{book_id}/remove", method = RequestMethod.POST)
    public String bookRemovePost(@PathVariable(value = "book_id") long book_id, Model model) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        booksRepository.delete(books);
        return "redirect:/books";
    }
}
