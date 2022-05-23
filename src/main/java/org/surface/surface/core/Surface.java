package org.surface.surface.core;

import org.surface.surface.core.control.RemoteSnapshotsProjectsControl;
import org.surface.surface.core.control.SingleLocalProjectControl;

import java.nio.file.Path;
import java.util.Arrays;

public class Surface {
    private final RunSetting runSetting;

    public Surface(RunSetting runSetting) {
        this.runSetting = runSetting;
    }

    public void run() {
        String[] metricsCodes = runSetting.getMetricsCodes();
        System.out.println("* Going to compute the following metrics: " + Arrays.toString(metricsCodes) + ".");

        String exportFormat = runSetting.getExportFormat();
        String outFile = runSetting.getOutFile();
        System.out.println("* Going to export as " + exportFormat + " to file " + outFile + ".");

        Path remoteProjectsAbsolutePath = runSetting.getRemoteProjectsAbsolutePath();
        if (remoteProjectsAbsolutePath != null) {
            RemoteSnapshotsProjectsControl remoteSnapshotsProjectsControl = new RemoteSnapshotsProjectsControl(metricsCodes, exportFormat, outFile, remoteProjectsAbsolutePath);
            remoteSnapshotsProjectsControl.run();
        } else {
            Path projectAbsolutePath = runSetting.getProjectAbsolutePath();
            SingleLocalProjectControl singleLocalProjectControl = new SingleLocalProjectControl(metricsCodes, exportFormat, outFile, projectAbsolutePath);
            singleLocalProjectControl.run();
        }
    }
}
