export interface User {
  status: number;
  message: string;
  data: userStatus;
}

export interface userStatus {
  id: number;
  name: string;
  email: string;
  accessCheck: boolean;
  role: string;
  latestScore: number;
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
