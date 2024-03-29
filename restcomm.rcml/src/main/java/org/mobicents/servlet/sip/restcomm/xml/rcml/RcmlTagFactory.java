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
package org.mobicents.servlet.sip.restcomm.xml.rcml;

import java.util.HashMap;
import java.util.Map;

import org.mobicents.servlet.sip.restcomm.ObjectInstantiationException;
import org.mobicents.servlet.sip.restcomm.annotations.concurrency.ThreadSafe;
import org.mobicents.servlet.sip.restcomm.xml.Attribute;
import org.mobicents.servlet.sip.restcomm.xml.Tag;
import org.mobicents.servlet.sip.restcomm.xml.TagFactory;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Action;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Beep;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.CallerId;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.EndConferenceOnExit;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.FinishOnKey;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.From;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.HangupOnStar;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Language;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Length;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Loop;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.MaxLength;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.MaxParticipants;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Method;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Muted;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.NumDigits;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.PlayBeep;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Reason;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.RingbackTone;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.SendDigits;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.StartConferenceOnEnter;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.StatusCallback;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.TimeLimit;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Timeout;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.To;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Transcribe;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.TranscribeCallback;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.TranscribeLanguage;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Url;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Voice;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.WaitMethod;
import org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.WaitUrl;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@ThreadSafe public final class RcmlTagFactory implements TagFactory {
  private static final Map<String, Class<? extends Attribute>> ATTRIBUTES;
  private static final Map<String, Class<? extends Tag>> TAGS;
  static {
    ATTRIBUTES = new HashMap<String, Class<? extends Attribute>>();
    ATTRIBUTES.put(Action.NAME, Action.class);
    ATTRIBUTES.put(Beep.NAME, Beep.class);
    ATTRIBUTES.put(CallerId.NAME, CallerId.class);
    ATTRIBUTES.put(EndConferenceOnExit.NAME, EndConferenceOnExit.class);
    ATTRIBUTES.put(FinishOnKey.NAME, FinishOnKey.class);
    ATTRIBUTES.put(From.NAME, From.class);
    ATTRIBUTES.put(HangupOnStar.NAME, HangupOnStar.class);
    ATTRIBUTES.put(Language.NAME, Language.class);
    ATTRIBUTES.put(Length.NAME, Length.class);
    ATTRIBUTES.put(Loop.NAME, Loop.class);
    ATTRIBUTES.put(MaxLength.NAME, MaxLength.class);
    ATTRIBUTES.put(MaxParticipants.NAME, MaxParticipants.class);
    ATTRIBUTES.put(Method.NAME, Method.class);
    ATTRIBUTES.put(Muted.NAME, Muted.class);
    ATTRIBUTES.put(NumDigits.NAME, NumDigits.class);
    ATTRIBUTES.put(PlayBeep.NAME, PlayBeep.class);
    ATTRIBUTES.put(Reason.NAME, Reason.class);
    ATTRIBUTES.put(org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Record.NAME,
        org.mobicents.servlet.sip.restcomm.xml.rcml.attributes.Record.class);
    ATTRIBUTES.put(RingbackTone.NAME, RingbackTone.class);
    ATTRIBUTES.put(SendDigits.NAME, SendDigits.class);
    ATTRIBUTES.put(StartConferenceOnEnter.NAME, StartConferenceOnEnter.class);
    ATTRIBUTES.put(StatusCallback.NAME, StatusCallback.class);
    ATTRIBUTES.put(TimeLimit.NAME, TimeLimit.class);
    ATTRIBUTES.put(Timeout.NAME, Timeout.class);
    ATTRIBUTES.put(To.NAME, To.class);
    ATTRIBUTES.put(Transcribe.NAME, Transcribe.class);
    ATTRIBUTES.put(TranscribeCallback.NAME, TranscribeCallback.class);
    ATTRIBUTES.put(TranscribeLanguage.NAME, TranscribeLanguage.class);
    ATTRIBUTES.put(Url.NAME, Url.class);
    ATTRIBUTES.put(Voice.NAME, Voice.class);
    ATTRIBUTES.put(WaitMethod.NAME, WaitMethod.class);
    ATTRIBUTES.put(WaitUrl.NAME, WaitUrl.class);
    
    TAGS = new HashMap<String, Class<? extends Tag>>();
    TAGS.put(Client.NAME, Client.class);
    TAGS.put(Conference.NAME, Conference.class);
    TAGS.put(Dial.NAME, Dial.class);
    TAGS.put(Fax.NAME, Fax.class);
    TAGS.put(Gather.NAME, Gather.class);
    TAGS.put(Hangup.NAME, Hangup.class);
    TAGS.put(Mms.NAME, Mms.class);
    TAGS.put(Number.NAME, Number.class);
    TAGS.put(Pause.NAME, Pause.class);
    TAGS.put(Play.NAME, Play.class);
    TAGS.put(Record.NAME, Record.class);
    TAGS.put(Redirect.NAME, Redirect.class);
    TAGS.put(Reject.NAME, Reject.class);
    TAGS.put(Response.NAME, Response.class);
    TAGS.put(Say.NAME, Say.class);
    TAGS.put(Sms.NAME, Sms.class);
    TAGS.put(Uri.NAME, Uri.class);
  }
  
  public RcmlTagFactory() {
    super();
  }

  public Attribute getAttributeInstance(final String name) throws ObjectInstantiationException {
	if(name == null) {
	  throw new NullPointerException("Can not instantiate an attribute named null");
	} else if(!ATTRIBUTES.containsKey(name)) {
	  throw new ObjectInstantiationException("The attribute named " + name +
	      " can not be instantiated because it is not a supported RestComm attribute.");
	} else {
	  try {
	    return ATTRIBUTES.get(name).newInstance();
	  } catch(final InstantiationException exception) {
		throw new ObjectInstantiationException(exception);
	  } catch(final IllegalAccessException exception) {
		throw new ObjectInstantiationException(exception);
	  }
	}
  }

  public Tag getTagInstance(final String name) throws ObjectInstantiationException {
	if(name == null) {
      throw new NullPointerException("Can not instantiate a tag with a null name.");
	} else if(!TAGS.containsKey(name)) {
      throw new ObjectInstantiationException("The <" + name + "> tag can not be instantiated because it is not a supported RestComm tag.");
	} else {
	  try {
		return TAGS.get(name).newInstance();
	  } catch(final InstantiationException exception) {
		throw new ObjectInstantiationException(exception);
	  } catch(final IllegalAccessException exception) {
		throw new ObjectInstantiationException(exception);
	  }
	}
  }
}
