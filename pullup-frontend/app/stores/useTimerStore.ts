import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";

interface TimeerState {
  timer: number;
  timerStatus: boolean;
  setTimer: (time: number) => void;
  setTimerStatus: (status: boolean) => void;
}

export const useTimerStore = create<TimeerState>()(
  persist(
    (set) => ({
      timer: 0,
      timerStatus: false,
      setTimer: (time: number) => set({ timer: time }),
      setTimerStatus: (status: boolean) => set({ timerStatus: status }),
    }),
    {
      name: "timer-storage",
      storage: createJSONStorage(() => localStorage),
    },
  ),
);
