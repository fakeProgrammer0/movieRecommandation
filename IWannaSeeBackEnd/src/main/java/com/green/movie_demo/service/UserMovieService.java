package com.green.movie_demo.service;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.Rating;
import com.green.movie_demo.entity.Result;
import com.green.movie_demo.mapper.MovieMapper;
import com.green.movie_demo.mapper.UserInfoMapper;
import com.green.movie_demo.mapper.UserMovieMapper;
import com.green.movie_demo.util.ResultUtil;
import com.green.movie_demo.util.SqlUtil;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UserMovieService
{
    private Logger logger = LoggerFactory.getLogger(UserMovieService.class);
    
    @Autowired
    private UserMovieMapper userMovieMapper;
    
    @Autowired
    private MovieMapper movieMapper;
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    public Result addCollection(int user_id, int movie_id)
    {
        try
        {
            int affectedRow = userMovieMapper.insertIntoCollection(user_id, movie_id);
            if (affectedRow == 1)
            {
                String data = "{" +
                        "user_id: " + user_id +
                        ",movie_id: " + movie_id +
                        "}";
                return Result.OK().data(data).build();
            }
        } catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        
        return Result.BadRequest().build();
    }
    
    public Result removeCollection(int user_id, int movie_id)
    {
        int affectedRow = userMovieMapper.deleteFromCollection(user_id, movie_id);
        return affectedRow == 1 ? Result.OK().build() : Result.BadRequest().build();
    }
    
    public Result getCollections(int user_id)
    {
        List<Integer> movie_ids = userMovieMapper.getCollections(user_id);
        int total = movie_ids.size();
        Object data = ResultUtil.total(total, movie_ids);
        return Result.OK().data(data).build();
    }
    
    public Result getCollectedMovies(int user_id, int page, int per_page)
    {
        int total = userMovieMapper.findTotal_Collections(user_id);
        List<Movie> movies = userMovieMapper.getCollectedMovies(user_id, SqlUtil.offset(page,per_page), per_page);
        Object data = ResultUtil.total(total, movies);
        return Result.OK().data(data).build();
    }
    
    public Result checkCollectedMovie(int user_id, int movie_id)
    {
        Integer count = userMovieMapper.checkCollectedMovie(user_id, movie_id);
        return count == 1 ? Result.OK().build() : Result.NotFound().build();
    }
    
    public Result getFavorCategories(int user_id)
    {
        List<Category> categories = userMovieMapper.getFavorCategories(user_id);
        int total = categories.size();
        return Result.OK().data(ResultUtil.total(total, categories)).build();
    }
    
    public Result addFavorCategories(int user_id, List<Integer> category_ids)
    {
        List<Integer> oldCategoryIds = userMovieMapper.getFavorCategoryIds(user_id);
        List<Integer> newCategoryIds = new LinkedList<>();
        for(Integer category_id: category_ids)
        {
            if(!oldCategoryIds.contains(category_id))
                newCategoryIds.add(category_id);
        }
        try
        {
            int affectedRows = userMovieMapper.insertFavorCategories(insertFavoriteCategoriesHelper(user_id, newCategoryIds));
            return Result.OK().build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        
        return Result.BadRequest().build();
    }
    
    private static Map<String, Object> insertFavoriteCategoriesHelper(int user_id, List<Integer> category_ids)
    {
        Map<String, Object>paramMap = new HashMap<>();
        paramMap.put("user_id", user_id);
        paramMap.put("category_ids", category_ids);
        return paramMap;
    }
    
    public Result removeFavorCategory(int user_id, int category_id)
    {
        Integer affectedRow = userMovieMapper.deleteFavorCategory(user_id, category_id);
        return affectedRow == 1 ? Result.OK().build() : Result.BadRequest().build();
    }
    
    public Result addARating(Rating rating)
    {
        try{
            Integer affetedRow = userMovieMapper.insertRating(rating);
            if(affetedRow == 1) return Result.OK().data(rating).build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        return Result.BadRequest().build();
    }
    
    public Result getRatingsOfMovie(int movie_id, int page, int per_page)
    {
        try{
            int total = userMovieMapper.findTotal_RatingsOfMovie(movie_id);
            if(total == 0 && movieMapper.findAMovieById(movie_id) == null)
                return Result.BadRequest().build();
            
            List<Rating> ratings = userMovieMapper.findRatingsOfMovie(movie_id, SqlUtil.offset(page, per_page), per_page);
            return Result.OK().data(ResultUtil.total(total, ratings)).build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        return  Result.BadRequest().build();
    }
    
    public Result getRatingsOfUser(int user_id, int page, int per_page)
    {
        try{
            int total = userMovieMapper.findTotal_RatingsOfUser(user_id);
            if(total == 0 && userInfoMapper.findUserById(user_id) == null)
                return Result.BadRequest().build();
            
            List<Rating> ratings = userMovieMapper.findRatingsOfUser(user_id, SqlUtil.offset(page, per_page), per_page);
            return Result.OK().data(ResultUtil.total(total, ratings)).build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        return  Result.BadRequest().build();
    }
    
    public Result removeARating(int user_id, int movie_id)
    {
        try{
            Integer affectedRow = userMovieMapper.deleteRating(user_id, movie_id);
            if(affectedRow == 1) return Result.OK().build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        return Result.BadRequest().build();
    }
    
}
