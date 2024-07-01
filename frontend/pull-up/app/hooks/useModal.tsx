import {
  useState,
  useCallback,
  ReactNode,
  SetStateAction,
  Dispatch,
} from "react";
import { createPortal } from "react-dom";

interface UseModalProps {
  initialOpen?: boolean;
}

interface UseModalReturn {
  open: boolean;
  openModal: () => void;
  closeModal: () => void;
  Modal: React.FC<{ children: ReactNode }>;
}

const useModal = ({
  initialOpen = false,
}: UseModalProps = {}): UseModalReturn => {
  const [open, setOpen] = useState(initialOpen);

  const closeModal = useCallback(() => {
    setOpen(false);
  }, []);

  const openModal = useCallback(() => {
    setOpen(true);
  }, []);

  const Modal: React.FC<{ children: ReactNode }> = ({ children }) => {
    if (!open) return null;

    return createPortal(
      <div
        className="z-1000 fixed left-0 top-0 flex h-full w-full items-center justify-center bg-black bg-opacity-50"
        onClick={closeModal}
      >
        <div
          className="max-w-90pc max-h-90pc overflow-auto rounded-md bg-white p-5 shadow-md"
          onClick={(e) => e.stopPropagation()}
        >
          {children}
        </div>
      </div>,
      document.body,
    );
  };

  return { open, openModal, closeModal, Modal };
};

export default useModal;
