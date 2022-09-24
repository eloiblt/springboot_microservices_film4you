import { Film } from "./film.model";

export interface Watch {
  id?: number;
  userId?: number;
  filmId?: string;
  film?: Film;
}