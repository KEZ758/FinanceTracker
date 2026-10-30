package kz.KEZ.FinanceTracker.service;

import kz.KEZ.FinanceTracker.models.Category;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getUserCategories(User user) {
        return categoryRepository.findAllByUserIsNullOrUser(user);
    }

    public Category createCategory(String name, User user) {
        Category category = new Category(name, user);
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
}
