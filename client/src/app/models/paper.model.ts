import { Student } from './student.model';

export interface Paper {
  id: number,
  status: number,
  vote: number,
  lastUpdateTime: Date
  student: Student
}