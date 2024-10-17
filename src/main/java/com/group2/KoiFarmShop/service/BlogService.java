package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.entity.BlogPost;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements BlogServiceImp{
    @Override
    public List<BlogPost> getAllBlogPosts() {
        return List.of();
    }

    @Override
    public BlogPost getBlogPostById(int id) {
        return null;
    }

    @Override
    public BlogPost addBlogPost(BlogPost blogPost) {
        return null;
    }

    @Override
    public BlogPost updateBlogPost(BlogPost blogPost) {
        return null;
    }
}
