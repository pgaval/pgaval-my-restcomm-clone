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
package org.mobicents.servlet.sip.restcomm.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.thoughtworks.xstream.XStream;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;

import org.joda.time.DateTime;

import org.mobicents.servlet.sip.restcomm.ServiceLocator;
import org.mobicents.servlet.sip.restcomm.Sid;
import org.mobicents.servlet.sip.restcomm.dao.AccountsDao;
import org.mobicents.servlet.sip.restcomm.dao.DaoManager;
import org.mobicents.servlet.sip.restcomm.entities.Account;
import org.mobicents.servlet.sip.restcomm.entities.AccountList;
import org.mobicents.servlet.sip.restcomm.entities.RestCommResponse;
import org.mobicents.servlet.sip.restcomm.http.converter.AccountConverter;
import org.mobicents.servlet.sip.restcomm.http.converter.AccountListConverter;
import org.mobicents.servlet.sip.restcomm.http.converter.RestCommResponseConverter;
import org.mobicents.servlet.sip.restcomm.util.StringUtils;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
public abstract class AccountsEndpoint extends AbstractEndpoint {
  protected final AccountsDao dao;
  protected final Gson gson;
  protected final XStream xstream;

  public AccountsEndpoint() {
    super();
    final ServiceLocator services = ServiceLocator.getInstance();
    dao = services.get(DaoManager.class).getAccountsDao();
    final AccountConverter converter = new AccountConverter(configuration);
    final GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Account.class, converter);
    builder.setPrettyPrinting();
    gson = builder.create();
    xstream = new XStream();
    xstream.alias("RestcommResponse", RestCommResponse.class);
    xstream.registerConverter(converter);
    xstream.registerConverter(new AccountListConverter(configuration));
    xstream.registerConverter(new RestCommResponseConverter(configuration));
  }
  
  private Account createFrom(final Sid accountSid, final MultivaluedMap<String, String> data) {
    validate(data);
    final Sid sid = Sid.generate(Sid.Type.ACCOUNT);
    final DateTime now = DateTime.now();
    final String emailAddress = data.getFirst("EmailAddress");
    String friendlyName = emailAddress;
    if(data.containsKey("FriendlyName")) {
      friendlyName = data.getFirst("FriendlyName");
    }
    final Account.Type type = Account.Type.FULL;
    Account.Status status = Account.Status.ACTIVE;
    if(data.containsKey("Status")) {
      status = Account.Status.valueOf(data.getFirst("Status"));
    }
    final String password = data.getFirst("Password");
    final String authToken = new Md5Hash(password).toString();
    final String role = data.getFirst("Role");
    String rootUri = configuration.getString("root-uri");
    rootUri = StringUtils.addSuffixIfNotPresent(rootUri, "/");
    final StringBuilder buffer = new StringBuilder();
    buffer.append(rootUri).append(getApiVersion(null)).append("/Accounts/").append(sid.toString());
    final URI uri = URI.create(buffer.toString());
    return new Account(sid, now, now, emailAddress, friendlyName, accountSid, type, status, authToken,
        role, uri);
  }
  
  protected Response getAccount(final String accountSid, final MediaType responseType) {
    final Sid sid = new Sid(accountSid);
    try { secure(sid, "RestComm:Read:Accounts"); }
	catch(final AuthorizationException exception) { return status(UNAUTHORIZED).build(); }
    final Account account = dao.getAccount(sid);
    if(account == null) {
      return status(NOT_FOUND).build();
    } else {
      if(APPLICATION_XML_TYPE == responseType) {
        final RestCommResponse response = new RestCommResponse(account);
        return ok(xstream.toXML(response), APPLICATION_XML).build();
      } else if(APPLICATION_JSON_TYPE == responseType) {
        return ok(gson.toJson(account), APPLICATION_JSON).build();
      } else {
        return null;
      }
    }
  }
  
  protected Response getAccounts(final MediaType responseType) {
    final Subject subject = SecurityUtils.getSubject();
    final Sid sid = new Sid((String)subject.getPrincipal());
    try { secure(sid, "RestComm:Read:Accounts"); }
	catch(final AuthorizationException exception) { return status(UNAUTHORIZED).build(); }
    final Account account = dao.getAccount(sid);
    if(account == null) {
      return status(NOT_FOUND).build();
    } else {
      final List<Account> accounts = new ArrayList<Account>();
      accounts.add(account);
      accounts.addAll(dao.getAccounts(sid));
      if(APPLICATION_XML_TYPE == responseType) {
        final RestCommResponse response = new RestCommResponse(new AccountList(accounts));
        return ok(xstream.toXML(response), APPLICATION_XML).build();
      } else if(APPLICATION_JSON_TYPE == responseType) {
        return ok(gson.toJson(accounts), APPLICATION_JSON).build();
      } else {
        return null;
      }
    }
  }
  
  protected Response putAccount(final MultivaluedMap<String, String> data, final MediaType responseType) {
    final Subject subject = SecurityUtils.getSubject();
    final Sid sid = new Sid((String)subject.getPrincipal());
    Account account = null;
    try { account = createFrom(sid, data); } catch(final NullPointerException exception) {
      return status(BAD_REQUEST).entity(exception.getMessage()).build();
    }
    if(subject.hasRole("Administrator") || (subject.isPermitted("RestComm:Create:Accounts"))) {
      final Account parent = dao.getAccount(sid);
      if(!subject.hasRole("Administrator") || !data.containsKey("Role")) {
        account = account.setRole(parent.getRole());
      }
      dao.addAccount(account);
    } else {
      return status(UNAUTHORIZED).build();
    }
    if(APPLICATION_JSON_TYPE == responseType) {
      return ok(gson.toJson(account), APPLICATION_JSON).build();
    } else if(APPLICATION_XML_TYPE == responseType) {
      final RestCommResponse response = new RestCommResponse(account);
      return ok(xstream.toXML(response), APPLICATION_XML).build();
    } else {
      return null;
    }
  }
  
  private Account update(final Account account, final MultivaluedMap<String, String> data) {
    Account result = account;
    if(data.containsKey("FriendlyName")) {
      result = result.setFriendlyName(data.getFirst("FriendlyName"));
    }
    if(data.containsKey("Status")) {
      result = result.setStatus(Account.Status.getValueOf(data.getFirst("Status")));
    }
    if(data.containsKey("Password")) {
      final String hash = new Md5Hash(data.getFirst("Password")).toString();
      result = result.setAuthToken(hash);
    }
    return result;
  }
  
  protected Response updateAccount(final String accountSid, final MultivaluedMap<String, String> data,
      final MediaType responseType) {
    final Sid sid = new Sid(accountSid);
    Account account = dao.getAccount(sid);
    if(account == null) {
      return status(NOT_FOUND).build();
    } else {
      account = update(account, data);
      final Subject subject = SecurityUtils.getSubject();
      if(subject.hasRole("Administrator") || (subject.getPrincipal().equals(accountSid) &&
          subject.isPermitted("RestComm:Modify:Accounts"))) {
        dao.updateAccount(account);
      } else {
        return status(UNAUTHORIZED).build();
      }
      if(APPLICATION_JSON_TYPE == responseType) {
        return ok(gson.toJson(account), APPLICATION_JSON).build();
      } else if(APPLICATION_XML_TYPE == responseType) {
        final RestCommResponse response = new RestCommResponse(account);
        return ok(xstream.toXML(response), APPLICATION_XML).build();
      } else {
        return null;
      }
    }
  }
  
  private void validate(final MultivaluedMap<String, String> data) throws NullPointerException {
    if(!data.containsKey("EmailAddress")) {
      throw new NullPointerException("Email address can not be null.");
    } else if(!data.containsKey("Password")) {
      throw new NullPointerException("Password can not be null.");
    }
  }
}
