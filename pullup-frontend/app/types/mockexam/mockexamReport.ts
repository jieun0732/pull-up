export interface MockExamAverageType {
  examId: number;
  rankPercent: string;
  averageScore: number;
  score: number;
}

export interface MockExamTimeAverageType {
  averageTime: number;
  requiredTime: string;
}  

export interface ProblemTypeResult {
  entry: string;
  totalProblems: number;
  correctProblems: number;
}

export interface MockExamWeakPartType {
  problemTypeResults: ProblemTypeResult[];
} 

// 추가된 타입 정의
export interface ProblemTypeResult {
  entry: string;
  correctProblems: number;
  totalProblems: number;
  weakest?: boolean;
  errorRate?: number;
}