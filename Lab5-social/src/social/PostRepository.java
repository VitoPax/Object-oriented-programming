package social;

public class PostRepository extends GenericRepository<Post, Long> {
    public PostRepository() {
        super(Post.class);
    }
}
