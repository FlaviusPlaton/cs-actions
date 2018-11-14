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
package io.cloudslang.content.entities.inputs;

import com.google.api.client.http.HttpRequestInitializer;

import java.io.InputStream;
import java.util.List;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class CommonInputs {
    private final List<String> scopes;
    private final HttpRequestInitializer requestInitializer;
    private final InputStream jsonToken;

    private CommonInputs(Builder builder) {
        this.scopes = builder.scopes;
        this.requestInitializer = builder.requestInitializer;
        this.jsonToken = builder.jsonToken;
    }

    public List<String> getScopes() { return scopes; }

    public HttpRequestInitializer getRequestInitializer() { return requestInitializer; }

    public InputStream getJsonToken() { return jsonToken; }

    public static class Builder {
        private List<String> scopes;
        private HttpRequestInitializer requestInitializer;
        private InputStream jsonToken;

        public CommonInputs build() { return new CommonInputs(this); }

        public Builder withScopes(List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder withTimeout(HttpRequestInitializer requestInitializer) {
            this.requestInitializer = requestInitializer;
            return this;
        }

        public Builder withJsonToken(InputStream jsonToken) {
            this.jsonToken = jsonToken;
            return this;
        }
    }
}
