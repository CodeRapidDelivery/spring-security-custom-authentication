package com.marbor.customauthentication.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.marbor.customauthentication.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
