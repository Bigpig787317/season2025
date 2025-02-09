// package frc.robot.subsystems.vision.apriltagvision;
//
// import static frc.robot.GlobalConstants.FieldMap.APRIL_TAG_FIELD_LAYOUT;
//
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Pose3d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.smartdashboard.Field2d;
// import frc.robot.Robot;
// import frc.robot.subsystems.vision.VisionConstants.CameraConstants;
// import frc.robot.subsystems.vision.apriltagvision.AprilTagVisionHelpers.PoseEstimate;
// import java.util.ArrayList;
// import java.util.Optional;
// import java.util.function.Supplier;
// import org.photonvision.EstimatedRobotPose;
// import org.photonvision.PhotonCamera;
// import org.photonvision.PhotonPoseEstimator;
// import org.photonvision.PhotonPoseEstimator.PoseStrategy;
// import org.photonvision.simulation.PhotonCameraSim;
// import org.photonvision.simulation.SimCameraProperties;
// import org.photonvision.simulation.VisionSystemSim;
// import org.photonvision.targeting.PhotonPipelineResult;
// import org.photonvision.targeting.PhotonTrackedTarget;
//
// public class AprilTagVisionIOPhotonSim implements AprilTagVisionIO {
//
//  private final VisionSystemSim sim;
//  private final PhotonCamera[] cameras;
//  private final PhotonPoseEstimator[] poseEstimators;
//  public final Supplier<Pose2d> drivePoseSupplier;
//  private double previousTimestamp = 0.0;
//
//  private static final String NAME = "TagVisionSim";
//
//  private final CameraConstants[] constantsList;
//
//  public AprilTagVisionIOPhotonSim(
//      Supplier<Pose2d> drivePoseSupplier, CameraConstants... constantsList) {
//    this.constantsList = constantsList;
//    sim = new VisionSystemSim(NAME);
//    sim.addAprilTags(APRIL_TAG_FIELD_LAYOUT);
//
//    cameras = new PhotonCamera[constantsList.length];
//
//    poseEstimators = new PhotonPoseEstimator[cameras.length];
//
//    for (int i = 0; i < cameras.length; i++) {
//      CameraConstants constants = constantsList[i];
//
//      PhotonCamera camera = new PhotonCamera(constants.cameraName());
//      cameras[i] = camera;
//
//      poseEstimators[i] =
//          new PhotonPoseEstimator(
//              APRIL_TAG_FIELD_LAYOUT,
//              PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
//              camera,
//              constants.robotToCamera());
//      poseEstimators[i].setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
//
//      SimCameraProperties cameraProperties = new SimCameraProperties();
//      cameraProperties.setCalibration(854, 480, Rotation2d.fromDegrees(120));
//      cameraProperties.setCalibError(0.25, 0.15);
//      cameraProperties.setFPS(25.0);
//      cameraProperties.setAvgLatencyMs(30.0);
//      cameraProperties.setLatencyStdDevMs(5.0);
//
//      PhotonCameraSim simCamera = new PhotonCameraSim(camera, cameraProperties);
//
//      sim.addCamera(simCamera, constants.robotToCamera());
//      // simCamera.enableDrawWireframe(true);
//    }
//
//    this.drivePoseSupplier = drivePoseSupplier;
//  }
//
//  @Override
//  public void updateInputs(AprilTagIOInputsLogged inputs) {
//    sim.update(drivePoseSupplier.get());
//
//    // because there's no better way to log a dynamic-length list of estimates
//    ArrayList<PoseEstimate> poseEstimates = new ArrayList<>();
//    Optional<EstimatedRobotPose>[] poseEstimateOptionals = getSimPoses();
//
//    for (int i = 0; i < cameras.length; i++) {
//      PhotonCamera camera = cameras[i];
//
//      PhotonPipelineResult result = camera.getLatestResult();
//      double timestamp = result.getTimestampSeconds();
//
//      // why check if enabled?
//      // don't we want estimation to continue updating even while disabled?
//      if (!result.targets.isEmpty() && DriverStation.isEnabled()) {
//        if (poseEstimateOptionals[i].isEmpty()) continue;
//        Pose3d estimatedPose = poseEstimateOptionals[i].get().estimatedPose;
//
//        double averageTagDistance = 0.0;
//
//        for (PhotonTrackedTarget target : result.targets) {
//          Optional<Pose3d> tagPose = APRIL_TAG_FIELD_LAYOUT.getTagPose(target.getFiducialId());
//          // invalid field tag, don't try to add to average
//          if (tagPose.isEmpty()) continue;
//
//          averageTagDistance +=
//              estimatedPose
//                  .getTranslation()
//                  .toTranslation2d()
//                  .getDistance(tagPose.get().toPose2d().getTranslation());
//        }
//
//        // invalid tags are still added to the average – how do we stop this?
//        // ideally it doesn't matter in simulation since the field is preloaded
//        // with the right tags, but like... :/
//        averageTagDistance = averageTagDistance / result.targets.size();
//
//        poseEstimates.add(
//            new PoseEstimate(estimatedPose, timestamp, averageTagDistance,
// result.targets.size()));
//      }
//    }
//
//    inputs.poseEstimates = poseEstimates;
//  }
//
//  public Optional<EstimatedRobotPose>[] getSimPoses() {
//    var optionalPoseEstimates =
//        new Optional[cameras.length]; // can an array of generified objects be type-inferred?
//
//    for (int i = 0; i < cameras.length; i++) {
//      Optional<EstimatedRobotPose> estimatedRobotPose = poseEstimators[i].update();
//      double latestTimestamp = cameras[i].getLatestResult().getTimestampSeconds();
//      boolean isNewResult = Math.abs(latestTimestamp - previousTimestamp) > 1e-5;
//
//      if (estimatedRobotPose.isPresent()) {
//        getDebugField()
//            .getObject("VisionEstimation/" + i)
//            .setPose(estimatedRobotPose.get().estimatedPose.toPose2d());
//      } else {
//        if (isNewResult) getDebugField().getObject("VisionEstimation/" + i).setPoses();
//      }
//
//      if (isNewResult) previousTimestamp = latestTimestamp;
//
//      optionalPoseEstimates[i] = estimatedRobotPose;
//    }
//    return optionalPoseEstimates;
//  }
//
//  /** A Field2d for visualizing our robot and objects on the field. */
//  public Field2d getDebugField() {
//    return !Robot.isSimulation() ? null : sim.getDebugField();
//  }
//
//  @Override
//  public double[] getNoisyDistances() {
//    double[] distances = new double[cameras.length];
//    for (int i = 0; i < cameras.length; i++) {
//      distances[i] = constantsList[i].cameraType().getNoisyDistance();
//    }
//    return distances;
//  }
// }
