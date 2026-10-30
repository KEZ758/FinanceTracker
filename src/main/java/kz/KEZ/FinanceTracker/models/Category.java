package kz.KEZ.FinanceTracker.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "categories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Category() {}

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
    }


}

