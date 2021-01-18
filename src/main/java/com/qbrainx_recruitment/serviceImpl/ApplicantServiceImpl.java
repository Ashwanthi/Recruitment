package com.qbrainx_recruitment.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.exception.ResourceNotFoundException;
import com.qbrainx_recruitment.model.Applicant;
import com.qbrainx_recruitment.repository.ApplicantRepository;
import com.qbrainx_recruitment.service.ApplicantService;
import com.qbrainx_recruitment.util.CustomConverters;
import com.qbrainx_recruitment.util.ModelConverters;

@Service
public class ApplicantServiceImpl implements ApplicantService {

	private ApplicantRepository applicantRepository;


	private final ModelConverters<ApplicantDto, Applicant> modelConverters;

	public ApplicantServiceImpl(ApplicantRepository applicantRepository, DozerBeanMapper dozerBeanMapper) {
		this.applicantRepository = applicantRepository;
		this.modelConverters = CustomConverters.converter(dozerBeanMapper, ApplicantDto.class, Applicant.class);

	}

	@Override
	public List<ApplicantDto> getApplicant() {
		List<Applicant> li = applicantRepository.findAll();
		ModelMapper mdl = new ModelMapper();
		List<ApplicantDto> appDtoList = li.stream().map(applicant -> {
			ApplicantDto eDto = new ApplicantDto(applicant.getFirstName(), applicant.getLastName(),
					applicant.getEmailId());
			mdl.map(applicant, eDto);
			return eDto;
		}).collect(Collectors.toList());
		return appDtoList;
	}

	@Override
	public ApplicantDto applicantByEmailId(String emailId) {
		Applicant applicant = applicantRepository.findByemailId(emailId)
				.orElseThrow(() -> new ResourceNotFoundException("emailId", "emailId not found"));
		ApplicantDto dto = new ApplicantDto(applicant.getFirstName(), applicant.getLastName(), applicant.getEmailId());
		ModelMapper modelmapper = new ModelMapper();
		modelmapper.map(applicant, dto);
		return dto;
	}

	@Override
	public ApplicantDto applicantfindById(Long applicantId) {
		Applicant applicant = applicantRepository.findById(applicantId)
				.orElseThrow(() -> new ResourceNotFoundException("pkId", "pkId not found"));
		ApplicantDto dto = new ApplicantDto(applicant.getFirstName(), applicant.getLastName(), applicant.getEmailId());
		ModelMapper modelmapper = new ModelMapper();
		modelmapper.map(applicant, dto);
		return dto;
	}

	@Override
	public Map<String, Boolean> delete(Long applicantId) {
		Map<String, Boolean> response = new HashMap<>();
		applicantRepository.findById(applicantId)
				.orElseThrow(() -> new ResourceNotFoundException("pkId", "Id not found"));
		applicantRepository.deleteById(applicantId);
		response.put("deleted", Boolean.TRUE);
		return response;

	}

	@Override
	public Map<String, Boolean> deleteAll() {
		Map<String, Boolean> response = new HashMap<>();
		applicantRepository.deleteAll();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@Override
	public ApplicantDto save(ApplicantDto applicantDto) {

		Optional<Applicant> applicant = applicantRepository.findByemailId(applicantDto.getEmailId());
		
		if(applicant.isPresent()) {
			applicant = buildApplicantDetails(applicantDto, applicant.get());
			ModelMapper mapper = new ModelMapper();
			mapper.map(applicantRepository.save(applicant.get()), applicantDto);
		}
		else {
			Optional<Applicant> applicantSave = buildApplicantDetails(applicantDto, new Applicant());
			ModelMapper mapper = new ModelMapper();
			mapper.map(applicantRepository.save(applicantSave.get()), applicantDto);

		}
		
		return applicantDto;
	}

	private Optional<Applicant> buildApplicantDetails(ApplicantDto applicantDto, Applicant applicant) {

		applicant = buildApplicantSaveAndUpdate(applicantDto, applicant);
		
		return Optional.ofNullable(applicant);
	}

	private Applicant buildApplicantSaveAndUpdate(ApplicantDto applicantDto, Applicant applicant) {

		if (applicant.getId() != null) {
			Optional<Applicant> foundApplicants = applicantRepository.findById(applicant.getId());
			if (foundApplicants.isPresent()) {
				applicant = foundApplicants.get();
				applicant.setAge(applicantDto.getAge());
				applicant.setEducationInfo(applicantDto.getEducationInfo());
				applicant.setEmailId(applicantDto.getEmailId());
				applicant.setYearsOfExp(applicantDto.getYearsOfExp());
				applicant.setFirstName(applicantDto.getFirstName());
				applicant.setLastName(applicantDto.getLastName());
				applicant.setIsSpousePresent(applicantDto.getIsSpousePresent());
				applicant.setAssessmentType(applicantDto.getAssessmentType());
			}
		} else {
			applicant = new Applicant();
			applicant.setAge(applicantDto.getAge());
			applicant.setEducationInfo(applicantDto.getEducationInfo());
			applicant.setEmailId(applicantDto.getEmailId());
			applicant.setYearsOfExp(applicantDto.getYearsOfExp());
			applicant.setFirstName(applicantDto.getFirstName());
			applicant.setLastName(applicantDto.getLastName());
			applicant.setIsSpousePresent(applicantDto.getIsSpousePresent());
			applicant.setAssessmentType(applicantDto.getAssessmentType());
		}

		return applicant;
	}

	@Override
	public ApplicantDto update(ApplicantDto applicantDto) {
		applicantRepository.findByemailId(applicantDto.getEmailId())
				.orElseThrow(() -> new ResourceNotFoundException("applicant", "applicant not found"));
		return save(applicantDto);
	}

}
