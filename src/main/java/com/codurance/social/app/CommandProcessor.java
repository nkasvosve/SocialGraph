package com.codurance.social.app;

import java.util.Set;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import com.codurance.social.model.Posting;
import com.codurance.social.model.User;
import com.codurance.social.repository.PostingRepository;
import com.codurance.social.repository.UserRepository;

import scala.collection.mutable.HashSet;

/**
 * @author nickk
 *
 *         Listens for input on the console. It parses the input and translates
 *         the input into commands to be processed. The commands are 'posting',
 *         'reading', 'following' and 'wall' A command always starts with a
 *         userName such as 'following Pete'
 */

public class CommandProcessor {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_CYAN = "\u001B[36m";

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostingRepository postingRepository;
	@Autowired
	private Neo4jTemplate template;

	public void scanForInput() {
		Scanner scanner = new Scanner(System.in);
		printHelp();
		scanProcessInputAndLoop(scanner);
	}

	private void scanProcessInputAndLoop(Scanner scanner) {
		while (true) {

			ansi_print(">"); // show prompt

			String userInput = scanner.nextLine(); // read input interactively

			if (!userInput.isEmpty()) { // skip processing of empty input

				String[] tokens = userInput.split(" ");
				String operationalUserName = tokens[0].trim();

				User operationalUser = userRepository.findByUserName(operationalUserName);
				if (operationalUser == null) {
					operationalUser = new User();
					operationalUser.setUserName(operationalUserName);
					userRepository.save(operationalUser);
					ansi_println("Created user " + operationalUserName);
				}

				if (tokens.length == 1) { // reading user time line
					showTimelineFor(operationalUser);
				} else if (tokens.length == 2) { // probably a command since
													// we have a sentence
													// with two words
					String literalCommand = tokens[1].trim();
					if (literalCommand.equalsIgnoreCase("wall")) {
						showWall(operationalUser);
					} else {
						ansi_println("Failed to process command, Please try again");
						printHelp();
						continue;
					}
				} else if (tokens.length == 3) {
					// post it, must be an update
					String literalCommand = tokens[1].trim();
					if ("->".equals(literalCommand)) {
						postUpdate(operationalUser, userInput);
					} else if ("follows".equalsIgnoreCase(literalCommand)) {
						String followedUserName = tokens[2].trim();
						User followedUser = userRepository.findByUserName(followedUserName);
						if (followedUser == null) {
							ansi_println(String.format("Failed to find followed user %s. Now creating them",
									followedUserName));
							followedUser = new User();
							followedUser.setUserName(followedUserName);
							userRepository.save(followedUser);
							ansi_println("Created user " + followedUserName);
						}
						follow(operationalUser, followedUser);
					}
				} else if (userInput.contains("->")) {
					String literalCommand = tokens[1].trim();
					if ("->".equals(literalCommand)) {
						int index = StringUtils.ordinalIndexOf(userInput, " ", 2);
						postUpdate(operationalUser, userInput.substring(index));
					}
				} else {
					ansi_println("Failed to process the command. Please try again");
				}
			}
		}
	}

	void showTimelineFor(User targetUser) {
		DateTime now = new DateTime();
		List<Posting> list = new ArrayList<>();

		Set<Posting> postings = template.fetch(targetUser.getPostings());
		list.addAll(postings);

		Collections.sort(list, new Comparator<Posting>() {
			@Override
			public int compare(Posting posting1, Posting posting2) {
				return posting1.getDateCreated().compareTo(posting2.getDateCreated());
			}
		});
		printPostings(list, now);
	}

	void showWall(User targetUser) {
		DateTime now = new DateTime();

		List<Posting> list = new ArrayList<>();
		Set<Posting> allPostings = new java.util.HashSet<>();

		Set<Posting> postings = template.fetch(targetUser.getPostings());
		allPostings.addAll(postings);

		Set<User> follows = template.fetch(targetUser.getFollows());

		for (User followedUser : follows) {
			allPostings.addAll(template.fetch(followedUser.getPostings()));
		}

		list.addAll(allPostings);

		Collections.sort(list, new Comparator<Posting>() {
			@Override
			public int compare(Posting posting1, Posting posting2) {
				return posting1.getDateCreated().compareTo(posting2.getDateCreated());
			}
		});
		printPostings(list, now);
	}

	void printPostings(List<Posting> postings, DateTime now) {

		for (Posting posting : postings) {
			posting = template.fetch(posting);
			Period period = new Period(posting.getDateCreated(), now);
			PeriodFormatter formatter = new PeriodFormatterBuilder().appendMinutes().appendSuffix(" minute(s) ")
					.appendSeconds().appendSuffix(" second(s) ago").toFormatter();
			String elapsed = formatter.print(period);

			template.fetch(posting.getPoster());
			System.out.println(posting.getPoster().getUserName() + " - " + posting.getMessage() + " (" + elapsed + ")");
		}
	}

	void follow(User currentUser, User targetUser) {

		GraphDatabase gdb = template.getGraphDatabase();
		Transaction tx = gdb.beginTx();
		template.fetch(currentUser);
		template.fetch(targetUser);

		currentUser.getFollows().add(targetUser);
		targetUser.getFollowers().add(currentUser);

		tx.success();
		tx.finish();
	}

	void postUpdate(User currentUser, String updateText) {
		Posting posting = new Posting();
		posting.setPoster(currentUser);
		posting.setMessage(updateText);
		posting.setPosterName(currentUser.getUserName());
		postingRepository.save(posting);
		GraphDatabase gdb = template.getGraphDatabase();
		Transaction tx = gdb.beginTx();
		currentUser.getPostings().add(posting);
		currentUser.getWall().getPostings().add(posting);
		tx.success();
		tx.finish();
	}

	private void printHelp() {
		// print some helpful hints
		System.out.println();

		ansi_println("Type a message to post to the board, e.g. jimmy -> hello my friends");
		ansi_println("Type a friend'name to read their time line");
		ansi_println("Type <username> 'follows' to follow a friend");
		ansi_println("Type <username> 'wall' to see a friend's wall");
		ansi_println("Press CNTRL-C to exit");
		System.out.println();
	}

	private void ansi_print(String str) {
		if (SystemUtils.IS_OS_WINDOWS) {
			System.out.print(str);
		} else {
			System.out.print(ANSI_CYAN + str + ANSI_RESET);
		}
	}

	private void ansi_println(String str) {
		if (SystemUtils.IS_OS_WINDOWS) {
			System.out.println(str);
		} else {
			System.out.println(ANSI_CYAN + str + ANSI_RESET);
		}
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setPostingRepository(PostingRepository postingRepository) {
		this.postingRepository = postingRepository;
	}

	public void setTemplate(Neo4jTemplate template) {
		this.template = template;
	}
}
