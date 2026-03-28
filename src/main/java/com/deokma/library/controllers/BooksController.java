package com.deokma.library.controllers;

import com.deokma.library.exceptions.CustomNotFoundException;
import com.deokma.library.models.*;
import com.deokma.library.repo.*;
import com.deokma.library.services.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Denis Popolamov
 */
@Controller
public class BooksController {

    private final BooksRepository booksRepository;
    private final GenreRepository genreRepository;
    private final BooksCoverRepository booksCoverRepository;
    private final BooksCoverService booksCoverService;
    private final BooksPDFService bookPdfService;
    private final BooksPdfRepository booksPDFRepository;
    private final BooksService booksService;
    //private final UserBooksRepository userBooksRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GenreService genreService;
    @Autowired
    private UserBooksRatingRepository userBooksRatingRepository;

    public BooksController(BooksRepository booksRepository, GenreRepository genreRepository, BooksCoverRepository booksCoverRepository, BooksCoverService booksCoverService, BooksPDFService bookPdfService, BooksPdfRepository booksPDFRepository, BooksService booksService, UserRepository userRepository, UserService userService, GenreService genreService) {
        this.booksRepository = booksRepository;
        this.genreRepository = genreRepository;
        this.booksCoverRepository = booksCoverRepository;
        this.booksCoverService = booksCoverService;
        this.bookPdfService = bookPdfService;
        this.booksPDFRepository = booksPDFRepository;
        this.booksService = booksService;
        //this.userBooksRepository = userBooksRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.genreService = genreService;
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
    public String booksAdd(Model model) {


        List<Genre> genres = genreService.getAllGenres(); // предполагается, что у вас есть метод для получения всех жанров из сервиса

        model.addAttribute("genres", genres);
        return "books-add";
    }

    @Value("${file.upload-books-dir}")
    private String uploadBookDirectory;
    @Value("${file.upload-book-covers-dir}")
    private String uploadBookCoversDirectory;

    /**
     * Method for Add Book
     *
     * @param name        Name of Book
     * @param author      Author of Book
     * @param description Description of Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/books/add")
    public String booksAddPost(@RequestParam String name,
                               @RequestParam String author,
                               @RequestParam Integer issueYear,
                               @RequestParam Integer pageCount,
                               @RequestParam String description,
                               @RequestParam(value = "bookCover", required = false) MultipartFile bookCoverFile,
                               @RequestParam(value = "genres", required = false) List<Long> genreIds,
                               Model model) {
        Books book = new Books();
        try {
            if (!bookCoverFile.getOriginalFilename().matches(".*\\.(png|jpg|jpeg)$")) {
                throw new Exception("Invalid file type. Only PNG files are allowed.");
            }
            book.setName(name);
            book.setAuthor(author);
            book.setIssueYear(issueYear);
            book.setPageCount(pageCount);
            book.setDescription(description);

            if (genreIds != null) {
                Set<Genre> selectedGenres = genreIds.stream()
                        .map(genreId -> genreRepository.findById(genreId).orElse(null))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                book.setGenres(selectedGenres);
            }
            model.addAttribute("genres", genreRepository.findAll());

            booksRepository.save(book);
            if (!bookCoverFile.isEmpty()) {
                BooksCover booksCover = new BooksCover();
                booksCover.setId(book.getBook_id());
                booksCover.setFileName(book.getBook_id().toString());
                booksCover.setOriginalFileName(bookCoverFile.getOriginalFilename());

                // Save the image file to the "resources/book_covers" directory
                File fileToSaveBookCover = new File(uploadBookCoversDirectory + book.getBook_id().toString() + ".png");
                FileOutputStream fosBookCover = new FileOutputStream(fileToSaveBookCover);
                fosBookCover.write(bookCoverFile.getBytes());
                fosBookCover.close();

                // Cache the image file
                Cache cache = CacheBuilder.newBuilder()
                        .maximumSize(100)
                        .expireAfterAccess(1, TimeUnit.HOURS)
                        .build();
                String fileContents = new String(Files.readAllBytes(fileToSaveBookCover.toPath()), StandardCharsets.UTF_8);
                cache.put(book.getBook_id().toString(), fileContents);

                booksCoverRepository.save(booksCover);
            }

            model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("el", new Books()); // Assuming you are using "el" as the model attribute for a single book

            model.addAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "File upload failed! " + e.getMessage());
            return "redirect:/books/add";
        }

        return "redirect:/";
    }


//    /**
//     * Converter link to file
//     *
//     * @param coverImageUrl
//     * @return
//     * @throws IOException
//     */
//    public static byte[] convert(String coverImageUrl) throws IOException {
//        URL url = new URL(coverImageUrl);
//        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = rbc.read(ByteBuffer.wrap(buffer))) != -1) {
//            baos.write(buffer, 0, bytesRead);
//        }
//        return baos.toByteArray();
//    }

    /**
     * Go to Book page
     */
    @GetMapping("/books/{book_id}")
    public String bookDetails(@PathVariable(value = "book_id") long book_id, Model model) {
        Optional<Books> book = booksRepository.findById(book_id);
        book.ifPresent(value -> model.addAttribute("book", value));
        model.addAttribute("genres", genreRepository.findAll()); // Add this line to load genres
        return "book-details";
    }


    /**
     * Go to Edit Book Page
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/book/edit/{book_id}")
    public String bookEdit(@PathVariable(value = "book_id") long book_id, Model model) {
        Books book = booksRepository.findById(book_id)
                .orElseThrow(() -> new CustomNotFoundException("Book not found with id " + book_id));

        // Загрузите все жанры из базы данных
        Iterable<Genre> genres = genreRepository.findAll();

        // Передайте жанры в модель
        model.addAttribute("genres", genres);
        model.addAttribute("book", book);

        return "book-edit";
    }

    ////////////////////////////////////////////////////
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/book/edit/{book_id}")
    public String updateBook(@PathVariable("book_id") long book_id,
                             @ModelAttribute("book") @Validated Books book,
                             //BindingResult result,
                             @RequestParam("coverImageFile") MultipartFile coverImageFile,
                             //@RequestParam("pdfFile") MultipartFile pdfFile,
            /*@RequestParam("imageURL") String imageURL,*/
                             @RequestParam(value = "genres", required = false) List<Long> genreIds,
                             RedirectAttributes redirectAttributes, Model model) throws IOException {

        Books existingBook = booksRepository.findById(book_id)
                .orElseThrow(() -> new CustomNotFoundException("Book not found with id " + book_id));
        BooksCover existCover = booksCoverRepository.findById(book_id)
                .orElseThrow(() -> new CustomNotFoundException("Cover not found with id " + book_id));
        //BooksPDF existPDF = booksPDFRepository.findById(book_id)
        //        .orElseThrow(() -> new CustomNotFoundException("Cover not found with id " + book_id));

        // update book information from the form
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setIssueYear(book.getIssueYear());
        existingBook.setPageCount(book.getPageCount());
        existingBook.setDescription(book.getDescription());
        if (genreIds != null) {
            Set<Genre> selectedGenres = genreIds.stream()
                    .map(genreId -> genreRepository.findById(genreId).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            existingBook.setGenres(selectedGenres);
        }
        model.addAttribute("genres", genreRepository.findAll());
        // update book cover image
        if (!coverImageFile.isEmpty()) {
            String coverImageFileName = StringUtils.cleanPath(coverImageFile.getOriginalFilename());

            if (!coverImageFileName.matches(".*\\.(png|jpg|jpeg|gif)$")) {
                redirectAttributes.addFlashAttribute("error", "Invalid cover image file type. Only PNG, JPG, JPEG, and GIF files are allowed.");
                return "redirect:/book-edit/" + book_id;
            }

            // Получаем полный путь до файла в директории
         /*   String fileBooksPath = Paths.get(uploadBookDirectory, existingBook.getBook_id().toString()).toString();
            File oldBookFile = new File(fileBooksPath);

            // Удаляем старый файл
            if (oldBookFile.exists()) {
                oldBookFile.delete();
            }*/

     /*   if (!coverImageFile.isEmpty()) {
            existCover.setData(coverImageFile.getBytes());
            existCover.setFileName(coverImageFile.getOriginalFilename());
        } else if (!imageURL.isEmpty()) {
            existCover.setData(convert(imageURL));
        } else {
            existCover.setData(convert("https://clck.ru/33LDY5"));
        }
        }*/
        }
        // Закомментирована секция, относящаяся к PDF-файлу
    /*
    if (!pdfFile.isEmpty()) {
        String pdfFileName = existingBook.getBook_id().toString();
        String pdfOriginalFileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());

        if (!pdfOriginalFileName.matches(".*\\.pdf$")) {
            redirectAttributes.addFlashAttribute("error", "Invalid PDF file type. Only PDF files are allowed.");
            return "redirect:/book-edit/" + book_id;
        }

        if (!pdfFile.isEmpty()) {
            //existPDF.setData(pdfFile.getBytes());
            existPDF.setOriginalFileName(pdfFile.getOriginalFilename());
            existPDF.setFileName(book.getBook_id().toString());
        }
    }
    */

        booksRepository.save(existingBook);
        booksCoverRepository.save(existCover);
        // Закомментирована секция, относящаяся к PDF-файлу
        //booksPDFRepository.save(existPDF);

        return "redirect:/books/{book_id}";
    }


    /**
     * Method for Delete Book
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/book/remove/{book_id}")
    public String bookDeletePost(@PathVariable(value = "book_id") long book_id, Principal principal, HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        Books book = booksRepository.findById(book_id).orElseThrow();
        if (user.getBooks_list().contains(book)) {
            user.getBooks_list().remove(book);
            userRepository.save(user);
        }

        bookPdfService.deleteByFileName(book.getBook_id().toString());

        Path fileBooksPath = Paths.get(uploadBookDirectory, book.getBook_id().toString() + ".pdf");
        try {
            Files.delete(fileBooksPath);
        } catch (IOException e) {
            // обработка ошибки удаления файла книги
            System.out.println("Не удалось найти книгу!");
        }


        booksCoverService.deleteByFileName(book.getBook_id().toString());
        Path fileBooksCoverPath = Paths.get(uploadBookCoversDirectory, book.getBook_id().toString() + ".png");
        try {
            Files.delete(fileBooksCoverPath);
        } catch (IOException e) {
            // обработка ошибки удаления файла обложки
            System.out.println("Не удалось найти обложку!");
        }

        booksRepository.delete(book);
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
    public void downloadBookPDF(@PathVariable Long id, HttpServletResponse response) {
        try {
            // Получаем путь к файлу по book_id
            File file = new File(uploadBookDirectory + id + ".pdf");
            Path filePath = Paths.get(uploadBookDirectory + file.getName());
            BooksPDF file_book_name = booksPDFRepository.findById(id).orElseThrow();
            // Устанавливаем метаданные для ответа
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + file_book_name.getOriginalFileName());
            response.setContentLength((int) Files.size(filePath));

            // Копируем содержимое файла в ответ
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            // Обработка ошибок
            e.printStackTrace();
        }
    }


    @PostMapping("/rate/{book_id}")
    public String rateBook(@PathVariable(value = "book_id") long bookId,
                           @RequestParam("rating") Float rating,
                           Model model, Principal principal, HttpServletRequest request) {
        User user = userService.getUserByPrincipal(principal);
        Books book = booksRepository.findById(bookId).orElseThrow();

        // Check if the user has already rated the book
        Optional<UserBooksRating> existingRating = userBooksRatingRepository.findByUserAndBook(user, book);

        if (existingRating.isPresent()) {
            // If the user has already rated the book, update the existing rating
            UserBooksRating userRating = existingRating.get();
            userRating.setRating(rating);
            userBooksRatingRepository.save(userRating);
        } else {
            // If the user has not rated the book, create a new rating entry
            UserBooksRating newRating = new UserBooksRating();
            newRating.setUser(user);
            newRating.setBook(book);
            newRating.setRating(rating);
            userBooksRatingRepository.save(newRating);
        }

        // Update the average rating for the book
        updateAverageRating(book);

        // Redirect back to the book details page
        return "redirect:/books/" + bookId;
    }

    // Method to update the average rating for a book
    private void updateAverageRating(Books book) {
        List<UserBooksRating> ratings = userBooksRatingRepository.findByBook(book);
        int totalRatings = ratings.size();
        if (!ratings.isEmpty()) {
            // Calculate the new average rating
            float sumOfRatings = (float) ratings.stream().mapToDouble(UserBooksRating::getRating).sum();
            float averageRating = (float) (sumOfRatings / ratings.size());

            // Update the book's averageRating property
            book.setAverageRating(averageRating);
            book.setRatingCount(totalRatings);
            // Save the updated book entity
            booksRepository.save(book);
        }
    }

    @GetMapping("/multicriterial_search")
    @Cacheable("searchResultCache")
    public ResponseEntity<List<Books>> getParetoOptimalBooks(@RequestParam Map<String, Object> criteria, Model model) {
        List<Books> result = booksService.findParetoOptimalBooks(criteria);
        model.addAttribute("books", result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/multicriterial_search")
    public String multiCritetialSearchBooks(@RequestParam Map<String, Object> criteria, Model model,
                                            HttpServletResponse response) {
        List<Books> result = booksService.findParetoOptimalBooks(criteria);
        model.addAttribute("books", result);

        // Установка заголовков для кэширования
        response.setHeader("Cache-Control", "public, max-age=3600"); // Настройте max-age по необходимости

        return "search"; // Replace with the actual name of your search template
    }

    @GetMapping("/read/{id}")
    public void readPdfFile(@PathVariable Long id, HttpServletResponse response) {
        try {

            // Получаем содержимое файла
            File file = new File(uploadBookDirectory + id + ".pdf");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);
            fileInputStream.close();

            // Определяем MIME-тип файла
            response.setContentType("application/pdf");

            // Устанавливаем заголовок с именем файла
            response.setHeader("Content-disposition", "inline; filename=" + file.getName());

            // Записываем содержимое файла в выходной поток
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}