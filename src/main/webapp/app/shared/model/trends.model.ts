import { IUser } from 'app/shared/model/user.model';

export interface ITrends {
  id?: number;
  avgPace?: number | null;
  avgdistance?: number | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITrends> = {};
