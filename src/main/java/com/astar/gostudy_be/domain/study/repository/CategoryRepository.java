package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
