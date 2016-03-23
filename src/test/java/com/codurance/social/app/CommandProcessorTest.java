package com.codurance.social.app;

import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.neo4j.graphdb.Transaction;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.codurance.social.model.Posting;
import com.codurance.social.model.User;
import com.codurance.social.repository.PostingRepository;
import com.codurance.social.repository.UserRepository;

/**
 * @author nickk
 *
 */
public class CommandProcessorTest {

	private UserRepository userRepository;
	private PostingRepository postingRepository;
	private Neo4jTemplate template;
	private CommandProcessor processor;
	private GraphDatabase graphDatabase;
	private Transaction transaction;

	@org.junit.Before
	public void setup() {
		processor = new CommandProcessor();
		userRepository = Mockito.mock(UserRepository.class);
		postingRepository = Mockito.mock(PostingRepository.class);
		template = Mockito.mock(Neo4jTemplate.class);
		graphDatabase = Mockito.mock(GraphDatabase.class);
		transaction = Mockito.mock(Transaction.class);
		processor.setPostingRepository(postingRepository);
		processor.setTemplate(template);
		processor.setUserRepository(userRepository);

		Mockito.stub(template.getGraphDatabase()).toReturn(graphDatabase);
		Mockito.stub(graphDatabase.beginTx()).toReturn(transaction);
	}

	@Test
	public void showFollowProcessing() {
		User bob = new User();
		bob.setUserName("JimBob");

		User alice = new User();
		alice.setUserName("Alice");

		Mockito.stub(userRepository.findByUserName("JimBob")).toReturn(bob);
		Mockito.stub(userRepository.findByUserName("Alice")).toReturn(alice);

		processor.processInput("JimBob follows Alice");
		Set<User> followers = alice.getFollowers();
		org.junit.Assert.assertEquals(1, followers.size());
	}

	@Test
	public void showUpdateWithPost() {
		User user = new User();
		user.setUserName("JimBob");
		Mockito.stub(userRepository.findByUserName("JimBob")).toReturn(user);
		processor.processInput("JimBob -> Hello there");
		Set<Posting> postings = user.getWall().getPostings();
		org.junit.Assert.assertEquals(1, postings.size());
	}
	
	@Test
	public void showTimelineForUsers() {
		User bob = new User();
		bob.setUserName("JimBob");

		User alice = new User();
		alice.setUserName("Alice");

		Mockito.stub(userRepository.findByUserName("JimBob")).toReturn(bob);
		Mockito.stub(userRepository.findByUserName("Alice")).toReturn(alice);

		processor.processInput("JimBob follows Alice");
		processor.processInput("JimBob -> Hello there");
		processor.processInput("JimBob -> I am hungry people");
		processor.processInput("Alice -> What a day");

		org.junit.Assert.assertEquals(2, bob.getPostings().size());
		org.junit.Assert.assertEquals(1, alice.getPostings().size());
	}
	
	@Test
	public void showReadingOfUserMessage() {
		User bob = new User();
		bob.setUserName("JimBob");

		User alice = new User();
		alice.setUserName("Alice");

		Mockito.stub(userRepository.findByUserName("JimBob")).toReturn(bob);
		Mockito.stub(userRepository.findByUserName("Alice")).toReturn(alice);

		processor.processInput("JimBob follows Alice");
		processor.processInput("JimBob -> Hello there");
		processor.processInput("JimBob -> I need lunch");
		processor.processInput("Alice -> I am going home");

		org.junit.Assert.assertEquals(2, bob.getPostings().size());
		org.junit.Assert.assertEquals(1, alice.getPostings().size());				
	}
}
