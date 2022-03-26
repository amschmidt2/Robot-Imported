// /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

// package frc.robot;

// import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.GenericHID.Hand;
// import edu.wpi.first.wpilibj.XboxController.Button;
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.SpeedController;
// import edu.wpi.first.wpilibj.SpeedControllerGroup;
// import edu.wpi.first.wpilibj.buttons.JoystickButton;
// import edu.wpi.first.wpilibj.buttons.Trigger;
// import edu.wpi.first.wpilibj.motorcontrol.Victor;
// import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.SolenoidBase;
// import edu.wpi.first.wpilibj.DoubleSolenoid;
// import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

// /**
//  * The VM is configured to automatically run this class, and to call the
//  * functions corresponding to each mode, as described in the TimedRobot
//  * documentation. If you change the name of this class or the package after
//  * creating this project, you must also update the build.gradle file in the
//  * project.
//  */
// public class Robot extends TimedRobot {

//   private final Boolean setState = false;

//   // Robots Motors for the wheels
//   private final Victor left = new Victor(0);
//   private final Victor right = new Victor(1);
//   private final Victor winch = new Victor(2);
//   public static final DoubleSolenoid up = new DoubleSolenoid(0, 2);
//   public static final DoubleSolenoid down = new DoubleSolenoid(3, 1);

//   // private Joystick driverJoystick = new Joystick(0); (use this if you are using
//   // logitech controller)
//   // Drive chain
//   private final DifferentialDrive drive = new DifferentialDrive(left, right);
//   public XboxController xStick = new XboxController(0);
//   /*
//    * private XboxController pneaumaticsControlIn = new XboxController(1); private
//    * XboxController pneumaticsControlOut = new XboxController(2);
//    */
//   public JoystickButton Out = new JoystickButton(xStick, 1);
//   boolean state;

//   public void pneumatics() {

//     state = false;
//     final double Outtake = -1 * xStick.getRawAxis(1);
//     final double Intake = -1 * xStick.getRawAxis(5);

//     DoubleSolenoid.Value upState;
//     if (Outtake > 0.9) {
//       upState = Value.kForward;
//     } else if (Outtake < -9) {
//       upState = Value.kReverse;
//     } else {
//       upState = Value.kOff;
//     }

//     final DoubleSolenoid.Value downState;
//     if (Intake > 0.9) {
//       downState = Value.kForward;
//     } else if (Intake < -9) {
//       downState = Value.kReverse;
//     } else {
//       downState = Value.kOff;
//     }

//     SmartDashboard.putBoolean("Pneumatics Controls setState", setState);
//     if (setState) {
//       //SmartDashboard.putString("Left Piston", upState);
//       //SmartDashboard.putString("Right Piston", downState);

//       up.set(upState);
//       down.set(downState);
//     } else {
//       Up(Value.kOff);
//       Down(Value.kOff);
//     }
//   };

//   public void toggle() {
//     if (!state) {
//       state = true;
//     } else {
//       state = false;
//     }
//   }

//   public void Up(final DoubleSolenoid.Value state) {
//     SmartDashboard.putString("Left Piston", state + "");
//     up.set(state);
//   }

//   public void Down(final DoubleSolenoid.Value state) {
//     SmartDashboard.putString("Left Piston", state + "");
//     down.set(state);
//   }

//   private String m_autoSelected;
//   SendableChooser auto_chooser;
//   Timer autoTimer;
//   int auto_choice;

//   /**
//    * This function is run when the robot is first started up and should be used
//    * for any initialization code.
//    */

//   @Override
//   public void robotInit() {
//     // Camera
//     final CameraServer server = CameraServer.getInstance();
//     server.startAutomaticCapture(0);

//     auto_chooser = new SendableChooser();
//     auto_chooser.addOption("Auto 1", 1);
//     auto_chooser.addOption("Auto 2", 2);
//     auto_chooser.setDefaultOption("Default", 3);
//     SmartDashboard.putData("Auto Choices", auto_chooser);
//     autoTimer = new Timer();

//   }

//   @Override
//   public void autonomousInit() {
//     auto_choice = ((Integer) (auto_chooser.getSelected())).intValue();
//     autoTimer.reset();
//     autoTimer.start();

//   }

//   @Override
//   public void autonomousPeriodic() {
//     switch (auto_choice) {
//     case 1:
//       auto1();
//       break;
//     case 2:
//       auto2();
//     default:
//       break;
//     }

//   }

//   @Override
//   public void teleopInit() {
//   }

//   @Override
//   public void teleopPeriodic() {
//     // power turn for drive
//     final double power = -xStick.getRawAxis(1);
//     final double turn = xStick.getRawAxis(4);
//     double winch = 0;
//     // Program for button
//     if (xStick.getRawButton(7) == true) {
//       winch = 1;
//     } else if (xStick.getRawButton(8)) {
//       winch = -1;
//     }
//     //winch.setwinch(1);
//     // make robot go go
//     drive.arcadeDrive(power * 0.8, turn * 0.8);

//   }

//   @Override
//   public void testInit() {
//   }

//   @Override
//   public void testPeriodic() {
//   }

//   public void auto1() {
//     if (autoTimer.get() < 2) {
//       left.set(0.5);
//       right.set(0.5);
//     } else {
//       left.set(0);
//       right.set(0);
//       autoTimer.stop();
//     }
//   }

//   public void auto2() {
//     if (autoTimer.get() > 2 && autoTimer.get() < 4) {
//       left.set(0.5);
//       right.set(0.5);
//     } else if (autoTimer.get() > 4) {
//       left.set(0);
//       right.set(0);
//       autoTimer.stop();
//       autoTimer.reset();
//     }
//   }
// }
