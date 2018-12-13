package com.green.movie_demo.controller;

import com.green.movie_demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/movies")
public class MovieController
{
    @Autowired
    private MovieService movieService;
    
    @GetMapping(value = "/info")
    public Object getMoviesInfo(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "per_page", defaultValue = "20") int per_page)
    {
        return movieService.getMoviesInfo(page, per_page);
    }
    
    @GetMapping(value = "/{id}")
    public Object getAMovie(@PathVariable("id") int id)
    {
        return movieService.getAMovie(id);
    }
    
    @GetMapping(value = "/count")
    public Object getMoviesCnt()
    {
        return movieService.getMoviesCnt();
    }
    
    @GetMapping(value = "/{movie_id}/short-comments")
    public Object getShortComments(@PathVariable("movie_id") int movie_id)
    {
        return movieService.getShortComments(movie_id);
    }
    
    // 所有电影类别，一个类别下的电影数目越多，排序越靠前
    @GetMapping(value = "/categories")
    public Object getAllCategories()
    {
        return movieService.getAllCategories();
    }
    
    // TODO: 对page, per_page等参数的检查
    // 该类别下K部的电影
    // orderByRank:
    //  true 获取排名靠前的K部电影
    //  false 随机获取K部电影
    @GetMapping("/categories/{category_id}/some-movies")
    public Object getKMoviesFromCategory(@PathVariable("category_id") int category_id,
                                         @RequestParam("K") int K,
                                         @RequestParam("orderByRank") boolean orderByRank)
    {
        return movieService.findKMoviesUnderCategory(category_id, K, orderByRank);
    }
    
    @GetMapping("/categories/{category_id}/movies")
    public Object getMoviesUnderCategory(@PathVariable("category_id") int category_id,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "per_page", defaultValue = "10") int per_page)
    {
        return movieService.getMoviesUnderCategory(category_id, page, per_page);
    }
}

