package io.cloudslang.content.utilities.services.osdetector;

import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.util.OsDetectorUtils;
import io.cloudslang.content.utilities.util.ProcessExecutor;
import io.cloudslang.content.utilities.util.ProcessResponseEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_HOST;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_PORT;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by Tirla Florin-Alin on 24/11/2017.
 **/
public class NmapOsDetectorService implements OsDetectorService {
    private static final String OS_DETAILS = "OS details: ";
    private static final String NMAP = "Nmap";
    private final OsDetectorUtils osDetectorUtils;

    public NmapOsDetectorService(OsDetectorUtils osDetectorUtils) {
        this.osDetectorUtils = osDetectorUtils;
    }

    @Override
    public OperatingSystemDetails detect(OsDetectorInputs osDetectorInputs) {
        OperatingSystemDetails operatingSystemDetails = new OperatingSystemDetails();
        List<String> nmapCommandOutput = new ArrayList<>();
        try {
            int timeout = parseInt(osDetectorInputs.getNmapTimeout());

            ProcessResponseEntity responseEntity = new ProcessExecutor().execute(buildNmapCommand(osDetectorInputs), timeout);
            if (responseEntity.isTimeout()) {
                addTimeoutErrorMessage(nmapCommandOutput);
            } else {
                String stdOut = responseEntity.getStdout();
                String stdErrors = responseEntity.getStderr();
                int exitCode = responseEntity.getExitCode();

                if (!isEmpty(stdOut)) {
                    nmapCommandOutput.add(stdOut);
                    String osName = osDetectorUtils.cropValue(stdOut, OS_DETAILS, System.lineSeparator());
                    operatingSystemDetails.setName(osName);
                    operatingSystemDetails.setFamily(osDetectorUtils.resolveOsFamily(osName));
                }
                if (!isEmpty(stdErrors)) {
                    nmapCommandOutput.add(stdErrors);
                }
                nmapCommandOutput.add("Execution of Nmap command had exit code: " + exitCode);
            }
        } catch (IOException e) {
            nmapCommandOutput.add("Failed to run Nmap command: " + e.getMessage());
        } catch (InterruptedException e) {
            nmapCommandOutput.add("Execution of Nmap command was canceled.");
        } catch (ExecutionException e) {
            nmapCommandOutput.add("An exception occurred while running the Nmap command: " + e.getMessage());
        } catch (TimeoutException e) {
            addTimeoutErrorMessage(nmapCommandOutput);
        }
        operatingSystemDetails.addCommandOutput(NMAP, nmapCommandOutput);
        return operatingSystemDetails;
    }

    private void addTimeoutErrorMessage(List<String> operatingSystemDetails) {
        operatingSystemDetails.add("The nmap command timed out");
    }

    private String buildNmapCommand(OsDetectorInputs osDetectorInputs) {
        String nmapPath = osDetectorInputs.getNmapPath();

        String nmapArguments = osDetectorInputs.getNmapArguments();
        String proxyHost = osDetectorInputs.getProxyHost();
        if (!contains(nmapArguments, "--proxies") && !contains(nmapArguments, "--proxy") && isNotEmpty(proxyHost)) {
            nmapArguments = appendProxyArgument(nmapArguments, proxyHost, osDetectorInputs.getProxyPort());
        }

        String host = osDetectorInputs.getHost();

        return nmapPath + " " + nmapArguments + " " + host;
    }

    @NotNull
    public String appendProxyArgument(String nmapArguments, String proxyHost, String proxyPort) {
        try {
            URL proxyUrl = new URL(proxyHost);
            if (!isValidIpPort(proxyPort)) {
                throw new IllegalArgumentException(format("The '%s' input does not contain a valid port.", PROXY_PORT));
            }
            nmapArguments += " --proxies " + proxyUrl.getProtocol() + "://" + proxyUrl.getHost() + ":" + proxyPort;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(format("The '%s' input does not contain a valid URL: %s.", PROXY_HOST, e.getMessage()));
        }
        return nmapArguments;
    }
}
