package com.example.demo.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherRequestDTO {

	@NotBlank(message = "Name is required")
	private String name;

	private String website;
	private String address;
}
