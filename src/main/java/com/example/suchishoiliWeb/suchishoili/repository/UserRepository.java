package com.example.suchishoiliWeb.suchishoili.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.suchishoiliWeb.suchishoili.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByNameAndPhoneNumber(String name, String phoneNumber);
}
