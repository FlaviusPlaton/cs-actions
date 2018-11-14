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
package io.cloudslang.content.actions;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.entities.inputs.CommonInputs;
import io.cloudslang.content.entities.inputs.InputsWrapper;
import io.cloudslang.content.execute.DriveAuthService;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static io.cloudslang.content.utils.service.HttpTransportUtils.getNetHttpTransport;
import static io.cloudslang.content.utils.service.InputUtils.getCommonInputs;
import static io.cloudslang.content.utils.service.JsonFactoryUtils.getJsonFactory;

/**
 * @author Flavius-Rareș Platon
 * 11/14/2018
 */
public class GetAuthToken {
    @Action(name = "Get authentication token for Google Drive API",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = JSON_TOKEN, required = true) String jsonToken,
            @Param(value = SCOPES, required = true) String scopes,
            @Param(value = SCOPES_DELIMITER) String scopesDelimiter,
            @Param(value = TIMEOUT) String timeout,
            @Param(value = PROXY_HOST) String proxyHost,
            @Param(value = PROXY_PORT) String proxyPort,
            @Param(value = PROXY_USERNAME) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword)  {

        try {
            final HttpTransport httpTransport = getNetHttpTransport(proxyHost, proxyPort, proxyUsername, proxyPassword);
            final CommonInputs commonInputs = getCommonInputs(jsonToken, scopes, scopesDelimiter, timeout);
            final JsonFactory jsonFactory = getJsonFactory();

            final InputsWrapper inputsWrapper = new InputsWrapper.Builder()
                    .withHttpTransport(httpTransport)
                    .withCommonInputs(commonInputs)
                    .withJsonFactory(jsonFactory)
                    .build();

            final String authToken = DriveAuthService.getAccessToken(inputsWrapper);
            return getSuccessResultsMap(authToken);
        } catch(Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
