/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// Jeannine added
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import com.revrobotics.REVLibError;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;// import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
// import edu.wpi.first.wpilibj.SolenoidBase;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
// import edu.wpi.first.wpilibj.GenericHID.Hand;
// import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.SpeedController;
// import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private Joystick m_stick;
  // private static final int deviceID = 1;
  public PWMSparkMax m_motor;


  private final Boolean setState = true;

  // Robots Motors for the wheels
  private final Victor left = new Victor(0);
  private final Victor right = new Victor(1);
  private final Victor winch = new Victor(2);
  public static final DoubleSolenoid cyl1 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1); //top cyl
  public static final DoubleSolenoid cyl2 = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2,3); //bottom cyl

  // private Joystick driverJoystick = new Joystick(0); (use this if you are using
  // logitech controller)
  // Drive chain
  private final DifferentialDrive drive = new DifferentialDrive(left, right);
  public XboxController xStick = new XboxController(0);
  /*
   * private XboxController pneaumaticsControlIn = new XboxController(1); private
   * XboxController pneumaticsControlOut = new XboxController(2);
   */
  public JoystickButton Out = new JoystickButton(xStick, 1);
  boolean state;

  public void pneumatics() {
    state = false;
     final boolean YButton = xStick.getRawButton(4); //Y up
     final boolean AButton= xStick.getRawButton(1); //A (up)
     final boolean BButton= xStick.getRawButton(2); //
    //  final boolean AirDown2= xStick.getRawButton(3); //
    DoubleSolenoid.Value cyl2State;
    DoubleSolenoid.Value cyl1State;
    if (YButton) {
      cyl1State = Value.kForward;
      cyl2State = Value.kForward;
    } else if (AButton) {
      cyl1State = Value.kReverse;
      cyl2State = Value.kReverse;
    } else if (BButton){
      cyl1State = Value.kReverse;
      cyl2State = Value.kOff;
    }else {
      cyl1State = Value.kOff;
      cyl2State = Value.kOff;
    }

    

    SmartDashboard.putBoolean("Pneumatics Controls setState", setState);
    if (setState) {
      //SmartDashboard.putString("Left Piston", upState);
      //SmartDashboard.putString("Right Piston", downState);

      cyl1.set(cyl1State);
      cyl2.set(cyl2State);
    } else {
      Up(Value.kOff);
      Down(Value.kOff);
    }
  };

  public void shooters(){
    // m_motor.set(state);
    // m_motor.set(m_stick.getY());
    // xStick.getRawAxis(1);

  }

  public void toggle() {
    if (!state) {
      state = true;
    } else {
      state = false;
    }
  }

  public void Up(final DoubleSolenoid.Value state) {
    SmartDashboard.putString("Left Piston", state + "");
    cyl1.set(state);
  }

  public void Down(final DoubleSolenoid.Value state) {
    SmartDashboard.putString("Left Piston", state + "");
    cyl2.set(state);
  }

  // private String m_autoSelected;
  SendableChooser auto_chooser;
  Timer autoTimer;
  int auto_choice;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  @Override
  public void robotInit() {
    // Camera
    final CameraServer server = CameraServer.getInstance();
    server.startAutomaticCapture(0);

    auto_chooser = new SendableChooser();
    auto_chooser.setDefaultOption("Auto 1", 1);
    auto_chooser.addOption("Auto 2", 2);
    SmartDashboard.putData("Auto Choices", auto_chooser);
    autoTimer = new Timer();


    //m_motor = new PWMSparkMax(1, MotorType.kBrushed); //brushed 
    m_motor = new PWMSparkMax(3);
    // m_motor.restoreFactoryDefaults();

  }

  @Override
  public void autonomousInit() {
    auto_choice = ((Integer) (auto_chooser.getSelected())).intValue();
    autoTimer.reset();
    autoTimer.start();

  }

  @Override
  public void autonomousPeriodic() {
    switch (auto_choice) {
    case 1:
      auto1();
      break;
    case 2:
      auto2();
    default:
      break;
    }

  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    pneumatics();
    shooters();
    // power turn for drive
    final double power = xStick.getRawAxis(1);
   final double turn = xStick.getRawAxis(0);
    final double currentVolt = 8 * xStick.getRawAxis(5); //can move to higher voltage
    // Program for button

    this.winch.setVoltage(currentVolt);
    //this.winch.setVoltage(currentVolt);
    // make robot go go
    drive.arcadeDrive(power * 0.8, turn * 0.8);



    //sparkmax
    // Set motor output to joystick value
    m_motor.set(xStick.getRawAxis(3));


  }
  

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  public void auto1() {
    if (autoTimer.get() < 2) {
      left.set(0.5);
      right.set(-0.5);
    } else 
    {
      left.set(0);
      right.set(0);
      autoTimer.stop();
    }
  }

  public void auto2() {
    if (autoTimer.get() > 2 && autoTimer.get() < 4) {
      left.set(0.5);
      right.set(-0.5);
    } else if (autoTimer.get() > 4) 
    {
      left.set(0);
      right.set(0);
      autoTimer.stop();
      autoTimer.reset();
    }
  }
}
