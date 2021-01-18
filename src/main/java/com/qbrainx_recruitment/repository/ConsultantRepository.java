package com.qbrainx_recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qbrainx_recruitment.model.Consultant;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

}
