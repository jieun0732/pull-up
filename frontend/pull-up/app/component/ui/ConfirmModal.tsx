"use client";

import Button from "./Button";
import Text from "./Text";
import WarningIcon from "@/assets/icon/warningIcon";

interface ConfirmModalProps {
  onLeft: () => void;
  onRight: () => void;
  title: string;
  description: string;
  left: string;
  right: string;
}

export const ConfirmModal: React.FC<ConfirmModalProps> = ({
  onLeft,
  onRight,
  title,
  description,
  left,
  right,
}) => {
  return (
    <>
      <WarningIcon />
      <h2 className="mt-5 text-[19px] font-bold text-black01">{title}</h2>
      <p className="text-[15px] font-semibold text-gray02">{description}</p>
      <div className="mt-5 flex w-full gap-4">
        <Button size="medium" color="activeBlack" onClick={onLeft}>
          {left}
        </Button>
        <Button size="medium" color="active" onClick={onRight}>
          {right}
        </Button>
      </div>
    </>
  );
};
