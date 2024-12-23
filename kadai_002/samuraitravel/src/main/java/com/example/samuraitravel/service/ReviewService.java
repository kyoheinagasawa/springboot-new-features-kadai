package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;


@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;  
    private final HouseRepository houseRepository;  
    private final UserRepository userRepository;  
    
    public ReviewService(ReviewRepository reviewRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;  
        this.houseRepository = houseRepository;  
        this.userRepository = userRepository;  
    }    
    
    @Transactional
    public void create(ReviewRegisterForm reviewRegisterForm) { 
        Review review = new Review();
        House house = houseRepository.getReferenceById(reviewRegisterForm.getHouseId());
        User user = userRepository.getReferenceById(reviewRegisterForm.getUserId());
                
        review.setHouse(house);
        review.setUser(user);
        review.setComment(reviewRegisterForm.getComment());
        review.setEvaluation(reviewRegisterForm.getEvaluation());
        
        reviewRepository.save(review);
    }
    
    @Transactional
    public void update(ReviewEditForm reviewEditForm) {
        Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
        House house = houseRepository.getReferenceById(reviewEditForm.getHouseId());
        User user = userRepository.getReferenceById(reviewEditForm.getUserId());
        
                       
        review.setEvaluation(reviewEditForm.getEvaluation());
        review.setComment(reviewEditForm.getComment());
        review.setHouse(house);
        review.setUser(user);
                    
        reviewRepository.save(review);
    }  
}

