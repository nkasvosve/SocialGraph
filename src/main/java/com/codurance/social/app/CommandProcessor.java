package com.codurance.social.app;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.codurance.social.ha.wall.MessageService;
import com.codurance.social.model.Posting;
import com.codurance.social.model.User;
import com.codurance.social.repository.PostingRepository;
import com.codurance.social.repository.UserRepository;

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
	private MessageService messageService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostingRepository postingRepository;

	public void scanForInput() {

		String userName = null;
		Scanner scanner = new Scanner(System.in);

		userName = getUserIdentifier(userName, scanner);

		User theUser = userRepository.findByUserName(userName);

		if (theUser == null) {
			theUser = new User();
			theUser.setUserName(userName);
			userRepository.save(theUser);
			ansi_println("Created user " + theUser);
		} else {
			ansi_println("Found user " + theUser);
		}

		/// -------------start interaction processing----------------
		printHelp(userName);
		scanProcessInputAndLoop(scanner);
	}

	private String getUserIdentifier(String userName, Scanner scanner) {
		// prompt for identifier
		while (StringUtils.isBlank(userName)) {
			ansi_print("Please enter your name:");
			userName = scanner.nextLine();
		}
		return userName;
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
							ansi_println(String.format("Failed to find followed user %s, " + "Please try again",
									followedUserName));
							continue;
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

	private void showTimelineFor(User targetUser) {
		System.out.println("show timeline for : " + targetUser);
	}

	private void showWall(User targetUser) {
		System.out.println("show wall for : " + targetUser);
	}

	private void follow(User currentUser, User targetUser) {
		System.out.println(currentUser + "now following: " + targetUser);
	}

	private void postUpdate(User currentUser, String updateText) {
		System.out.println("posting update: " + updateText);
		Posting posting = new Posting();
		posting.setPoster(currentUser);
		posting.setMessage(updateText);
		postingRepository.save(posting);
		currentUser.getPostings().add(posting);
		userRepository.save(currentUser);
		messageService.getWallDelegate().advise(posting);
	}

	private void printHelp(String userName) {
		// print some helpful hints
		System.out.println();
		ansi_println(String.format("Welcome %s", userName));
		ansi_println("Type a message to post to the board");
		ansi_println("Input a username to read a user's timeline");
		ansi_println("Input <username> 'follows' to follow a user, and input <username> 'wall' to see a user's wall");
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
}
