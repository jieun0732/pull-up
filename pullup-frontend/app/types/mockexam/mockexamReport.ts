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