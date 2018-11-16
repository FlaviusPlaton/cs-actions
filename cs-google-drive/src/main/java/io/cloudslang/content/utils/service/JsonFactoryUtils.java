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

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class JsonFactoryUtils {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private JsonFactoryUtils() {
        // prevent instantiation
    }

    public static JsonFactory getJsonFactory() {
        return JSON_FACTORY;
    }
}