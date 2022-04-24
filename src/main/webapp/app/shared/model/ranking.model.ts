import { IUser } from 'app/shared/model/user.model';
import { IStats } from 'app/shared/model/stats.model';

export interface IRanking {
  id?: number;
  avgpace?: number | null;
  rank?: number | null;
  user?: IUser | null;
  stats?: IStats[] | null;
}

export const defaultValue: Readonly<IRanking> = {};
