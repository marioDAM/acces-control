package com.example.repository;

import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByFirstName(String firstName, Pageable pageable);

    Page<User> findByAge(Integer age, Pageable pageable);

    Page<User> findByFirstNameAndAge(String firstName, Integer age, Pageable pageable);

}
