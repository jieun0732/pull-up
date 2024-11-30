export interface UserLogin {
  memberId: number;
  name: string;
  email: string;
  snsProvider: string;
}

export interface User {
  memberId: number;
  name: string;
  email: string;
  snsProvider: string;
  mockExamSolved: boolean;
  mockExamScore: number;
}

export interface UserLoginStatus {
  firstLogin: boolean;
  memberId: number;
  provider: "apple" | "kakao";
  name: string;
  email: string;
}

export interface authType {
  code: string;
  state: string;
  user: {
    email: string;
    firstName: string;
    lastName: string;
  };
}
