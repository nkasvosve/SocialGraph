package com.codurance.social.ha.wall;

import com.codurance.social.ha.HazelcastHolder;
import com.codurance.social.model.Posting;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nickk
 */

public class HazelcastWallRequestListener implements MessageListener<Posting> {

    private HazelcastInstance hazelcastInstance;
    private Set<WallCallback> callBacks = new HashSet<>();

    public HazelcastWallRequestListener(String configFile) throws Exception {

        Validate.notBlank(configFile, "configFile must not be blank");

        hazelcastInstance = HazelcastHolder.INSTANCE.getHazelcastInstance(configFile);
        if (hazelcastInstance == null) {
            throw new IllegalStateException("Could not create hazelcast instance");
        }
    }

    public String getLocalEndpoint() {
        return hazelcastInstance.getLocalEndpoint().getSocketAddress().toString();
    }

    public void addServiceDiscoveryCallback(WallCallback callback) {
        callBacks.add(callback);
    }

    @PostConstruct
    @SuppressWarnings({"unchecked","rawtypes"})
    public void listen() {
        if (hazelcastInstance != null) {
            ITopic topic = hazelcastInstance.getTopic(HazelcastWallDelegate.WALL_MESSAGES);
            topic.addMessageListener(this);
            if (LOG.isDebugEnabled()) LOG.debug("Now listening on: " + HazelcastWallDelegate.WALL_MESSAGES);
        } else {
            throw new IllegalStateException("Could not create hazelcast instance to listen through");
        }
    }

    @Override
    public void onMessage(Message<Posting> deploymentManagementMessage) {
        if (LOG.isDebugEnabled())
            LOG.debug("HazelcastWallRequestListener received {}", deploymentManagementMessage.getMessageObject());

        for (WallCallback callback : callBacks) {
            callback.update(deploymentManagementMessage.getMessageObject());
        }
    }

    private static Logger LOG = LoggerFactory.getLogger(HazelcastWallRequestListener.class);
}
