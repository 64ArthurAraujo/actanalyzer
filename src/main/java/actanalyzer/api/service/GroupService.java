package actanalyzer.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import actanalyzer.api.repository.GroupRepository;
import actanalyzer.api.service.implementation.GroupServiceInterface;
import actanalyzer.database.table.Group;

@Service
public class GroupService implements GroupServiceInterface {
	
	@Autowired
	private GroupRepository repository;
	
	public GroupService(GroupRepository repository) {
		this.repository = repository;
	}

	@Override
	public Group getById(Group entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group insert(Group entity) {
		return repository.save(entity);
	}

	@Override
	public Iterable<Group> getAllGroups() {
		return repository.findAll();
	}
}
