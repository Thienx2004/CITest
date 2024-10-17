package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.entity.BlogPost;

import java.util.List;

public interface BlogServiceImp {

    public List<BlogPost> getAllBlogPosts();
    public BlogPost getBlogPostById(int id);
    public BlogPost addBlogPost(BlogPost blogPost);
    public BlogPost updateBlogPost(BlogPost blogPost);
}
