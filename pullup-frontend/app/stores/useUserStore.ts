import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { UserLogin } from "@/types/userType";

interface UserStore {
  user: UserLogin;
  setUser: (user: UserLogin) => void;
  updateUser: (key: keyof UserLogin, value: string | number) => void;
  resetUserData: () => void;
}

const useUserStore = create<UserStore>()(
  persist(
    (set) => ({
      user: {
        memberId: 99999999,
        name: "test user",
        email: "test@example.com",
        snsProvider: "apple",
      },
      setUser: (user) => {
        set({ user });
      },
      updateUser: (key, value) => {
        set((state) => ({
          user: { ...state.user, [key]: value },
        }));
      },
      resetUserData: () => {
        const defaultUser = {
          memberId: 99999999,
          name: "test user",
          email: "test@example.com",
          snsProvider: "apple",
        };
        set({ user: defaultUser });
      },
    }),
    {
      name: "user-storage", // 로컬 스토리지에 저장할 이름
      storage: createJSONStorage(() => localStorage), // 로컬 스토리지 사용
    },
  ),
);

export default useUserStore;
