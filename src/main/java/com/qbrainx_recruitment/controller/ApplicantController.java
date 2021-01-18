package com.qbrainx_recruitment.controller;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.event.ApplicantFormProcessEvent;
import com.qbrainx_recruitment.model.Applicant;
import com.qbrainx_recruitment.service.ApplicantService;
import com.qbrainx_recruitment.util.CustomConverters;
import com.qbrainx_recruitment.util.ModelConverters;

@RestController
@RequestMapping("/v1/applicant")
public class ApplicantController {
	
	@Autowired
	private final ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private ApplicantService applicantService;
	
	
	private final ModelConverters<ApplicantDto, Applicant> modelConverters;
	
	public ApplicantController(ApplicationEventPublisher applicationEventPublisher, ApplicantService applicantService, final DozerBeanMapper dozerBeanMapper) {
		this.modelConverters = CustomConverters.converter(dozerBeanMapper, ApplicantDto.class, Applicant.class);
		this.applicationEventPublisher = applicationEventPublisher;
		this.applicantService = applicantService;
	}

	@GetMapping
	public List<ApplicantDto> getAllApplicant() {
		return applicantService.getApplicant();
	}

	@GetMapping("/email/{emailId}")
	public ResponseEntity<ApplicantDto> getApplicantEmail(@PathVariable("emailId") final String emailId) {
		return new ResponseEntity<>(applicantService.applicantByEmailId(emailId), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApplicantDto> getApplicantById(@PathVariable("id") final Long id) {
		return new ResponseEntity<>(applicantService.applicantfindById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApplicantDto> saveApplicant(@Valid @RequestBody ApplicantDto applicantDto) {
				
		ApplicantDto applicantDtoResponse = applicantService.save(applicantDto);
	
		applicationEventPublisher.publishEvent(new ApplicantFormProcessEvent(applicantDtoResponse));
		return new ResponseEntity<>(applicantDtoResponse, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<ApplicantDto> updateApplicant(@RequestBody ApplicantDto applicantDto) {
		return new ResponseEntity<>(applicantService.update(applicantDto), HttpStatus.OK);

	}

	@DeleteMapping
	public Map<String, Boolean> deleteAll() {
		return applicantService.deleteAll();
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteApplicantById(@PathVariable(value = "id") Long id) {
		return applicantService.delete(id);
	}

}
