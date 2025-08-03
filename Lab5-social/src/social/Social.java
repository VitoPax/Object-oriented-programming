package social;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Facade class for the social network system.
 * 
 */
public class Social {

  private final PersonRepository personRepository = new PersonRepository();
  private final GroupRepository groupRepository = new GroupRepository();
  private final PostRepository postRepository = new PostRepository();
  
  /**
   * Creates a new account for a person
   * 
   * @param code    nickname of the account
   * @param name    first name
   * @param surname last name
   * @throws PersonExistsException in case of duplicate code
   */
  public void addPerson(String code, String name, String surname) throws PersonExistsException {
    if (personRepository.findById(code).isPresent()){    // check if db already contains the code
        throw new PersonExistsException();
    }
    Person person = new Person(code, name, surname);    // create the person as a POJO
    personRepository.save(person);                      // save it to db
  }

  /**
   * Retrieves information about the person given their account code.
   * The info consists in name and surname of the person, in order, separated by
   * blanks.
   * 
   * @param code account code
   * @return the information of the person
   * @throws NoSuchCodeException if a person with that code does not exist
   */
  public String getPerson(String code) throws NoSuchCodeException {
    return this.personRepository.findById(code)
        .map(Person::toString)
        .orElseThrow(NoSuchCodeException::new);
  }

  /**
   * Define a friendship relationship between two persons given their codes.
   * <p>
   * Friendship is bidirectional: if person A is adding as friend person B, that means
   * that person B automatically adds as friend person A.
   * 
   * @param codePerson1 first person code
   * @param codePerson2 second person code
   * @throws NoSuchCodeException in case either code does not exist
   */
  public void addFriendship(String codePerson1, String codePerson2) throws NoSuchCodeException {
    Person person1 = personRepository.findById(codePerson1).orElseThrow(NoSuchCodeException::new);
    Person person2 = personRepository.findById(codePerson2).orElseThrow(NoSuchCodeException::new);
    person1.addFriend(person2);
    person2.addFriend(person1);
    personRepository.update(person1);
    personRepository.update(person2);
  }

  /**
   * Retrieve the collection of their friends given the code of a person.
   *
   * @param codePerson code of the person
   * @return the list of person codes
   * @throws NoSuchCodeException in case the code does not exist
   */
  public Collection<String> listOfFriends(String codePerson)
      throws NoSuchCodeException {
    return personRepository.findById(codePerson)
        .map(person -> person.getFriends().stream()
            .map(Person::getCode)
            .toList())
        .orElseThrow(NoSuchCodeException::new);
  }

  /**
   * Creates a new group with the given name
   * 
   * @param groupName name of the group
   * @throws GroupExistsException if a group with given name does not exist
   */
  public void addGroup(String groupName) throws GroupExistsException {
    if (this.groupRepository.findByName(groupName).isPresent()) {
      throw new GroupExistsException();
    }

    Group group = new Group(groupName);
    this.groupRepository.save(group);
  }

  /**
   * Deletes the group with the given name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if a group with given name does not exist
   */
  public void deleteGroup(String groupName) throws NoSuchCodeException {
    Group group = this.groupRepository.findByName(groupName).orElseThrow(NoSuchCodeException::new);
    this.groupRepository.delete(group);
  }

  /**
   * Modifies the group name
   * 
   * @param groupName name of the group
   * @throws NoSuchCodeException if the original group name does not exist
   * @throws GroupExistsException if the target group name already exist
   */
  public void updateGroupName(String groupName, String newName) throws NoSuchCodeException, GroupExistsException {
    if (this.groupRepository.findByName(newName).isPresent()) {
      throw new GroupExistsException();
    }
    Group group = this.groupRepository.findByName(groupName).orElseThrow(NoSuchCodeException::new);
    group.setName(newName);
    this.groupRepository.update(group);
  }

  /**
   * Retrieves the list of groups.
   * 
   * @return the collection of group names
   */
  public Collection<String> listOfGroups() {
    return this.groupRepository.findAll()
        .stream()
        .map(Group::getName)
        .collect(Collectors.toSet());
  }

  /**
   * Add a person to a group
   * 
   * @param codePerson person code
   * @param groupName  name of the group
   * @throws NoSuchCodeException in case the code or group name do not exist
   */
  public void addPersonToGroup(String codePerson, String groupName) throws NoSuchCodeException {
    Person person = this.personRepository.findById(codePerson).orElseThrow(NoSuchCodeException::new);
    Group group = this.groupRepository.findByName(groupName).orElseThrow(NoSuchCodeException::new);

    group.addMember(person);
    this.groupRepository.update(group);
  }

  /**
   * Retrieves the list of people on a group
   * 
   * @param groupName name of the group
   * @return collection of person codes
   */
  public Collection<String> listOfPeopleInGroup(String groupName) {
    return this.groupRepository.findByName(groupName)
        .map(group -> group.getMembers().stream()
            .map(Person::getCode)
            .collect(Collectors.toSet()))
        .orElse(null);
  }

  /**
   * Retrieves the code of the person having the largest
   * group of friends
   * 
   * @return the code of the person
   */
  public String personWithLargestNumberOfFriends() {
    return this.personRepository.findPersonWithMostFriends().map(Person::getCode).orElse(null);
  }

  /**
   * Find the name of group with the largest number of members
   * 
   * @return the name of the group
   */
  public String largestGroup() {
    return this.groupRepository.findGroupWithMostMembers().map(Group::getName).orElse(null);
  }

  /**
   * Find the code of the person that is member of
   * the largest number of groups
   * 
   * @return the code of the person
   */
  public String personInLargestNumberOfGroups() {
    return this.personRepository.findPersonInMostGroups().map(Person::getCode).orElse(null);
  }

  // R5

  /**
   * add a new post by a given account
   * 
   * @param authorCode the id of the post author
   * @param text   the content of the post
   * @return a unique id of the post
   */
  public String post(String authorCode, String text) {
    Person author = personRepository.findById(authorCode).orElse(null);
    Post post = new Post(author, text, System.currentTimeMillis());
    this.postRepository.save(post);
    return post.getId().toString();
  }

  /**
   * retrieves the content of the given post
   * 
   * @param pid    the id of the post
   * @return the content of the post
   */
  public String getPostContent(String pid) {
    return this.postRepository.findById(Long.parseLong(pid)).map(Post::getContent).orElse(null);
  }

  /**
   * retrieves the timestamp of the given post
   * 
   * @param pid    the id of the post
   * @return the timestamp of the post
   */
  public long getTimestamp(String pid) {
    return this.postRepository.findById(Long.parseLong(pid)).map(Post::getTimestamp).orElse(null);
  }

  /**
   * returns the list of post of a given author paginated
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts id
   */
  public List<String> getPaginatedUserPosts(String author, int pageNo, int pageLength) {
    Person person = this.personRepository.findById(author).orElse(null);
    int offset = (pageNo - 1) * pageLength;
    return person.getPosts()
        .stream()
        .sorted(Comparator.comparing(Post::getTimestamp).reversed())
        .skip(offset)
        .limit(pageLength)
        .map(post -> post.getId().toString())
        .collect(Collectors.toList());
  }

  /**
   * returns the paginated list of post of friends.
   * The returned list contains the author and the id of a post separated by ":"
   * 
   * @param author     author of the post
   * @param pageNo     page number (starting at 1)
   * @param pageLength page length
   * @return the list of posts key elements
   */
  public List<String> getPaginatedFriendPosts(String author, int pageNo, int pageLength) {
    Person person = this.personRepository.findById(author).orElse(null);
    int offset = (pageNo - 1) * pageLength;

    return person.getFriends()
        .stream()
        .flatMap(friend -> friend.getPosts().stream())
        .sorted(Comparator.comparing(Post::getTimestamp).reversed())
        .skip(offset)
        .limit(pageLength)
        .map(post -> post.getAuthor().getCode() + ":" + post.getId())
        .collect(Collectors.toList());
  }

}