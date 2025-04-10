public class Backend {
    public void enableDevice(String device) {
        runPowerShellCommand("Enable", device);
    }
    public void disableDevice(String device) {
        runPowerShellCommand("Disable", device);
    }
    private void runPowerShellCommand(String action, String device) {
        String instanceId = switch (device.toLowerCase()) {
            case "camera" -> "USB\\VIDXXX"; // -> Add your camera INSTANCEID
            case "keyboard" -> "HID\\VIDXXXX"; // -> Add  external keyboard INSTANCEID
            case "mouse" -> "HID\\VIDXXXXX"; // -> Add external mouse INSTANCEID
            default -> "";
        };
        if (instanceId.isEmpty()) return;
        String command = String.format(
            "powershell.exe -Command \"Get-PnpDevice -InstanceId '%s' | %s-PnpDevice -Confirm:$false\"",
            instanceId, action
        );
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);
            builder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
