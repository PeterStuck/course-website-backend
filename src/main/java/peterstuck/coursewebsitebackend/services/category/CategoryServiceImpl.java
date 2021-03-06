package peterstuck.coursewebsitebackend.services.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peterstuck.coursewebsitebackend.exceptions.CategoryNotFoundException;
import peterstuck.coursewebsitebackend.models.course.Category;
import peterstuck.coursewebsitebackend.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static peterstuck.coursewebsitebackend.utils.ObjectUpdater.updateCategory;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    @Transactional
    public List<Category> getMainCategories() {
        return repository.findAll().stream()
                .filter(category -> category.getParentCategoryId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Category> getChildCategories(int parentCategoryId) {
        return repository.findAll().stream()
                .filter(category -> category.getParentCategoryId() == parentCategoryId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return repository.save(category);
    }

    @Override
    @Transactional
    public Category update(int categoryId, Category updated) throws CategoryNotFoundException {
        Category category = getCategoryOrThrowException(categoryId);
        updateCategory(category, updated);
        save(category);

        return category;
    }

    @Override
    @Transactional
    public void delete(int categoryId) throws CategoryNotFoundException {
        Category category = getCategoryOrThrowException(categoryId);
        repository.delete(category);
    }

    private Category getCategoryOrThrowException(int categoryId) throws CategoryNotFoundException {
        return repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + categoryId + " not found."));
    }
}
