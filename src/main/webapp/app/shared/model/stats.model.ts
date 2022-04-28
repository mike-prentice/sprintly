import { IUser } from 'app/shared/model/user.model';
import { IRanking } from 'app/shared/model/ranking.model';

export interface IStats {
  id?: number;
  distance?: number | null;
  time?: number | null;
  avgpace?: number | null;
  user?: IUser | null;
  ranking?: IRanking | null;
}

export const defaultValue: Readonly<IStats> = {};
