"use client";

import { FC, ReactNode } from "react";
import { createPortal } from "react-dom";

interface ModalProps {
  open: boolean;
  onClose: () => void;
  children: ReactNode;
  [key: string]: any;
}

const Modal: FC<ModalProps> = ({ open, onClose, children, ...props }) => {
  if (!open) return null;

  return createPortal(
    <div
      className="z-1000 fixed left-0 top-0 flex h-full w-full items-center justify-center bg-black bg-opacity-50"
      onClick={onClose}
    >
      <div
        className="max-w-90pc max-h-90pc overflow-auto rounded-md bg-white p-5 shadow-md"
        onClick={(e) => e.stopPropagation()}
        {...props}
      >
        {children}
      </div>
    </div>,
    document.body,
  );
};

export default Modal;
