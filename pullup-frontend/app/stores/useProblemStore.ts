import { create } from "zustand";

interface ProblemStore {
  examId: number;
  setExamId: (id: number) => void;
}

const useProblemStore = create<ProblemStore>((set) => ({
  examId: 0,
  setExamId: (id) => set({ examId: id }),
}));

export default useProblemStore;
