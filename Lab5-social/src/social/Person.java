package social;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
class Person {
  @Id
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Person> friends = new HashSet<>();

  @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
  private Set<Group> groups = new HashSet<>();

  @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
  private Set<Post> posts = new HashSet<>();

  public Person() {
    // default constructor is needed by JPA
  }

  Person(String code, String name, String surname) {
    this.code = code;
    this.name = name;
    this.surname = surname;
  }

  String getCode() {
    return code;
  }

  String getName() {
    return name;
  }

  String getSurname() {
    return surname;
  }

  void addFriend(Person friend) {
    this.friends.add(friend);
  }

  Set<Person> getFriends() {
    return this.friends;
  }
  
  public Set<Post> getPosts() {
      return posts;
  }

  @Override
  public String toString() {
    return code + " " + name + " " + surname;
  }

  @Override
  public boolean equals(Object obj) {
    if (code == null) return super.equals(obj);
    if (!(obj instanceof Person)) return false;
    return code.equals(((Person) obj).code);
  }

  @Override
  public int hashCode() {
    if (code == null) return super.hashCode();
    return code.hashCode();
  }
}
