package com.example.Library.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Book {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    private String tittle;
    private String author;
    private int flag;
    private int stock; //stock saat ini, stock total, jaga jaga handling

    @CreationTimestamp
    @Column( columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @ManyToOne(targetEntity = Category.class) // cascade type all check
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
