package com.qbrainx_recruitment.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.ConsultantDto;
import com.qbrainx_recruitment.service.ConsultantService;

@RestController
@RequestMapping("/v1/consultant")
public class ConsultantController {

	private ConsultantService consultantService;

	public ConsultantController(ConsultantService consultantService) {
		this.consultantService = consultantService;
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ConsultantDto> createUser(@Valid @RequestBody final ConsultantDto consultantDto) {
		ConsultantDto savedConsultantDto = consultantService.save(consultantDto);
		return new ResponseEntity<>(savedConsultantDto, HttpStatus.CREATED);

	}

}
