"use client";

import { IconProp } from "@/types/IconProp";

function CloseIcon({ onClick }: IconProp) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="25"
      height="25"
      viewBox="0 0 25 25"
      fill="none"
      onClick={onClick}
      className="absolute left-0"
    >
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M2.13124 4.36566C1.64369 3.87811 0.853212 3.87811 0.365662 4.36566C-0.121888 4.85321 -0.121887 5.64369 0.365662 6.13124L5.95665 11.7222L0.365675 17.3132C-0.121875 17.8008 -0.121875 18.5912 0.365675 19.0788C0.853225 19.5663 1.6437 19.5663 2.13125 19.0788L7.72223 13.4878L13.3132 19.0788C13.8008 19.5663 14.5912 19.5663 15.0788 19.0788C15.5663 18.5912 15.5663 17.8008 15.0788 17.3132L9.4878 11.7222L15.0788 6.13124C15.5663 5.64369 15.5663 4.85321 15.0788 4.36566C14.5912 3.87811 13.8008 3.87811 13.3132 4.36566L7.72223 9.95665L2.13124 4.36566Z"
        fill="#3D4150"
      />
    </svg>
  );
}

export default CloseIcon;
