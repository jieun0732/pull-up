import { create } from "zustand";

interface SelectedAnswer {
  problemNumber: number;
  submitAnswer: number | null; // selectedAnswer가 null일 수 있도록 설정
}

interface ExamStore {
  examId: number;
  selectedAnswers: SelectedAnswer[];
  isFinished: boolean;
  time: string;
  tutorialFinished: boolean;
  setExamId: (id: number) => void;
  updateSelectedAnswer: (
    problemNumber: number,
    selectedAnswer: number | null,
  ) => void;
  resetSelectedAnswers: () => void;
  setTime: (id: string) => void;
  setTutorialFinished: (finished: boolean) => void;
}

const useExamStore = create<ExamStore>((set) => ({
  examId: 0,
  selectedAnswers: Array.from({ length: 20 }, (_, index) => ({
    problemNumber: index + 1,
    submitAnswer: null,
  })),
  isFinished: false,
  tutorialFinished: false,
  time: "",
  setExamId: (id) => set({ examId: id }),
  updateSelectedAnswer: (problemNumber, submitAnswer) =>
    set((state) => {
      const updatedAnswers = state.selectedAnswers.map((answer) =>
        answer.problemNumber === problemNumber
          ? { ...answer, submitAnswer }
          : answer,
      );

      return {
        selectedAnswers: updatedAnswers,
        isFinished: updatedAnswers.every((ans) => ans.submitAnswer !== null),
      };
    }),
  resetSelectedAnswers: () =>
    set({
      selectedAnswers: Array.from({ length: 20 }, (_, index) => ({
        problemNumber: index + 1,
        submitAnswer: null,
      })),
      isFinished: false,
    }),
  setTime: (time: string) => set({ time }),
  setTutorialFinished: (finished: boolean) =>
    set({ tutorialFinished: finished }),
}));

export default useExamStore;
