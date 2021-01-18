package com.qbrainx_recruitment.service;

import javax.validation.Valid;

import com.qbrainx_recruitment.dto.ConsultantDto;

public interface ConsultantService {

	ConsultantDto save(@Valid ConsultantDto consultantDto);

}
