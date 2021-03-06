/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.transport.passthru.config;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.ParameterInclude;
import org.apache.axis2.transport.base.threads.WorkerPool;
import org.apache.http.protocol.*;
import org.apache.synapse.transport.passthru.connections.TargetConnections;

/**
 * This class stores configuration specific to HTTP Connectors (Senders)
 */
public class TargetConfiguration extends BaseConfiguration {

    private int maxConnections = Integer.MAX_VALUE;

    /** Whether User-Agent header coming from client should be preserved */
    private boolean preserveUserAgentHeader = false;

    /** Whether Server header coming from server should be preserved */
    private boolean preserveServerHeader = true;

    private TargetConnections connections = null;

    public TargetConfiguration(ConfigurationContext configurationContext,
                               ParameterInclude parameters,
                               WorkerPool pool) {
        super(configurationContext, parameters, pool);
        maxConnections = conf.getIntProperty(
                PassThroughConfigPNames.MAX_CONNECTION_PER_TARGET,
                Integer.MAX_VALUE);
        preserveUserAgentHeader = conf.getBooleanProperty(
                PassThroughConfigPNames.USER_AGENT_HEADER_PRESERVE, false);
        preserveServerHeader = conf.getBooleanProperty(
                PassThroughConfigPNames.SERVER_HEADER_PRESERVE, true);
    }

    @Override
    protected HttpProcessor initHttpProcessor() {
        String userAgent = conf.getStringProperty(
                PassThroughConfigPNames.USER_AGENT_HEADER_VALUE,
                "Synapse-PT-HttpComponents-NIO");
        return new ImmutableHttpProcessor(
                new RequestContent(),
                new RequestTargetHost(),
                new RequestConnControl(),
                new RequestUserAgent(userAgent),
                new RequestExpectContinue(false));
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public boolean isPreserveUserAgentHeader() {
        return preserveUserAgentHeader;
    }

    public boolean isPreserveServerHeader() {
        return preserveServerHeader;
    }

    public TargetConnections getConnections() {
        return connections;
    }

    public void setConnections(TargetConnections connections) {
        this.connections = connections;
    }
}
