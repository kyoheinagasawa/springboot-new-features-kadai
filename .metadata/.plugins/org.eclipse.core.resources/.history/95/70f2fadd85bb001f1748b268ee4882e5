package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
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
    	
    	review.setEvaluation(reviewRegisterForm.getEvaluation());
    	review.setComment(reviewRegisterForm.getComment());
    	
    	House house = houseRepository.getReferenceById(reviewRegisterForm.getHouseId());
    	review.setHouse(house);
    	
    	User user = userRepository.getReferenceById(reviewRegisterForm.getUserId());
    	review.setUser(user);
    	
    	
    	reviewRepository.save(review);
    }
}
