# PC Controller - Java Swing UI to Enable/Disable Devices

This project is a Java Swing-based desktop application that lets you control (enable/disable) your PC's **Camera**, **Keyboard**, and **Mouse** through a graphical interface. The toggling is achieved by running PowerShell commands via Java’s ProcessBuilder. 

---

## Description

- **UI Features:**  
  The application displays a simple UI with toggle switches for three devices: Camera, Keyboard, and Mouse. All image files (e.g., `bgr.png`, `bg.png`, `camera.jpg`, `keyboard.jpg`, `mouse.jpg`) must reside in the same folder as the Java source code, so that relative paths can be used.  
  
- **Backend Logic:**  
  The backend (in `Backend.java`) uses PowerShell commands (`Disable-PnpDevice` and `Enable-PnpDevice`) to disable or enable devices based on their Windows **Instance IDs**. In the code you’ll see placeholder strings `"xxxxx"` where the Instance IDs should be entered.

- **Important Note on Device Toggling:**  
  You must run the Java application from PowerShell or CMD **as Administrator** because system-level hardware changes require elevated privileges.  
  Also, internal laptop keyboards or touchpads may not be disabled by these commands (due to Windows system protection) whereas external devices (e.g., USB keyboards/mice) usually work properly.

---

## Setup & Running the Project

1. **Obtain the Code:**  
   Ensure you have both `UI.java` and `Backend.java` files in the same folder, along with all required image files.

2. **Compile the Code:**  
   Open a terminal as **administrator** (CMD or PowerShell) and navigate to your project folder. Run:
   ```bash
   javac Backend.java UI.java

## How to Obtain INSTANCEIDs?
**Open powershell as administrator**
Run these commands
```
Get-PnpDevice -Class Camera | Select-Object Name, InstanceId
Get-PnpDevice -Class Keyboard | Select-Object Name, InstanceId
Get-PnpDevice -Class Mouse | Select-Object Name, InstanceId
```
In the `Backend.java` file, you will find a switch statement where the device Instance IDs are set. Initially, they use placeholders `"xxxxx"`:

```java
String instanceId = switch (device.toLowerCase()) {
    case "camera" -> "xxxxx";
    case "keyboard" -> "xxxxx";
    case "mouse" -> "xxxxx";
    default -> "";
};
```
Add your ids here
---
### Final Recommendations

- Run your terminal as **Administrator** when compiling and running the application.
- Replace the `"xxxxx"` placeholders in `Backend.java` with the actual **Instance IDs** obtained using the PowerShell commands provided above.
- For best results, use **external keyboards/mice**, since **internal devices** might not allow toggling via PowerShell due to Windows protection.
