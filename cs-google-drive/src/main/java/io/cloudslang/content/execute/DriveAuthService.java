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
package io.cloudslang.content.execute;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import io.cloudslang.content.entities.inputs.CommonInputs;
import io.cloudslang.content.entities.inputs.InputsWrapper;

import java.io.IOException;
import java.io.InputStreamReader;

import static io.cloudslang.content.entities.constants.Constants.Values.ACCESS_TYPE;
import static io.cloudslang.content.entities.constants.Constants.Values.LOCAL_RECEIVER_PORT;
import static io.cloudslang.content.entities.constants.Constants.Values.TOKENS_DIRECTORY_PATH;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class DriveAuthService {

    private DriveAuthService () {
        // prevent instantiation
    }

    /**
     * Creates an authorized Credential object.
     * @return An authorized Credential object.
     * @throws IOException If the client_id.json file cannot be found.
     */
    private static Credential getCredentials(
            final HttpTransport httpTransport,
            final CommonInputs commonInputs,
            final JsonFactory jsonFactory) throws IOException {

        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(commonInputs.getJsonToken()));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, clientSecrets, commonInputs.getScopes()
                ).setRequestInitializer(commonInputs.getRequestInitializer())
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType(ACCESS_TYPE)
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(LOCAL_RECEIVER_PORT)
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static String getAccessToken(final InputsWrapper inputs) throws IOException {

        final Credential credentials = getCredentials(
                inputs.getHttpTransport(),
                inputs.getCommonInputs(),
                inputs.getJsonFactory());
        return credentials.getAccessToken();
    }

    public static Drive getService(final InputsWrapper inputs) throws IOException {

        // Build a new authorized API client service.
        final Credential credentials = getCredentials(
                inputs.getHttpTransport(),
                inputs.getCommonInputs(),
                inputs.getJsonFactory());
        return new Drive.Builder(
                    inputs.getHttpTransport(),
                    inputs.getJsonFactory(),
                    credentials
                ).build();
    }

}
