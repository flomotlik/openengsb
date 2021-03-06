/**

   Copyright 2010 OpenEngSB Division, Vienna University of Technology

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.openengsb.ekb;

import javax.annotation.Resource;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;

import org.apache.servicemix.jbi.listener.MessageExchangeListener;

/**
 * Temporary transformer used to transform timing messages into messages
 * understand by the svn component.
 */
public class TimingSvnTransformer implements MessageExchangeListener {

    @Resource
    private DeliveryChannel channel;

    public void onMessageExchange(final MessageExchange exchange) throws MessagingException {
        System.out.println("Received exchange: " + exchange);
        exchange.setStatus(ExchangeStatus.DONE);
        this.channel.send(exchange);
    }

}
