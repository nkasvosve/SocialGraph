package com.codurance.social.app;

import com.codurance.social.model.Posting;
import com.codurance.social.model.User;
import com.codurance.social.repository.PostingRepository;
import com.codurance.social.repository.UserRepository;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author nickk
 */
public class CommandProcessorTest {

    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private CommandProcessor processor;
    private UserRepository userRepository;
    private PostingRepository postingRepository;
    private Neo4jTemplate template;

    @Before
    public void setup() {
        processor = new CommandProcessor();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        userRepository = Mockito.mock(UserRepository.class);
        postingRepository = Mockito.mock(PostingRepository.class);
        template = Mockito.mock(Neo4jTemplate.class);

        processor.setPostingRepository(postingRepository);
        processor.setTemplate(template);
        processor.setUserRepository(userRepository);

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void showTimelineForUser() throws Exception{

        User user = new User();
        user.setUserName("JimBob");
        Posting posting = new Posting();
        posting.setPoster(user);
        posting.setMessage("This is a test posting");
        posting.setPosterName(user.getUserName());
        user.getPostings().add(posting);

        Mockito.stub(template.fetch(user.getPostings())).toReturn(user.getPostings());
        Mockito.stub(template.fetch(posting)).toReturn(posting);

        processor.showTimelineFor(user);
        org.junit.Assert.assertEquals("JimBob - This is a test posting ()", outContent.toString().trim());

    }

    @Test
    public void showWallForUser() {

        User user = new User();
        user.setUserName("JimBob");
        Posting posting = new Posting();
        posting.setPoster(user);
        posting.setMessage("This is a test posting");
        posting.setPosterName(user.getUserName());
        user.getPostings().add(posting);

        Mockito.stub(template.fetch(user.getPostings())).toReturn(user.getPostings());
        Mockito.stub(template.fetch(posting)).toReturn(posting);
        Mockito.stub(template.fetch(user.getFollows())).toReturn(new HashSet<>());
        Mockito.stub(template.fetch(user.getFollowers())).toReturn(new HashSet<>());
        processor.showWall(user);
        org.junit.Assert.assertEquals("JimBob - This is a test posting ()", outContent.toString().trim());
    }

    @Test
    public void showPrintPostingsForUser() {
        User user = new User();
        user.setUserName("JimBob");
        Posting posting = new Posting();
        posting.setPoster(user);
        posting.setMessage("This is a test posting");
        posting.setPosterName(user.getUserName());
        List<Posting> list = new ArrayList<>();
        list.add(posting);

        Mockito.stub(template.fetch(user.getPostings())).toReturn(user.getPostings());
        Mockito.stub(template.fetch(posting)).toReturn(posting);
        Mockito.stub(template.fetch(user.getFollows())).toReturn(new HashSet<>());
        Mockito.stub(template.fetch(user.getFollowers())).toReturn(new HashSet<>());
        processor.printPostings(list, new DateTime());
        org.junit.Assert.assertEquals("JimBob - This is a test posting ()", outContent.toString().trim());
    }

    @Test
    public void showFollowing() {
        User user = new User();
        user.setUserName("JimBob");
        Posting posting = new Posting();
        posting.setPoster(user);
        posting.setMessage("This is a test posting");
        posting.setPosterName(user.getUserName());
        user.getPostings().add(posting);

        Mockito.stub(template.fetch(user.getPostings())).toReturn(user.getPostings());
        Mockito.stub(template.fetch(posting)).toReturn(posting);
        Mockito.stub(template.fetch(user.getFollows())).toReturn(new HashSet<>());
        Mockito.stub(template.fetch(user.getFollowers())).toReturn(new HashSet<>());
        processor.showWall(user);
        org.junit.Assert.assertEquals("JimBob - This is a test posting ()", outContent.toString().trim());
    }
}
