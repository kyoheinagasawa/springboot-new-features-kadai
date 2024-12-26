package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewInputForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("houses/{id}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;

	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository,
			ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}

	@GetMapping
	public String index(@PathVariable(name = "id") Integer id, Model model,
			@PageableDefault(page = 0, size = 8, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByHouseId(id, pageable);
		House house = houseRepository.getReferenceById(id);

		model.addAttribute("Reviews", reviews);
		model.addAttribute("house", house);

		return "reviews/index";
	}

	@GetMapping("/register")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);

		model.addAttribute("house", house);
		model.addAttribute("reviewInputForm", new ReviewInputForm());

		return "reviews/register";
	}

	@GetMapping("/register/input")
	public String input(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated ReviewInputForm reviewInputForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		House house = houseRepository.getReferenceById(id);

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("errorMessage", "レビュー内容に不備があります。");
			return "reviews/register";
		}

		model.addAttribute("house", house);
		redirectAttributes.addFlashAttribute("reviewInputForm", reviewInputForm);

		return "redirect:/houses/{id}/reviews/register/confirm";
	}

	@GetMapping("/register/confirm")
	public String confirm(@PathVariable(name = "id") Integer id,
			@ModelAttribute ReviewInputForm reviewInputForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		House house = houseRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();

		ReviewRegisterForm reviewRegisterForm = new ReviewRegisterForm(house.getId(), user.getId(),
				reviewInputForm.getEvaluation(), reviewInputForm.getComment());

		model.addAttribute("house", house);
		model.addAttribute("reviewRegisterForm", reviewRegisterForm);

		return "reviews/confirm";
	}

	@PostMapping("/create")
	public String create(@PathVariable(name = "id") Integer id,
			@ModelAttribute ReviewRegisterForm reservationRegisterForm, Model model) {
		reviewService.create(reservationRegisterForm);

		House house = houseRepository.getReferenceById(id);
		model.addAttribute("house", house);

		return "redirect:/houses/{id}?reserved";
	}

	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable Integer reviewId,
			Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Review review = reviewRepository.getReferenceById(reviewId);
		House house = review.getHouse(); // レビューからハウスを取得
		User user = userDetailsImpl.getUser();
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getEvaluation(), review.getComment(),
				house.getId(), user.getId());

		model.addAttribute("house", house);
		model.addAttribute("reviewEditForm", reviewEditForm);

		return "reviews/edit";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "reviews/edit";
		}

		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビュー情報を編集しました。");

		return "redirect:/houses/{id}";
	}
	
	@PostMapping("/{reviewid}/delete")
    public String delete(@PathVariable(name = "reviewid") Integer reviewId, RedirectAttributes redirectAttributes) {        
        reviewRepository.deleteById(reviewId);
                
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
        
        return "redirect:/houses/{id}";
    }  
}
