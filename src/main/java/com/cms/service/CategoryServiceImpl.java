package com.cms.service;

import com.cms.dto.CategoryDTO;
import com.cms.entity.Category;
import com.cms.exception.ResourceNotFoundException;
import com.cms.exception.DuplicateResourceException;
import com.cms.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new DuplicateResourceException(
                    "Category with name " + categoryDTO.getName() + " already exists");
        }

        Category category = mapper.map(categoryDTO, Category.class);

        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        return mapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with name: " + name));

        return mapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> mapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO updateCategory(Integer id,
                                      CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id));

        if (!category.getName().equals(categoryDTO.getName()) &&
                categoryRepository.existsByName(categoryDTO.getName())) {

            throw new DuplicateResourceException(
                    "Category with name " + categoryDTO.getName() + " already exists");
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return mapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Integer id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Category not found with id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}
