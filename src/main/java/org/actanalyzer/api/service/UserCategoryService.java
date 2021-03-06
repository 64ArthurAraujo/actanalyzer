package org.actanalyzer.api.service;

import java.util.ArrayList;
import java.util.List;

import org.actanalyzer.api.repository.CategoryRepository;
import org.actanalyzer.api.repository.UserCategoryRepository;
import org.actanalyzer.api.repository.UserRepository;
import org.actanalyzer.api.service.implementation.UserCategoryServiceInterface;
import org.actanalyzer.database.table.Category;
import org.actanalyzer.database.table.User;
import org.actanalyzer.database.table.UserCategory;
import org.actanalyzer.database.table.util.CategorisedUserCategory;
import org.actanalyzer.database.table.util.ConvertedUserCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryService implements UserCategoryServiceInterface {
	
	@Autowired
	private UserCategoryRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserCategory getById(Long id) {
		return repository.findById(id).get();
	}
	
	public UserCategory getAlreadyCreatedUserCategoryRelation(UserCategory entity) {
		return repository.findById(getAlreadyCreatedCategory(entity).getId()).get();
	}

	@Override
	public ConvertedUserCategory insert(UserCategory entity) {
		if (categoryIsAlreadyCreated(entity)) {
			UserCategory alreadyCreatedCategory = getAlreadyCreatedUserCategoryRelation(entity);

			entity.setId(alreadyCreatedCategory.getId());
			entity.addInterest(1);
		} 
		
		return convertUserCategory(repository.save(entity));
	}

	private boolean categoryIsAlreadyCreated(UserCategory entity) {
		ConvertedUserCategory convertedEntity = convertUserCategory(entity);
		
		for (UserCategory categoryEntry : repository.findAll()) {
			ConvertedUserCategory convertedEntry = convertUserCategory(categoryEntry);
			
			if (convertedEntry.getIdCategory().equals(convertedEntity.getIdCategory())  &&
				convertedEntry.getIdUser().equals(convertedEntity.getIdUser())
			) {
				return true;
			}
		}
		
		return false;
	}
	
	private UserCategory getAlreadyCreatedCategory(UserCategory entity) {
		ConvertedUserCategory convertedEntity = convertUserCategory(entity);
		
		for (UserCategory categoryEntry : repository.findAll()) {
			ConvertedUserCategory convertedEntry = convertUserCategory(categoryEntry);

			if (convertedEntry.getIdCategory().equals(convertedEntity.getIdCategory())  &&
					convertedEntry.getIdUser().equals(convertedEntity.getIdUser())
			) {
				return categoryEntry;
			}
		}
		
		return null;
	}

	private ConvertedUserCategory convertUserCategory(UserCategory userCategory) {
		Category category = categoryRepository.findByName(userCategory.getCategoryName());
		User user = userRepository.findByAuthToken(userCategory.getUserToken());
		
		ConvertedUserCategory converted = new ConvertedUserCategory(
			userCategory.getId(), category.getId(),
			user.getId(), userCategory.getInterestRate()
		);
		
		return converted;
	}
	
	public CategorisedUserCategory convertToCategorisedUserCategory(UserCategory userCategory) {
		User user = userRepository.findByAuthToken(userCategory.getUserToken());
		
		CategorisedUserCategory converted = new CategorisedUserCategory(
			userCategory.getId(), userCategory.getCategoryName(),
			user.getUsername(), userCategory.getInterestRate()
		);
		
		return converted;
	}

	@Override
	public Iterable<UserCategory> listUserCategories(Long userId) {
		List<UserCategory> categoriesFromUser = new ArrayList<UserCategory>();
		
		for (UserCategory categoryEntry : repository.findAll()) {
			ConvertedUserCategory convertedCategoryEntry = convertUserCategory(categoryEntry);
			
			if (convertedCategoryEntry.getIdUser() == userId) {
				categoriesFromUser.add(categoryEntry);
			}
		}
		
		return categoriesFromUser;
	}
}
