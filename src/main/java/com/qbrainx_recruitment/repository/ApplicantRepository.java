package com.qbrainx_recruitment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.model.Applicant;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

	ApplicantDto save(Long id);

	Optional<Applicant> findByemailId(String emailId);

}
