"use client";

import { useEffect, useState } from "react";
import Text from "../ui/Text";

const ReplaySpeechBubble = function () {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
    }, 3000);

    return () => clearTimeout(timer);
  }, []);

  if (!isVisible) return null;

  return (
    <div className="absolute left-[-25px] top-[-50px] whitespace-nowrap rounded-[0.4em] bg-black01 px-4 py-2 text-white after:absolute after:bottom-0 after:left-1/4 after:mb-[-8px] after:h-0 after:w-0 after:-translate-x-1/2 after:border-[9px] after:border-b-0 after:border-transparent after:border-t-black01 after:content-['']">
      <Text size="caption-02">문제를 다시 풀 수 있어요!</Text>
    </div>
  );
};

export default ReplaySpeechBubble;
