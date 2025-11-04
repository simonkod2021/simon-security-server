package com.example.demo.service;

import com.example.demo.dto.BlogPostsDTO;
import com.example.demo.models.BlogPosts;
import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.repository.BlogPostRepository;
import com.example.demo.repository.CommentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public BlogPostService(BlogPostRepository blogPostRepository, CommentRepository commentRepository, UserService userService) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    // Return all blog posts in the database
    public List<BlogPosts> getAllBlogPosts(){
        return blogPostRepository.findAll();
    }

    // Get a blog post by its id
    public BlogPosts getBlogPostById(String id){
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found with id: " + id));
    }

    // Create a new blog post
    public void createPost(BlogPostsDTO dto, Authentication authentication){

        // Get the current authenticated user
        String username = authentication.getName();

        // Find the user by username and set as author
        User author = userService.findByUsername(username);

        BlogPosts blogPost = new BlogPosts();

        // Set the author and other fields properly
        blogPost.setAuthor(author);
        // Set current timestamp
        blogPost.setCreatedAt(LocalDateTime.now());
        // Set current timestamp
        blogPost.setContent(dto.getContent());
        blogPost.setTitle(dto.getTitle());

        blogPostRepository.save(blogPost);
    }

    public BlogPosts deleteBlogPost(@PathVariable String id){
        BlogPosts blogPost = getBlogPostById(id);
        blogPostRepository.deleteById(id);
        return blogPost;

    }

    // Add a new comment on a post by its id and save it to the DB
    public Comment addComment(String blogPostId, Comment comment){
        BlogPosts blogPost = getBlogPostById(blogPostId);
        comment.setBlogPost(blogPost);
        comment.setCreatedAt(LocalDateTime.now());
        comment = commentRepository.save(comment);
        blogPostRepository.save(blogPost);

        return comment;
    }

    public BlogPostsDTO getBlogPostWithComments(String id){
        BlogPosts blogposts = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found with id: " + id));


        return new BlogPostsDTO(blogposts);
    }

}
