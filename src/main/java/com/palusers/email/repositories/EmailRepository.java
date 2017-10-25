package com.palusers.email.repositories;

import com.palusers.email.domain.EmailEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 299787 on 13/10/17.
 */
public interface EmailRepository extends CrudRepository<EmailEntity, Long> {


    @Query("select email from EmailEntity email where email.status =?1")
    List<EmailEntity> GetEmailEntities(String status);

    @Transactional
    @Modifying
    @Query("Update EmailEntity email set email.status =?2 where email.id =?1")
    void UpdateEmailStatus(Long id,String status);

}
