#!/bin/sh

bazel-bin/mediapipe/examples/desktop/hand_tracking/hand_tracking_out_cpu --calculator_graph_config_file=mediapipe/graphs/hand_tracking/hand_tracking_desktop_live.pbtxt

# 1. this script should be in /mediapipe directory, you can find path in StartScreenController
# 2. file my_run.cc which you can find near current file should be in /mediapipe directory too
# 3. Add this lines to file /mediapipe/mediapipe/examples/BUILD

#cc_library(
#    name = "my_run",
#    srcs = ["my_run.cc"],
#    deps = [
#        "//mediapipe/calculators/util:landmarks_to_render_data_calculator",
#        "//mediapipe/framework:calculator_framework",
#        "//mediapipe/framework/formats:image_frame",
#        "//mediapipe/framework/formats:image_frame_opencv",
#        "//mediapipe/framework/formats:landmark_cc_proto",
#        "//mediapipe/framework/port:commandlineflags",
#        "//mediapipe/framework/port:file_helpers",
#        "//mediapipe/framework/port:opencv_highgui",
#        "//mediapipe/framework/port:opencv_imgproc",
#        "//mediapipe/framework/port:opencv_video",
#        "//mediapipe/framework/port:parse_text_proto",
#        "//mediapipe/framework/port:status",
#    ],
#)

#4. Add this lines to file /mediapipe/mediapipe/examples/desktop/hand_tracking/BUILD

#cc_binary(
#    name = "hand_tracking_out_cpu",
#    deps = [
#        "//mediapipe/examples/desktop:my_run",
#        "//mediapipe/graphs/hand_tracking:desktop_tflite_calculators",
#    ],
#)