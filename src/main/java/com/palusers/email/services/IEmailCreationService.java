package com.palusers.email.services;

import com.palusers.email.domain.EmailEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IEmailCreationService {

	List<EmailEntity> GetEmailEntities(String status);
	void UpdateEmailStatus(Long id, String status, LocalDateTime dt);

	void saveAll(EmailEntity entity);
}
