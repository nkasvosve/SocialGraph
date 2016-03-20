package com.codurance.social.ha.wall;

import com.codurance.social.model.Posting;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author nickk
 *
 * This class is a service that acts as a glue between the user interface and back end systems
 * The backend systens include Hazelcast and Neo4j
 */

public class MessageService implements WallCallback {

    @Autowired
    private HazelcastWallRequestListener requestListener;

    @Autowired
    private HazelcastWallDelegate wallDelegate;

    @PostConstruct
    private void decorate() {
        requestListener.addServiceDiscoveryCallback(this);
    }

    public void monitor(Posting posting) {
        Validate.notNull(posting, "posting must not be null");
    }

    public HazelcastWallRequestListener getRequestListener() {
        return requestListener;
    }

    public HazelcastWallDelegate getWallDelegate() {
        return wallDelegate;
    }

    /**
     * This method should update the timeline of the user posting a message.
     *
     * @param posting
     */
    @Override
    public void update(Posting posting) {
    }
}
