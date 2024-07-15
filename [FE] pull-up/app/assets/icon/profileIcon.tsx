interface IconPropType {
  isClicked: boolean;
}

function ProfileIcon({ isClicked }: IconPropType) {
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

export default ProfileIcon;
