package com.codurance.social.ha;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nickk
 */

public enum HazelcastHolder {

    INSTANCE;

    private static HazelcastInstance hazelcastInstance;

    public HazelcastInstance getHazelcastInstance(String configFile) throws Exception {
        Validate.notBlank(configFile, "configFile must not be blank");

        if (LOG.isDebugEnabled()) LOG.debug("Trying to create hazelcast instance from holder with: {}", configFile);

        if (hazelcastInstance == null) {
            Config config = null;
            try {
                config = new ClasspathXmlConfig(configFile);
                hazelcastInstance = Hazelcast.newHazelcastInstance(config);
            } catch (Exception e) {
                LOG.error("Error", e);
                if (LOG.isDebugEnabled()) LOG.debug(String.format("Failed to get %s on the classpath. Trying to load from the file system instead", configFile));
                config = new FileSystemXmlConfig(configFile);
                hazelcastInstance = Hazelcast.newHazelcastInstance(config);
            }
        }
        return hazelcastInstance;
    }

    private static Logger LOG = LoggerFactory.getLogger(HazelcastHolder.class);
}
