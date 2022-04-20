import { IExercise } from 'app/shared/model/exercise.model';

export interface ICourse {
  id?: number;
  title?: string;
  content?: string;
  exercises?: IExercise[] | null;
}

export const defaultValue: Readonly<ICourse> = {};
