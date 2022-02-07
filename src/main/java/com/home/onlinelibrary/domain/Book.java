package com.home.onlinelibrary.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.home.onlinelibrary.utils.Constants.FILES_PATH;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class Book implements Serializable {
    @Id
    @SequenceGenerator(name = "id_generator", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    private Long id;

    @Column(name = "s_title")
    private String title;

    @Column(name = "s_author")
    private String author;

    @Column(name = "s_genre")
    private String genre;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "d_created_date")
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "d_modified_date")
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "editor_id")
    private User editor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    private String iconName;

    private String modifiedIconName;

    private String fileName;

    private String modifiedFileName;

    public Book() {
    }

    public Book(String title, String author, String genre, User creator, String iconName, String fileName, Resource resource ) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.creator = creator;
        this.editor = creator;
        this.resource = resource;
        this.iconName = iconName;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String description) {
        this.genre = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getModifiedFileName() {
        return modifiedFileName;
    }

    public void setModifiedFileName(String modifiedFileName) {
        this.modifiedFileName = modifiedFileName;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getModifiedIconName() {
        return modifiedIconName;
    }

    public void setModifiedIconName(String modifiedIconName) {
        this.modifiedIconName = modifiedIconName;
    }

    public static String getFilesPath(){
        return FILES_PATH;
    }
}