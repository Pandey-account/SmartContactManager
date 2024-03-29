package com.smart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.models.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer> {
    
	@Query("from Contact as c where c.user.id =:userId")
	//current Page-page
//	Contact per page-5
	public Page<Contact> findContactsByUser(@Param("userId") int userId,Pageable pageable);
	
	//pagination
	
}
