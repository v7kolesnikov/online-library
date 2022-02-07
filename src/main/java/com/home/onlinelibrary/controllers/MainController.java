package com.home.onlinelibrary.controllers;

import com.home.onlinelibrary.domain.Book;
import com.home.onlinelibrary.domain.Resource;
import com.home.onlinelibrary.domain.User;
import com.home.onlinelibrary.service.BooksService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class MainController {
    private static final String COLLECTION_KEY = "books";
    private static final String GO_TO_HOME_PAGE = "redirect:/";
    private static final String HOME_PAGE = "main";

    private final BooksService bookService;

    private final ServletContext context;

    public MainController(BooksService bookService, ServletContext context) {
        this.bookService = bookService;
        this.context = context;
    }

    @GetMapping("/")
    public String main(Map<String, Object> model) {
        model.put(COLLECTION_KEY, bookService.getAllBooks());
        return HOME_PAGE;
    }

    @GetMapping("/newbook")
    public String newBook() {
        return "newbook";
    }

    @PostMapping("/newbook")
    public String addBook(@AuthenticationPrincipal User user,
                          @ModelAttribute Book book,
                          @RequestParam("icon") MultipartFile icon,
                          @RequestParam("file") MultipartFile file) throws IOException {

        bookService.save(new Book(book.getTitle(), book.getAuthor(), book.getGenre(), user, icon.getOriginalFilename(), file.getOriginalFilename(), new Resource(icon.getBytes(), file.getBytes())));

        return GO_TO_HOME_PAGE;
    }

    @RequestMapping(value = "/editBook", method = RequestMethod.POST, params = "edit")
    public String editBook(@ModelAttribute Book book, Model model) {
        if (book.getId() != null) {
            Book modifiedBook = bookService.getBookById(book.getId());
            model.addAttribute("book", modifiedBook);
        }
        return "editbook";
    }

    @PostMapping("/editbook")
    public String saveEditedBook(@AuthenticationPrincipal User user,
                                 @ModelAttribute Book book,
                                 @RequestParam("icon") MultipartFile icon,
                                 @RequestParam("file") MultipartFile file
    ) throws IOException {
        Book modifiedBook = bookService.getBookByIdAndFetchResource(book.getId());
        modifiedBook.setTitle(book.getTitle());
        modifiedBook.setAuthor(book.getAuthor());
        modifiedBook.setGenre(book.getGenre());
        if (!icon.isEmpty()) {
            modifiedBook.setIconName(icon.getOriginalFilename());
            Resource resource = modifiedBook.getResource();
            if (resource.getIcon() != null) {
                resource.setIcon(icon.getBytes());
            } else {
                modifiedBook.setResource(new Resource(icon.getBytes(), new byte[0]));
            }
        }
        if (!file.isEmpty()) {
            modifiedBook.setFileName(file.getOriginalFilename());
            if (modifiedBook.getResource().getFile() != null) {
                modifiedBook.getResource().setFile(file.getBytes());
            } else {
                modifiedBook.setResource(new Resource(new byte[0], file.getBytes()));
            }
        }
        modifiedBook.setEditor(user);

        bookService.updateBook(modifiedBook);

        return GO_TO_HOME_PAGE;
    }

    @GetMapping("/downloadicon")
    public void downloadIcon(@Param("id") Long id, @Param("modifiedName") String modifiedName,
                             @Param("name") String name,
                             HttpServletResponse response) throws IOException {
        copyFileToStream(modifiedName, name, response, "attachment", id);
    }

    @GetMapping("/downloadfile")
    public void downloadFile(@Param("id") Long id, @Param("modifiedName") String modifiedName,
                             @Param("name") String name,
                             HttpServletResponse response) throws IOException {
        copyFileToStream(modifiedName, name, response, "attachment", id);
    }

    @GetMapping("/preview")
    public void previewFile(@Param("id") Long id, @Param("modifiedName") String modifiedName, @Param("name") String name, HttpServletResponse response) throws IOException {
        copyFileToStream(modifiedName, name, response, "inline", id);
    }

    private void copyFileToStream(String modifiedName, String name, HttpServletResponse response, String dispositionType, Long id) throws UnsupportedEncodingException {
        String fullPath = context.getRealPath("/files/" + File.separator + modifiedName);
        File file = new File(fullPath);
        if (!file.exists()) {
            bookService.getBookByIdAndFetchResource(id);
        }

        response.setContentType(context.getMimeType(fullPath));
        String fileName = URLEncoder.encode(name, "UTF-8");
        fileName = URLDecoder.decode(fileName, "ISO8859_1");
        response.setHeader("Content-Disposition", dispositionType + "; filename=" + fileName);
        try {
            Files.copy(Paths.get(fullPath), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @RequestMapping(value = "/editBook", method = RequestMethod.POST, params = "delete")
    public String deleteBook(@ModelAttribute Book book) {
        bookService.deleteBook(book);

        return GO_TO_HOME_PAGE;
    }

    @PostMapping("/filter")
    public String filter(@RequestParam("filter") String filter, Map<String, Object> model) {
        Iterable<Book> filteredBooks;
        if (filter != null && !filter.isEmpty()) {
            filteredBooks = bookService.findBooks(filter);
            model.put(COLLECTION_KEY, filteredBooks);
            return HOME_PAGE;
        } else {
            filteredBooks = bookService.getAllBooks();
            model.put(COLLECTION_KEY, filteredBooks);
            return GO_TO_HOME_PAGE;
        }
    }
}
