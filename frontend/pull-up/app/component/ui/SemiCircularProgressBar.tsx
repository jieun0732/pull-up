import { useState } from "react";
import ProgressBar from "react-customizable-progressbar";

const SemiCircleProgressBar = () => {
  const [progress, setProgress] = useState(60);
  const [averageProgress, setAverageProgress] = useState(90);

  return (
    <div className="relative flex aspect-square flex-col items-center bg-green-500">
      <div className="absolute left-0 top-0">
        <ProgressBar
          progress={progress}
          radius={150}
          cut={200}
          rotate={190}
          initialAnimation={true}
          strokeColor="#4d70ec"
          strokeWidth={18}
          trackStrokeColor="#F2F3F6"
          trackStrokeWidth={18}
          strokeLinecap={"round"}
          pointerRadius={1}
          pointerStrokeWidth={10}
          pointerStrokeColor={"#4d70ec"}
        />
      </div>
      <div className="absolute left-0 top-0">
        <ProgressBar
          progress={averageProgress}
          radius={150}
          cut={200}
          rotate={190}
          initialAnimation={true}
          strokeColor="#4d6fec0"
          strokeWidth={18}
          trackStrokeColor="#f2f3f60"
          trackStrokeWidth={18}
          strokeLinecap={"round"}
          pointerRadius={1}
          pointerStrokeWidth={10}
          pointerStrokeColor={"black"}
        />
      </div>
      <div className="flex justify-center items-center text-center absolute top-0 w-full h-full mx-auto select-none">
  <p>dljlkj</p>
</div>
    </div>
  );
};

export default SemiCircleProgressBar;
