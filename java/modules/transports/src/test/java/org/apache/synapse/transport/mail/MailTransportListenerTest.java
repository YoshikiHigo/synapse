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

package org.apache.synapse.transport.mail;

import static org.apache.synapse.transport.testkit.AdapterUtils.adapt;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.synapse.transport.testkit.AdapterUtils;
import org.apache.synapse.transport.testkit.SimpleTransportDescriptionFactory;
import org.apache.synapse.transport.testkit.TransportDescriptionFactory;
import org.apache.synapse.transport.testkit.TransportTestSuite;
import org.apache.synapse.transport.testkit.client.AsyncTestClient;
import org.apache.synapse.transport.testkit.listener.ContentTypeMode;
import org.apache.synapse.transport.testkit.message.MessageConverter;
import org.apache.synapse.transport.testkit.message.XMLMessage;
import org.apache.synapse.transport.testkit.server.axis2.AxisAsyncEndpointFactory;
import org.apache.synapse.transport.testkit.server.axis2.AxisServer;

public class MailTransportListenerTest extends TestCase {
    public static TestSuite suite() throws Exception {
        TransportTestSuite suite = new TransportTestSuite();
        
        // TODO: these test don't work; need more analysis why this is so
        suite.addExclude("(&(messageType=SOAP12)(data=Latin1))");
        suite.addExclude("(&(messageType=POX)(data=Latin1))");
        suite.addExclude("(&(client=multipart)(data=Latin1))");
        suite.addExclude("(&(client=multipart)(messageType=POX))");
        suite.addExclude("(test=AsyncSwA)");
        suite.addExclude("(test=AsyncBinary)");
        suite.addExclude("(&(test=AsyncTextPlain)(!(data=ASCII)))");
        
        MailTestEnvironment env = new MailTestEnvironment();
        
        TransportDescriptionFactory tdf =
            new SimpleTransportDescriptionFactory(MailConstants.TRANSPORT_NAME,
                    MailTransportListener.class, MailTransportSender.class);
        AxisServer axisServer = new AxisServer(tdf);
        AxisAsyncEndpointFactory asyncEndpointFactory = new AxisAsyncEndpointFactory(axisServer);
        MailChannel channel = new MailChannel();
        List<MailClient> clients = new LinkedList<MailClient>();
        clients.add(new MimeClient());
        clients.add(new MultipartClient());
        for (MailClient client : clients) {
            AsyncTestClient<XMLMessage> xmlClient = adapt(client, MessageConverter.XML_TO_BYTE);
            suite.addSOAPTests(env, channel, xmlClient, asyncEndpointFactory, ContentTypeMode.TRANSPORT);
            suite.addPOXTests(env, channel, xmlClient, asyncEndpointFactory, ContentTypeMode.TRANSPORT);
            suite.addSwATests(env, channel, client, asyncEndpointFactory);
            suite.addTextPlainTests(env, channel, AdapterUtils.adapt(client, MessageConverter.STRING_TO_BYTE), AdapterUtils.adapt(asyncEndpointFactory, MessageConverter.AXIS_TO_STRING), ContentTypeMode.TRANSPORT);
            suite.addBinaryTest(env, channel, client, AdapterUtils.adapt(asyncEndpointFactory, MessageConverter.AXIS_TO_BYTE), ContentTypeMode.TRANSPORT);
        }
        return suite;
    }
}
