package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.models.User;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.Email = :email")
	public User getUserByUserName(@Param("email") String email);

}
