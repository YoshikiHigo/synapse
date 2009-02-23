/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.synapse.mediators.builtin;

import org.apache.synapse.SynapseContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Halts further processing/mediation of the current message. i.e. returns false
 */
public class DropMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(LogMediator.class);

    /**
     * Halts further mediation of the current message by returning false.
     * @param synCtx the current message
     * @return false always
     */
    public boolean mediate(SynapseContext synCtx) {
        log.debug(getType() + " mediate()");
        if (synCtx.getSynapseMessage().getTo() == null) {
            return false;
        } else {
            synCtx.getSynapseMessage().setTo(null);
            return false;
        }
    }
}