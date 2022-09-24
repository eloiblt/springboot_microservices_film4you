export interface Film {
  id?: string;
  title?: string;
  year?: number;
  genre?: string;
  duration?: number;
  director?: string[];
  writer?: string[];
  productionCompany?: string[];
  actors?: string[];
  description?: string;
  avgVote?: number;
  imgUrl?: string;
  userNote?: number;
}
