package social;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Person author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long timestamp;

    public Post() {}

    public Post(Person author, String content, Long timestamp) {
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Person getAuthor() {
        return this.author;
    }

    public String getContent() {
        return this.content;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public Long getId() {
        return id;
    }
}
