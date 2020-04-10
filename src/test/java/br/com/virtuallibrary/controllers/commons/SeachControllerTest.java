package br.com.virtuallibrary.controllers.commons;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.virtuallibrary.commons.controllers.impl.SeachController;
import br.com.virtuallibrary.commons.services.ISearchService;

@RunWith(SpringRunner.class)
public class SeachControllerTest {

	private SeachController controller;
	
	@Before
	public void setUp() {
		controller = Mockito.mock(SeachController.class, Mockito.RETURNS_MOCKS);
	}
	
	@Test
	public void testGetService() {
		Mockito.when(controller.getService()).thenReturn((ISearchService) new Object());
		ISearchService iSearchService = controller.getService();
		assertNotNull(iSearchService);
	}
	
	@Test
	public void testGetPagedResourcesAssembler() {
		Mockito.when(controller.getPagedResourcesAssembler()).thenReturn((PagedResourcesAssembler) new Object());
		PagedResourcesAssembler pagedResourcesAssembler = controller.getPagedResourcesAssembler();
		assertNotNull(pagedResourcesAssembler);
	}
	
	@Test
	public void testGetModelAssembler() {
		RepresentationModelAssemblerSupport representationModelAssemblerSupport = controller.getModelAssembler();
	}
	
	@Test
	public void testFindAll() {
		Map<String, String> filters = new HashMap<String, String>();
		ResponseEntity<CollectionModel> responseEntity = controller.findAll(0, 1, filters);
	}
	
}
