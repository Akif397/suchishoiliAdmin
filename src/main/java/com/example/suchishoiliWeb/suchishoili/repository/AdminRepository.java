package com.example.suchishoiliWeb.suchishoili.repository;

import com.example.suchishoiliWeb.suchishoili.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    public Admin findByEmail(String email);
}
