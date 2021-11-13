package com.example.Library.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Table(name = "borrowments")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Borrowment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Book.class)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Member.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @JsonProperty("due_date")
    private Date dueDate;

    @JsonProperty("fine")
    private int fine;

    @JsonProperty("return_status")
    @Column(columnDefinition = "integer default 0")
    private int returnFlag;

    @JsonProperty("email_flag")
    @Column(columnDefinition = "integer default 0")
    private int emailFlag;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("borrow_day")
    private Date createdAt;

    //waktu pengembalian leebih spesifik
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date updatedAt;
}