package org.actanalyzer.api.controller.retrieve;

import static org.actanalyzer.api.configuration.Settings.REQUEST_PATH_RETRIEVE;

import org.actanalyzer.api.service.implementation.GroupServiceInterface;
import org.actanalyzer.database.table.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = REQUEST_PATH_RETRIEVE)
public class GroupRetrieve {
	@Autowired
	private GroupServiceInterface groupService;
	
	@RequestMapping(method = RequestMethod.GET, path = "/groups/")
	public ResponseEntity<Iterable<Group>> getGroups() {
		
		return new ResponseEntity<Iterable<Group>>(groupService.getAllGroups(), HttpStatus.OK);
	}
}
