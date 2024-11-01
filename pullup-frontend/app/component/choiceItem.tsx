import { FormatQuestion } from "@/utils/FormatQuestion";
import { API } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";

interface ChoiceItemProps {
  idx: number;
  choice: string;
  isSelected: boolean;
  selectedId: number | null;
  setSelectedId: (id: number | null) => void;
  type?: string;
  problemNumber?: number;
  paramsId?: string;
}

const ChoiceItem = ({
  idx,
  choice,
  isSelected,
  selectedId,
  setSelectedId,
  type,
  problemNumber,
  paramsId,
}: ChoiceItemProps) => {
  const handleClick = () => {
    if (selectedId === idx) {
      setSelectedId(null);
    } else {
      setSelectedId(idx);
    }
    if (paramsId && paramsId == "20") {
      handleFinalQuestion();
    }
  };

  const handleFinalQuestion = async () => {
    if (type == "mockexam" && selectedId) {
      try {
        const response = await fetch(`${API}/exams/mock-exam/answer`, {
          method: "POST",
          headers: {
            Accept: "*/*",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            examInformationId: Number(LocalStorage.getItem("examId")),
            problemNumber: Number(problemNumber),
            chosenAnswer: selectedId + 1,
          }),
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
      } catch (error) {
        console.error(error);
      }
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
      <div className="min-h-[40px] min-w-0 flex-1">
        {FormatQuestion(choice)}
      </div>
    </div>
  );
};

export default ChoiceItem;
