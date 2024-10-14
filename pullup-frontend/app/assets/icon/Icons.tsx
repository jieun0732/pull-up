"use client";
import { IconProp } from "@/types/IconProp";

export function ArrowIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="10"
      height="8"
      viewBox="0 0 10 8"
      fill="none"
    >
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M5.03944 0.273312C5.33233 -0.019581 5.80721 -0.019581 6.1001 0.273312L9.67099 3.8442C9.7491 3.92231 9.7491 4.04894 9.67099 4.12704L8.7569 5.04113C8.75406 5.04398 8.74945 5.04398 8.7466 5.04113C8.74376 5.03829 8.73915 5.03829 8.73631 5.04113L6.05076 7.72668C5.75787 8.01957 5.28299 8.01957 4.9901 7.72668C4.69721 7.43379 4.69721 6.95891 4.9901 6.66602L7.10248 4.55364H0.570432C0.404746 4.55364 0.270432 4.41933 0.270432 4.25364V3.35364C0.270432 3.18796 0.404746 3.05364 0.570432 3.05364H6.75911L5.03944 1.33397C4.74655 1.04108 4.74655 0.566205 5.03944 0.273312Z"
        fill="#4D70EC"
      />
    </svg>
  );
}

export function BackIcon({ onClick }: IconProp) {
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
      <g clipPath="url(#clip0_404_5508)">
        <path
          fillRule="evenodd"
          clipRule="evenodd"
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

export function CloseIcon({ onClick }: IconProp) {
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

export function DepthIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="21"
      height="20"
      viewBox="0 0 21 20"
      fill="none"
    >
      <rect width="20" height="20" transform="translate(0.6521)" fill="white" />
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M14.565 10.4885C14.7176 10.1848 14.6697 9.80271 14.4212 9.54825L13.6479 8.75629L13.6377 8.76669L9.25852 4.28209C8.89103 3.90576 8.29522 3.90576 7.92773 4.28209C7.56025 4.65843 7.56025 5.26858 7.92773 5.64491L12.3069 10.1295L8.1813 14.3544C7.81381 14.7308 7.81381 15.3409 8.1813 15.7173C8.54879 16.0936 9.1446 16.0936 9.51209 15.7173L13.6377 11.4923L13.6479 11.5028L14.3982 10.7344C14.4698 10.6611 14.5254 10.5776 14.565 10.4885Z"
        fill="#3D4150"
      />
    </svg>
  );
}

interface IconPropType {
  isClicked: boolean;
}

export function ProfileIcon({ isClicked }: IconPropType) {
  return (
    <svg
      width="23"
      height="22"
      viewBox="0 0 23 22"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M0.725111 19.5533L0.725112 16.3893C0.725113 14.2282 2.47697 12.4764 4.63798 12.4764L14.139 12.4764L18.3622 12.4764C20.5233 12.4764 22.2752 14.2283 22.2752 16.3894L22.2752 19.5533C22.2752 20.6561 21.3812 21.5501 20.2784 21.5501L2.72197 21.5501C1.61914 21.5501 0.725111 20.6561 0.725111 19.5533Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
      <circle
        cx="11.5005"
        cy="5.10398"
        r="5.10398"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
    </svg>
  );
}

export function SectionalIcon({ isClicked }: IconPropType) {
  return (
    <svg
      width="23"
      height="22"
      viewBox="0 0 23 22"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M0.000173561 1.89257L0.000174625 6.52924C0.000175095 8.57733 1.66048 10.2376 3.70857 10.2376L6.37255 10.2376L9.10226 10.2376C9.72938 10.2376 10.2378 9.72925 10.2378 9.10213L10.2378 1.89257C10.2378 0.847361 9.39045 5.50004e-05 8.34525 5.50352e-05L1.89268 5.52501e-05C0.84748 5.52849e-05 0.000173321 0.847363 0.000173561 1.89257Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
      <path
        d="M22.0626 1.89257L22.0626 6.52924C22.0626 8.57734 20.4023 10.2376 18.3542 10.2376L15.6902 10.2376L12.9605 10.2376C12.3334 10.2376 11.825 9.72926 11.825 9.10213L11.825 1.89257C11.825 0.847362 12.6723 5.49847e-05 13.7175 5.50213e-05L20.1701 5.52469e-05C21.2153 5.52835e-05 22.0626 0.847363 22.0626 1.89257Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
      <path
        d="M0.000173561 19.6577L0.000174625 15.021C0.000175095 12.9729 1.66048 11.3126 3.70857 11.3126L6.37255 11.3126L9.10226 11.3126C9.72938 11.3126 10.2378 11.821 10.2378 12.4481L10.2378 19.6577C10.2378 20.7029 9.39045 21.5502 8.34525 21.5502L1.89268 21.5502C0.84748 21.5502 0.000173321 20.7029 0.000173561 19.6577Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
      <path
        d="M22.0626 19.6577L22.0626 15.021C22.0626 12.9729 20.4023 11.3126 18.3542 11.3126L15.6902 11.3126L12.9605 11.3126C12.3334 11.3126 11.825 11.821 11.825 12.4481L11.825 19.6577C11.825 20.7029 12.6723 21.5502 13.7175 21.5502L20.1701 21.5502C21.2153 21.5502 22.0626 20.7029 22.0626 19.6577Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
    </svg>
  );
}

export function ToggleIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="17"
      height="17"
      viewBox="0 0 17 17"
      fill="none"
    >
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M7.55317 11.1337C7.85931 11.2915 8.24465 11.2422 8.50125 10.9856L9.29933 10.1875L9.28908 10.1772L13.8085 5.65779C14.1878 5.27855 14.1878 4.66366 13.8085 4.28441C13.4293 3.90517 12.8144 3.90517 12.4352 4.28441L7.9157 8.80386L3.65782 4.54598C3.27857 4.16673 2.66368 4.16673 2.28444 4.54598C1.90519 4.92523 1.90519 5.54011 2.28444 5.91936L6.54232 10.1772L6.53192 10.1876L7.30622 10.9619C7.37987 11.0356 7.46369 11.0928 7.55317 11.1337Z"
        fill="#3D4150"
      />
    </svg>
  );
}

interface ReplayIconProps {
  className?: string;
  children?: React.ReactNode;
  onClick?: (event: React.MouseEvent<HTMLDivElement>) => void;
}

export function ReplayIcon({ onClick, className, children }: ReplayIconProps) {
  return (
    <div onClick={onClick} className={className}>
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="16"
        height="15"
        viewBox="0 0 16 15"
        fill="none"
      >
        <path
          fillRule="evenodd"
          clipRule="evenodd"
          d="M4.48906 2.87812L5.27387 4.53385C5.45129 4.90815 5.89854 5.06775 6.27283 4.89033C6.64713 4.71292 6.80673 4.26567 6.62931 3.89137L5.04234 0.54332C4.81761 0.0692144 4.2511 -0.13295 3.77699 0.0917763L0.428937 1.67875C0.0546421 1.85617 -0.10496 2.30341 0.0724549 2.67771C0.24987 3.052 0.69712 3.21161 1.07141 3.03419L2.29733 2.45311C1.73374 3.11471 1.28576 3.87354 0.978442 4.69855C0.410464 6.22328 0.353739 7.89138 0.816825 9.45118C1.27991 11.011 2.23769 12.3779 3.54567 13.3457C4.85364 14.3135 6.44088 14.8296 8.06791 14.8164C9.69495 14.8031 11.2735 14.261 12.5655 13.272C13.8575 12.283 14.7928 10.9006 15.2304 9.33344C15.6679 7.76628 15.584 6.09934 14.9912 4.58408C14.5153 3.36769 13.7322 2.30202 12.7254 1.48707C12.314 1.15406 11.7188 1.29845 11.4409 1.74894C11.163 2.19943 11.31 2.78439 11.7064 3.13513C12.3661 3.71886 12.8819 4.45365 13.2062 5.28242C13.6475 6.41043 13.71 7.65136 13.3842 8.81801C13.0585 9.98465 12.3622 11.0137 11.4004 11.75C10.4386 12.4863 9.26348 12.8898 8.05226 12.8997C6.84104 12.9096 5.65945 12.5253 4.68574 11.8048C3.71204 11.0844 2.99904 10.0668 2.6543 8.90566C2.30956 7.74449 2.35179 6.5027 2.77461 5.36763C3.08527 4.53367 3.589 3.79055 4.2391 3.19613C4.34045 3.10346 4.42476 2.99529 4.48906 2.87812Z"
          fill="#ACACAC"
        />
      </svg>
      {children}
    </div>
  );
}

export function WarningIcon() {
  return (
    <svg
      width="61"
      height="60"
      viewBox="0 0 61 60"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <circle
        cx="30.5"
        cy="30"
        r="28.5"
        fill="white"
        stroke="#4D70EC"
        strokeWidth="3"
      />
      <path
        d="M33.915 36.2881H27.6309L26.9717 14.1836H34.5742L33.915 36.2881ZM30.751 46.4395C30.0332 46.4395 29.3667 46.2637 28.7515 45.9121C28.1362 45.5605 27.6528 45.0771 27.3013 44.4619C26.9497 43.8467 26.7812 43.1729 26.7959 42.4404C26.7812 41.7373 26.9497 41.0854 27.3013 40.4849C27.6528 39.8843 28.1362 39.4082 28.7515 39.0566C29.3667 38.7051 30.0332 38.5293 30.751 38.5293C31.4395 38.5293 32.084 38.7051 32.6846 39.0566C33.2998 39.4082 33.7905 39.8843 34.1567 40.4849C34.5376 41.0854 34.7354 41.7373 34.75 42.4404C34.7354 43.1729 34.5449 43.8467 34.1787 44.4619C33.8125 45.0625 33.3218 45.5459 32.7065 45.9121C32.0913 46.2637 31.4395 46.4395 30.751 46.4395Z"
        fill="#4D70EC"
      />
    </svg>
  );
}

export function MockTestIcon({ isClicked }: IconPropType) {
  return (
    <svg
      width="22"
      height="22"
      viewBox="0 0 22 22"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M0 1.13908L2.24578e-06 19.7967C2.24578e-06 20.8424 0.847676 21.6901 1.89333 21.6901H6.39997H9.14283C9.77193 21.6901 10.2819 21.1801 10.2819 20.551L10.2819 1.13908C10.2819 0.509983 9.77193 -4.39977e-08 9.14283 0L1.13908 5.59768e-07C0.509982 6.03766e-07 -6.87464e-08 0.509984 0 1.13908ZM8.14584 6.07335C8.14584 6.54269 7.76536 6.92316 7.29602 6.92316H3.12652C2.65718 6.92316 2.2767 6.54269 2.2767 6.07335C2.2767 5.604 2.65718 5.22353 3.12652 5.22353H7.29602C7.76536 5.22353 8.14584 5.604 8.14584 6.07335ZM7.29602 10.3935C7.76536 10.3935 8.14584 10.0131 8.14584 9.54373C8.14584 9.07438 7.76536 8.69391 7.29602 8.69391H3.12652C2.65718 8.69391 2.2767 9.07438 2.2767 9.54373C2.2767 10.0131 2.65718 10.3935 3.12652 10.3935H7.29602Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M21.5498 1.13908L21.5498 19.7967C21.5498 20.8424 20.7021 21.6901 19.6564 21.6901H15.2467H12.5626C11.9335 21.6901 11.4235 21.1801 11.4235 20.551V1.13908C11.4235 0.509983 11.9335 -4.89617e-08 12.5626 0L20.4107 6.10808e-07C21.0398 6.59769e-07 21.5498 0.509984 21.5498 1.13908ZM13.5142 6.07335C13.5142 6.54269 13.8947 6.92316 14.364 6.92316H18.4704C18.9397 6.92316 19.3202 6.54269 19.3202 6.07335C19.3202 5.604 18.9397 5.22353 18.4704 5.22353H14.364C13.8947 5.22353 13.5142 5.604 13.5142 6.07335ZM14.364 10.3935C13.8947 10.3935 13.5142 10.0131 13.5142 9.54373C13.5142 9.07438 13.8947 8.69391 14.364 8.69391H18.4704C18.9397 8.69391 19.3202 9.07438 19.3202 9.54373C19.3202 10.0131 18.9397 10.3935 18.4704 10.3935H14.364Z"
        fill={isClicked ? "#4d70ec" : "#D9D9D9"}
      />
    </svg>
  );
}

export default MockTestIcon;
