package com.palusers.email.services;

import com.palusers.email.domain.EmailEntity;
import com.palusers.email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class EmailCreationService implements IEmailCreationService {

	private EmailRepository emailRepository;

	@Autowired
	public EmailCreationService(EmailRepository emailRepository)
	{
		this.emailRepository = emailRepository;
	}



	@Override
	public List<EmailEntity> GetEmailEntities(String status) {


		return emailRepository.GetEmailEntities(status);
	}



	@Override
	public void UpdateEmailStatus(Long id, String activationstatus, LocalDateTime dt) {
		emailRepository.UpdateEmailStatus(id, activationstatus);
	}

	@Override
	public void saveAll(EmailEntity entity) {
		emailRepository.save(entity);
	}
}
