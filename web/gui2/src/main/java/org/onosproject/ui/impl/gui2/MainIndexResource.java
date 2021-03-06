/*
 * Copyright 2015-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.ui.impl.gui2;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.onlab.osgi.ServiceNotFoundException;
import org.onosproject.rest.AbstractInjectionResource;
import org.onosproject.ui.UiExtensionService;
import org.onosproject.ui.UiPreferencesService;
import org.onosproject.ui.UiSessionToken;
import org.onosproject.ui.UiTokenService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URI;

import static com.google.common.collect.ImmutableList.of;
import static com.google.common.io.ByteStreams.toByteArray;

/**
 * Resource for serving the dynamically composed index.html for GUI2.
 */
@Path("/")
public class MainIndexResource extends AbstractInjectionResource {

    private static final String INDEX_REDIRECT = "/onos/ui/index.html";

    private static final String INDEX = "index.html";
    private static final String NOT_READY = "not-ready.html";

    private static final String INJECT_USER_START = "<!-- {INJECTED-USER-START} -->";
    private static final String INJECT_USER_END = "<!-- {INJECTED-USER-END} -->";

    private static final byte[] SCRIPT_START = "\n<script>\n".getBytes();
    private static final byte[] SCRIPT_END = "</script>\n\n".getBytes();

    @Context
    private SecurityContext ctx;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getMainIndexRedirect() throws IOException {
        if (ctx == null || ctx.getUserPrincipal() == null) {
            return Response.temporaryRedirect(URI.create(INDEX_REDIRECT)).build();
        }
        return getMainIndex();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/index.html")
    public Response getMainIndex() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        UiExtensionService service;
        UiTokenService tokens;

        try {
            service = get(UiExtensionService.class);
            tokens = get(UiTokenService.class);

        } catch (ServiceNotFoundException e) {
            return Response.ok(classLoader.getResourceAsStream(NOT_READY)).build();
        }

        InputStream indexTemplate = classLoader.getResourceAsStream(INDEX);
        String index = new String(toByteArray(indexTemplate));

        int p0s = split(index,   0, INJECT_USER_START) - INJECT_USER_START.length();
        int p0e = split(index, p0s, INJECT_USER_END);
        int p3s = split(index, p0e, null);


        // FIXME: use global opaque auth token to allow secure failover

        // for now, just use the user principal name...
        String userName = ctx.getUserPrincipal().getName();

        // get a session token to use for UI-web-socket authentication
        UiSessionToken token = tokens.issueToken(userName);

        String auth = "var onosUser='" + userName + "',\n" +
                      "    onosAuth='" + token + "';\n";

        StreamEnumeration streams =
                new StreamEnumeration(of(stream(index, 0, p0s),
                        new ByteArrayInputStream(SCRIPT_START),
                        stream(auth, 0, auth.length()),
                        userPreferences(userName),
                        userConsoleLog(userName),
                        new ByteArrayInputStream(SCRIPT_END),
                        stream(index, p0e, p3s)));

        return Response.ok(new SequenceInputStream(streams)).build();
    }

    private InputStream userConsoleLog(String userName) {
        String code = "console.log('Logging in as user >" + userName + "<');\n";
        return new ByteArrayInputStream(code.getBytes());
    }

    // Produces an input stream including user preferences.
    private InputStream userPreferences(String userName) {
        UiPreferencesService service = get(UiPreferencesService.class);
        ObjectNode prefs = mapper().createObjectNode();
        service.getPreferences(userName).forEach(prefs::set);
        String string = "var userPrefs = " + prefs.toString() + ";\n";
        return new ByteArrayInputStream(string.getBytes());
    }

    private static final String NL = String.format("%n");
    private static final byte[] NL_BYTES = NL.getBytes();
}
