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

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class InputsWrapper {
    private final HttpTransport httpTransport;
    private final CommonInputs commonInputs;
    private final JsonFactory jsonFactory;

    private InputsWrapper(Builder builder) {
        this.httpTransport = builder.httpTransport;
        this.commonInputs = builder.commonInputs;
        this.jsonFactory = builder.jsonFactory;
    }

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public static class Builder {
        private HttpTransport httpTransport;
        private CommonInputs commonInputs;
        private JsonFactory jsonFactory;

        public InputsWrapper build() { return new InputsWrapper(this); }

        public Builder withHttpTransport(HttpTransport httpTransport) {
            this.httpTransport = httpTransport;
            return this;
        }

        public Builder withCommonInputs(CommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public Builder withJsonFactory(JsonFactory jsonFactory) {
            this.jsonFactory = jsonFactory;
            return this;
        }
    }
}
