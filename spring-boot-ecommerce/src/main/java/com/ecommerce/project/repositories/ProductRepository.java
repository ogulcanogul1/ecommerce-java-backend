package com.ecommerce.project.repositories;

import com.ecommerce.project.models.Category;
import com.ecommerce.project.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> getProductsByCategory(Category category, Sort sort);
    Page<Product> getProductsByCategory(Category category, Pageable pageable);

    List<Product> findByProductNameLikeIgnoreCase(String keyword,Sort sort);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);

}
