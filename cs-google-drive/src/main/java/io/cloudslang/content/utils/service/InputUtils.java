/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.utils.service;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.common.base.Strings;
import io.cloudslang.content.entities.inputs.CommonInputs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.entities.constants.Constants.Values.SCOPES_DELIMITER;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class InputUtils {

    private InputUtils() {
        // prevent instantiation
    }

    public static CommonInputs getCommonInputs(String jsonToken, String scopes, String scopeDelimiter, String timeout) {
        HttpRequestInitializer httpRequestInitializer = getRequestInitializer(timeout);
        List<String> scopesAsList = getScopes(scopes, scopeDelimiter);
        final InputStream jsonStream =  new ByteArrayInputStream(jsonToken.getBytes());

        return new CommonInputs.Builder()
                .withScopes(scopesAsList)
                .withTimeout(httpRequestInitializer)
                .withJsonToken(jsonStream)
                .build();
    }

    private static HttpRequestInitializer getRequestInitializer(String timeout) {
        final int timeoutInt = Integer.valueOf(timeout);

        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) {
                httpRequest.setConnectTimeout(timeoutInt);
                httpRequest.setReadTimeout(timeoutInt);
            }
        };
    }

    private static List<String> getScopes(String scopes, String scopeDelimiter) {
        return !Strings.isNullOrEmpty(scopeDelimiter) ?
                Arrays.asList(scopes.split(scopeDelimiter)) :
                Arrays.asList(scopes.split(SCOPES_DELIMITER));
    }
}
