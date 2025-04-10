public class Backend {
    public void enableDevice(String device) {
        runPowerShellCommand("Enable", device);
    }
    public void disableDevice(String device) {
        runPowerShellCommand("Disable", device);
    }
    private void runPowerShellCommand(String action, String device) {
        String instanceId = switch (device.toLowerCase()) {
            case "camera" -> "USB\\VID_3277&PID_0022&MI_00\\6&5C9D285&0&0000";
            case "keyboard" -> "HID\\VID_3554&PID_FC03&MI_00\\7&C92CDF2&0&0000";
            case "mouse" -> "HID\\VID_3151&PID_1020&MI_01&COL01\\7&207A0A68&0&0000";
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
