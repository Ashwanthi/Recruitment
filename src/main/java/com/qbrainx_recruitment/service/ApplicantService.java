package com.qbrainx_recruitment.service;

import java.util.List;
import java.util.Map;

import com.qbrainx_recruitment.dto.ApplicantDto;

public interface ApplicantService {

	List<ApplicantDto> getApplicant();

	ApplicantDto applicantByEmailId(String emailId);

	ApplicantDto applicantfindById(Long applicantId);

	ApplicantDto save(ApplicantDto applicantDto);

	ApplicantDto update(ApplicantDto applicantDto);

	Map<String, Boolean> delete(Long applicantId);

	Map<String, Boolean> deleteAll();

}
