package be.pxl.auctions.dao.impl;

import be.pxl.auctions.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class UserDaoImplTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private UserDaoImpl userDao;

	@Test
	public void userCanBeSavedAndRetrievedById() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		long newUserId = userDao.saveUser(user).getId();
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserById(newUserId);
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());
	}
	@Test
	public void userCanBeSavedAndRetrievedByEmail() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		Optional<User> retrievedUser = userDao.findUserByEmail("mark@facebook.com");
		assertTrue(retrievedUser.isPresent());

		assertEquals(user.getFirstName(), retrievedUser.get().getFirstName());
		assertEquals(user.getLastName(), retrievedUser.get().getLastName());
		assertEquals(user.getEmail(), retrievedUser.get().getEmail());
		assertEquals(user.getDateOfBirth(), retrievedUser.get().getDateOfBirth());
	}

	@Test
	public void returnsNullWhenNoUserFoundWithGivenEmail() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		assertTrue(userDao.findUserByEmail("elon@tesla.com").isEmpty());
	}

	@Test
	public void allUsersCanBeRetrieved() {
		User user = new User();
		user.setFirstName("Mark");
		user.setLastName("Zuckerberg");
		user.setDateOfBirth(LocalDate.of(1989, 5, 3));
		user.setEmail("mark@facebook.com");
		userDao.saveUser(user);
		entityManager.flush();
		entityManager.clear();

		List<User> allUsers = userDao.findAllUsers();

		assertEquals(1, allUsers.size());
		assertTrue(allUsers.contains(user));
	}


}
