"use client";

import { IconProp } from "@/types/IconProp";

function BackIcon({ onClick }: IconProp) {
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
      <g clip-path="url(#clip0_404_5508)">
        <path
          fill-rule="evenodd"
          clip-rule="evenodd"
          d="M9.98538e-05 12.122C-0.00357205 12.3826 0.0940259 12.6444 0.292893 12.8433L1.4714 14.0218L1.48558 14.0076L7.69077 20.2128C8.21147 20.7335 9.05569 20.7335 9.57639 20.2128C10.0971 19.6921 10.0971 18.8479 9.57639 18.3272L3.3712 12.122L9.21707 6.27614C9.73777 5.75544 9.73777 4.91122 9.21707 4.39052C8.69637 3.86983 7.85215 3.86983 7.33145 4.39052L1.48558 10.2364L1.4714 10.2222L0.292893 11.4007C0.0940267 11.5996 -0.00357093 11.8614 9.98538e-05 12.122Z"
          fill="#3D4150"
        />
      </g>
      <defs>
        <clipPath id="clip0_404_5508">
          <rect width="24" height="24" fill="white" />
        </clipPath>
      </defs>
    </svg>
  );
}

export default BackIcon;
