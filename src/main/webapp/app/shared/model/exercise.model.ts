import { ICourse } from 'app/shared/model/course.model';
import { ICorrection } from 'app/shared/model/correction.model';

export interface IExercise {
  id?: number;
  title?: string;
  content?: string;
  mark?: number;
  course?: ICourse;
  correction?: ICorrection | null;
}

export const defaultValue: Readonly<IExercise> = {};
