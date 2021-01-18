package com.qbrainx_recruitment.event;

import org.springframework.context.ApplicationEvent;

import com.qbrainx_recruitment.dto.ApplicantDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantFormProcessEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8731552363375243305L;

	private ApplicantDto applicantDto;

	public ApplicantFormProcessEvent(final ApplicantDto applicantDto) {
		super(applicantDto);
		this.applicantDto = applicantDto;
	}

}
