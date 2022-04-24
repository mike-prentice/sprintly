import { IUser } from 'app/shared/model/user.model';
import { IRanking } from 'app/shared/model/ranking.model';

export interface IStats {
  id?: number;
  distance?: number | null;
  time?: string | null;
  avgpace?: string | null;
  user?: IUser | null;
  ranking?: IRanking | null;
}

export const defaultValue: Readonly<IStats> = {};
