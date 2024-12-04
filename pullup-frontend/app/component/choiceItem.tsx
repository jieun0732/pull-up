import { SetUnderline } from "@/utils/SetUnderline";
import { API } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";
import { ProblemBeingSolved } from "@/types/mockexam/mockexamQuestion";
import { Dispatch, SetStateAction } from "react";
import useExamStore from "@/stores/useExamStore";

interface ChoiceItemProps {
  idx: number;
  choice: string;
  isSelected: boolean;
  selectedId: number | null;
  setSelectedId?: Dispatch<SetStateAction<number | null>>;
  problemNumber: number;
}

const ChoiceItem = ({
  idx,
  choice,
  isSelected,
  selectedId,
  setSelectedId,
  problemNumber,
}: ChoiceItemProps) => {
  const { updateSelectedAnswer } = useExamStore();

  const handleClick = () => {
    if (selectedId === idx) {
      if (setSelectedId) {
        setSelectedId(null);
      }
      updateSelectedAnswer(problemNumber, null);
    } else {
      if (setSelectedId) {
        setSelectedId(idx);
      }
      updateSelectedAnswer(problemNumber, idx + 1);
    }
  };

  return (
    <div
      className={`flex w-full items-center gap-4 px-5 py-4 ${
        isSelected ? "bg-[#ebebeb]" : "bg-white"
      }`}
      onClick={handleClick}
    >
      <div
        className={`flex h-10 w-10 items-center justify-center rounded-full border border-solid border-black01 ${
          isSelected ? "bg-black01 text-white" : "bg-white text-black01"
        }`}
      >
        {idx + 1}
      </div>
      <div className="min-h-[40px] min-w-0 flex-1">{SetUnderline(choice)}</div>
    </div>
  );
};

export default ChoiceItem;
