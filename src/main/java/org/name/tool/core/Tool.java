package org.name.tool.core;

import org.name.tool.core.control.RemoteSnapshotsProjectsControl;
import org.name.tool.core.control.SingleLocalProjectControl;

import java.nio.file.Path;
import java.util.Arrays;

public class Tool {
    private final ToolInput toolInput;

    public Tool(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    public void run() {
        String[] metricsCodes = toolInput.getMetricsCodes();
        System.out.println("* Going to compute the following metrics: " + Arrays.toString(metricsCodes) + ".");

        String exportFormat = toolInput.getExportFormat();
        System.out.println("* Going to export as " + exportFormat + " file.");

        Path remoteProjectsAbsolutePath = toolInput.getRemoteProjectsAbsolutePath();
        if (remoteProjectsAbsolutePath != null) {
            RemoteSnapshotsProjectsControl remoteSnapshotsProjectsControl = new RemoteSnapshotsProjectsControl(metricsCodes, exportFormat, remoteProjectsAbsolutePath);
            remoteSnapshotsProjectsControl.run();
        } else {
            Path projectAbsolutePath = toolInput.getProjectAbsolutePath();
            SingleLocalProjectControl singleLocalProjectControl = new SingleLocalProjectControl(metricsCodes, exportFormat, projectAbsolutePath);
            singleLocalProjectControl.run();
        }
    }
}
