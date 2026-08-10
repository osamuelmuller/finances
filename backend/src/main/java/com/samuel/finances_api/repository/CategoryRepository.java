package com.samuel.finances_api.repository;

import com.samuel.finances_api.entity.Category;
import com.samuel.finances_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    List<Category> findAllByUserId(Long id);

}