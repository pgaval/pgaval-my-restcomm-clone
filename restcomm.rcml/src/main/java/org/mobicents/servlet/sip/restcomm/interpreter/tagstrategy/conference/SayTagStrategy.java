/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.servlet.sip.restcomm.interpreter.tagstrategy.conference;

import java.net.URI;

import org.mobicents.servlet.sip.restcomm.annotations.concurrency.NotThreadSafe;
import org.mobicents.servlet.sip.restcomm.entities.Notification;
import org.mobicents.servlet.sip.restcomm.interpreter.ConferenceRcmlInterpreterContext;
import org.mobicents.servlet.sip.restcomm.interpreter.RcmlInterpreter;
import org.mobicents.servlet.sip.restcomm.interpreter.RcmlInterpreterContext;
import org.mobicents.servlet.sip.restcomm.interpreter.TagStrategyException;
import org.mobicents.servlet.sip.restcomm.media.api.Conference;
import org.mobicents.servlet.sip.restcomm.xml.rcml.RcmlTag;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@NotThreadSafe public final class SayTagStrategy extends ConferenceRcmlTagStrategy  {
  private String gender;
  private String language;
  private int loop;
  private String text;
  
  public SayTagStrategy() {
    super();
  }
  
  @Override public void execute(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    final URI announcement = say(gender, language, text);
    final ConferenceRcmlInterpreterContext conferenceContext = (ConferenceRcmlInterpreterContext)context;
    final Conference conference = conferenceContext.getConference();
    if(loop == 0) {
      while(Conference.Status.IN_PROGRESS.equals(conference.getStatus()) &&
          interpreter.isRunning()) {
    	conference.play(announcement);
      }
    } else {
	  conference.play(announcement, loop);
    }
  }
  
  @Override public void initialize(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    initGender(interpreter, context, tag);
    initLanguage(interpreter, context, tag);
    initLoop(interpreter, context, tag);
    initText(interpreter, context, tag);
  }
  
  private void initGender(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    gender = getGender(interpreter, context, tag);
	if(gender == null) {
	  interpreter.notify(context, Notification.WARNING, 13511);
	  gender = "man";
	}
  }
  
  private void initLanguage(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    language = getLanguage(interpreter, context, tag);
    if(language == null) {
      language = "en";
    }
  }
  
  private void initLoop(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    loop = getLoop(interpreter, context, tag);
    if(loop == -1) {
      interpreter.notify(context, Notification.WARNING, 13510);
      loop = 1;
    }
  }
  
  private void initText(final RcmlInterpreter interpreter, final RcmlInterpreterContext context,
      final RcmlTag tag) throws TagStrategyException {
    text = tag.getText();
    if(text == null || text.isEmpty()) {
  	  interpreter.notify(context, Notification.WARNING, 13520);
  	  throw new TagStrategyException("There is no text to synthesize.");
  	}
  }
}
