import { IExercise } from 'app/shared/model/exercise.model';

export interface ICorrection {
  id?: number;
  content?: string;
  exercise?: IExercise;
}

export const defaultValue: Readonly<ICorrection> = {};
