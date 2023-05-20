package com.tss.entities.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "time_created", nullable = false)
    private Timestamp time_created;

    @Column(name = "time_modified")
    private Timestamp time_modified;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference(value="TaskAttachments")
    private Collection<Attachment> attachments = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "task", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonManagedReference(value="TaskComments")
    private Collection<Comment> comments = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "taskList_id", nullable = false)
    @JsonBackReference
    private TaskList taskList;

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Collection<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Timestamp getTime_modified() {
        return time_modified;
    }

    public void setTime_modified(Timestamp time_modified) {
        this.time_modified = time_modified;
    }

    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}