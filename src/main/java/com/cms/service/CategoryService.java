package com.cms.service;

import com.cms.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(Integer id);

    CategoryDTO getCategoryByName(String name);

    List<CategoryDTO> getAllCategories();

    CategoryDTO updateCategory(Integer id, CategoryDTO categoryDTO);

    void deleteCategory(Integer id);
}