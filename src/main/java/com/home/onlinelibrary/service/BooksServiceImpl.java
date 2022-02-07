package com.home.onlinelibrary.service;

import com.home.onlinelibrary.domain.Book;
import com.home.onlinelibrary.domain.Resource;
import com.home.onlinelibrary.repository.BooksRepository;
import com.home.onlinelibrary.repository.ResourcesRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.home.onlinelibrary.utils.Constants.FILES_PATH;

@Service
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final ResourcesRepository resourceRepository;
    private final UploadFileService uploadFileService;

    public BooksServiceImpl(BooksRepository repository, ResourcesRepository resourceRepository, UploadFileService uploadFileService, ServletContext context) {
        this.booksRepository = repository;
        this.uploadFileService = uploadFileService;
        this.resourceRepository = resourceRepository;
    }

    public List<Book> getAllBooks() {
        //todo: 10 рандомных книг, рендеринг картинок после загрузки в кэш
        List<Book> books = (List<Book>) booksRepository.findAll();
        initIcons(books);
        return books;
    }

    private void initIcons(List<Book> books) {
        List<Resource> resource = resourceRepository.findIconByIds(books.stream().map(b -> b.getResource().getId()).collect(Collectors.toList()));
        books.forEach(b -> setIcon(resource, b));
    }

    private void setIcon(List<Resource> resource, Book b) {
        b.setResource(resource.stream().filter(r -> r.getId().equals(b.getResource().getId())).findAny().orElse(null));
        if (b.getModifiedFileName() != null && b.getResource() != null && b.getResource().getIcon() != null) {
            saveToCache(b.getModifiedIconName(), b.getResource().getIcon());
        }
    }

    public Book getBookById(Long bookId) {
        return booksRepository.findById(bookId).orElse(null);
    }

    public Book getBookByIdAndFetchResource(Long bookId) {
        Book dbBook = booksRepository.findByIdAndFetchResource(bookId);
        if (dbBook != null) {
            if (dbBook.getModifiedIconName() != null) {
                saveToCache(dbBook.getModifiedIconName(), dbBook.getResource().getIcon());
            }
            if (dbBook.getModifiedFileName() != null) {
                saveToCache(dbBook.getModifiedFileName(), dbBook.getResource().getFile());
            }
        }
        return dbBook;
    }

    public void save(Book book) {
        long time = System.currentTimeMillis();
        String fileName = book.getFileName();
        if (!fileName.isEmpty()) {
            String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + time + "." + FilenameUtils.getExtension(fileName);
            book.setModifiedFileName(modifiedFileName);
        }

        String iconName = book.getIconName();
        if (!iconName.isEmpty()) {
            String modifiedIconName = FilenameUtils.getBaseName(iconName) + "_" + time + "." + FilenameUtils.getExtension(iconName);
            book.setModifiedIconName(modifiedIconName);
        }

        Book dbBook = booksRepository.save(book);

        if (dbBook != null) {
            if (book.getModifiedIconName() != null) {
                saveToCache(book.getModifiedIconName(), dbBook.getResource().getIcon());
            }
            if (book.getModifiedFileName() != null) {
                saveToCache(book.getModifiedFileName(), dbBook.getResource().getFile());
            }
        }
    }

    private void saveToCache(String modifiedName, byte[] data) {
        File store = uploadFileService.getFilePath(modifiedName, FILES_PATH);
        if (data.length > 0 && store != null && !store.exists()) {
            try {
                FileUtils.writeByteArrayToFile(store, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //todo:рендеринг картинки после загрузки
    }

    public void updateBook(Book book) {
        deleteFromCache(book);
        save(book);
    }

    private void deleteFromCache(Book book) {
        if (book != null) {
            deleteFile(book.getModifiedIconName());
            deleteFile(book.getModifiedFileName());
        }
    }

    private void deleteFile(String modifiedIconName) {
        if (modifiedIconName != null && !FilenameUtils.getExtension(modifiedIconName).isEmpty()) {
            File storedFile = uploadFileService.getFilePath(modifiedIconName, FILES_PATH);
            if (storedFile.exists()) {
                storedFile.delete();
            }
        }
    }

    public List<Book> findBooks(String searchText) {
        return booksRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(searchText, searchText, searchText);
    }

    public void deleteBook(Book book) {
        if (book.getId() != null) {
            booksRepository.deleteById(book.getId());
        }
        deleteFile(book.getModifiedIconName());
        deleteFile(book.getModifiedFileName());
    }
}
