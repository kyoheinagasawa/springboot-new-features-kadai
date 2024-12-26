package com.example.samuraitravel.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {
	
	private Integer houseId;
	
	private Integer userId;
	
	private Integer evaluation;
	
	private String comment;

}
