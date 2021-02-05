package org.surface.surface.core;

import org.surface.surface.core.control.RemoteSnapshotsProjectsControl;
import org.surface.surface.core.control.SingleLocalProjectControl;

import java.nio.file.Path;
import java.util.Arrays;

public class Surface {
    private final SurfaceInput surfaceInput;

    public Surface(SurfaceInput surfaceInput) {
        this.surfaceInput = surfaceInput;
    }

    public void run() {
        String[] metricsCodes = surfaceInput.getMetricsCodes();
        System.out.println("* Going to compute the following metrics: " + Arrays.toString(metricsCodes) + ".");

        String exportFormat = surfaceInput.getExportFormat();
        System.out.println("* Going to export as " + exportFormat + " file.");

        Path remoteProjectsAbsolutePath = surfaceInput.getRemoteProjectsAbsolutePath();
        if (remoteProjectsAbsolutePath != null) {
            RemoteSnapshotsProjectsControl remoteSnapshotsProjectsControl = new RemoteSnapshotsProjectsControl(metricsCodes, remoteProjectsAbsolutePath, exportFormat);
            remoteSnapshotsProjectsControl.run();
        } else {
            Path projectAbsolutePath = surfaceInput.getProjectAbsolutePath();
            SingleLocalProjectControl singleLocalProjectControl = new SingleLocalProjectControl(metricsCodes, projectAbsolutePath);
            singleLocalProjectControl.run();
        }
    }
}
