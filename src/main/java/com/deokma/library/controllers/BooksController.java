package com.deokma.library.controllers;

import com.deokma.library.exceptions.ResourceNotFoundException;
import com.deokma.library.models.Books;
import com.deokma.library.models.BooksPDF;
import com.deokma.library.models.User;
import com.deokma.library.repo.BooksPdfRepository;
import com.deokma.library.repo.BooksRepository;
import com.deokma.library.repo.UserRepository;
import com.deokma.library.services.BooksPDFService;
import com.deokma.library.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    private final BooksRepository booksRepository;
    private final BooksPDFService bookPdfService;
    private final BooksPdfRepository booksPDFRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public BooksController(BooksRepository booksRepository, BooksPDFService bookPdfService, BooksPdfRepository booksPDFRepository, UserRepository userRepository, UserService userService) {
        this.booksRepository = booksRepository;
        this.bookPdfService = bookPdfService;
        this.booksPDFRepository = booksPDFRepository;
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

    @Value("${file.upload-books-dir}")
    private String uploadDirectory;

    /**
     * Method for Add Book
     *
     * @param name        Name of Book
     * @param author      Author of Book
     * @param cover       Cover of Book
     * @param description Description of Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/add")
    public String booksAddPost(@RequestParam String name, @RequestParam String author, @RequestParam String cover, @RequestParam String description, @RequestParam("book_file") MultipartFile file, Model model) {
        //Books books = new Books(name, author, cover, view_link, download_link, description);
        Books book = new Books();
        try {
            if (!file.getOriginalFilename().matches(".*\\.pdf$")) {
                throw new Exception("Invalid file type. Only PDF files are allowed.");
            }
            book.setName(name);
            book.setAuthor(author);
            book.setCover(cover);
            book.setDescription(description);

//            String filePath = uploadDirectory + file.getOriginalFilename();
//
//            // Save the file to the specified directory
//            java.io.File dest = new java.io.File(filePath);
//            file.transferTo(dest);
            booksRepository.save(book);

            BooksPDF books_pdf = new BooksPDF();
            books_pdf.setId(book.getBook_id());
            books_pdf.setFileName(file.getOriginalFilename());
            books_pdf.setData(file.getBytes());
            booksPDFRepository.save(books_pdf);

            model.addAttribute("message", "File uploaded successfully! to ");
            //System.out.println("File uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "File upload failed! " + e.getMessage());
            //System.out.println("File upload failed!");
        }


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
     * @param book_id     Book id
     * @param name        Name of Book
     * @param author      Author of Book
     * @param cover       Cover of Book
     * @param description Description of Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/{book_id}/edit")
    public String bookEditPost(@PathVariable(value = "book_id") long book_id, @RequestParam String name, @RequestParam String author, @RequestParam String cover, @RequestParam String description) {
        Books books = booksRepository.findById(book_id).orElseThrow();
        books.setName(name);
        books.setAuthor(author);
        books.setCover(cover);
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
    public String addBookToUser(@PathVariable(value = "book_id") Long book_id, Principal principal, HttpServletRequest request) {
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
    public String DeleteBookFromUser(@PathVariable(value = "book_id") Long book_id, Principal principal, HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        Books book = booksRepository.findById(book_id).orElseThrow();
        if (user.getBooks_list().contains(book)) {
            user.getBooks_list().remove(book);
        }
        userRepository.save(user);
        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws IOException {
        BooksPDF bookPdf = bookPdfService.getBookPdfById(id);
        if (bookPdf == null) {
            throw new ResourceNotFoundException("Book PDF with id " + id + " not found.");
        }

        ByteArrayResource resource = new ByteArrayResource(bookPdf.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + bookPdf.getFileName())
                .contentLength(bookPdf.getData().length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<InputStreamResource> getBookPdf(@PathVariable Long id) throws ResourceNotFoundException {
        BooksPDF bookPdf = bookPdfService.getBookPdfById(id);

        if (bookPdf == null) {
            throw new ResourceNotFoundException("Book pdf with id: " + id + " not found");
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bookPdf.getData());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + bookPdf.getFileName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
