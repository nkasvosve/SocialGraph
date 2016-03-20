package com.codurance.social.ha.wall;

import com.codurance.social.ha.HazelcastHolder;
import com.codurance.social.model.Posting;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nickk
 */

public class HazelcastWallDelegate {

    private HazelcastInstance hazelcastInstance;
    public static final String WALL_MESSAGES = "wall-messages";

    public HazelcastWallDelegate(String configFile) throws Exception {

        Validate.notBlank(configFile, "configFile must not be blank");

        hazelcastInstance = HazelcastHolder.INSTANCE.getHazelcastInstance(configFile);
        if (hazelcastInstance == null) {
            throw new IllegalStateException("Could not create hazelcast instance");
        }
    }

    public void advise(Posting posting) {
        try {
            ITopic topic = hazelcastInstance.getTopic(WALL_MESSAGES);
            topic.publish(posting);
        } catch (Exception e) {
            LOG.error("Error sending message:", e);
        }
    }

    public String getLocalEndpoint() {
        return hazelcastInstance.getLocalEndpoint().getSocketAddress().toString();
    }

    private static Logger LOG = LoggerFactory.getLogger(HazelcastWallDelegate.class);
}
