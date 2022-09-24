import pandas as pd
import numpy as np
import requests
from sqlalchemy import create_engine

from sklearn.cluster import KMeans
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import MinMaxScaler

from text_mining import extract_text_feature
from scipy.spatial import distance_matrix


CSV_PATH = r'data/movies-cleaned_old.csv'
COLUMNS = ['id', 'title', 'year', 'genre', 'duration', 'country', 'language', 'director', 'writer',
               'production_company', 'actors', 'description', 'avg_vote', 'votes']

weight_vector = [1,2,3,0.5,1,0.5,2,0.1,0.3,2,1,0.1]

COLUMNS_JSON = ['id', 'title', 'year', 'genre', 'duration', 'country', 'language', 'director', 'writer',
                'productionCompany', 'actors', 'description', 'avgVote', 'votes']
COLUMNS_CSV = ['imdb_title_id', 'title', 'year', 'genre', 'duration', 'country', 'language', 'director', 'writer',
                'production_company', 'actors', 'description', 'avg_vote', 'votes']

N_CLUSTER = 10
N_TEXT_MINING_CLUSTER = 10
POSTGRES_URL = 'localhost:6003'
POSTGRES_DATABASE_NAME = 'recommandation-db'
FILMS_API_URL = 'http://localhost:51815/private/get-all'

postgres_engine = create_engine('postgresql://postgres:root@' + POSTGRES_URL + '/' + POSTGRES_DATABASE_NAME, echo=False)


def get_films_from_api():
    print('[+] Load api data ...')

    r = requests.get(FILMS_API_URL)
    print('[+] Retrieving informations from the server ...')
    try:
        df_json = r.json()

    except requests.exceptions.RequestException as e:  # This is the correct syntax
        raise SystemExit(e)

    df_json = pd.DataFrame.from_records(df_json, columns=COLUMNS_JSON)

    df = df_json.set_axis(COLUMNS, axis=1)
    return df


def get_films_from_csv():
    print('[+] Load csv data ...')

    df_csv = pd.read_csv(CSV_PATH, usecols=COLUMNS_CSV)
    df = df_csv.set_axis(COLUMNS, axis=1)

    return df


def write_on_database(df):
    print('[+] Writing data on database ...')

    df = df.set_axis(['film_id', 'cluster_id', 'mark', 'distance'], axis=1)
    pd.DataFrame.to_sql(df, 'recommandation', con=postgres_engine, if_exists='replace')
    return 1


if __name__ == "__main__":
    df = get_films_from_api()

    print(df)


    print('[+] Get first element of actor, director and writer ...')
    df['director'] = df['director'].str[0]
    df['writer'] = df['writer'].str[0]
    df['actors'] = df['actors'].str[0]
    df['production_company'] = df['production_company'].str[0]

    # extract text mining features
    df = extract_text_feature(df)

    print('[+] Encording labels ...')
    # Non numeric label encoding
    labelEncoder = LabelEncoder()

    labelEncoder.fit(df['genre'])
    df['genre'] = labelEncoder.transform(df['genre'])
    labelEncoder.fit(df['country'])
    df['country'] = labelEncoder.transform(df['country'])
    labelEncoder.fit(df['language'])
    df['language'] = labelEncoder.transform(df['language'])
    labelEncoder.fit(df['director'])
    df['director'] = labelEncoder.transform(df['director'])
    labelEncoder.fit(df['writer'])
    df['writer'] = labelEncoder.transform(df['writer'])
    labelEncoder.fit(df['production_company'])
    df['production_company'] = labelEncoder.transform(df['production_company'])
    labelEncoder.fit(df['actors'])
    df['actors'] = labelEncoder.transform(df['actors'])


    # We drop text mining related data that are already used on the related function
    X = np.array(df.drop(['id', 'description', 'title'], 1))

    # We scale all the features
    scaler = MinMaxScaler()
    X_scaled = scaler.fit_transform(X)
    X_weighted = X_scaled * weight_vector

    #find_cluster_number(X_weighted)

    kmeans = KMeans(n_clusters=6, max_iter=600, algorithm='auto')
    kmeans.fit(X_weighted)

    centroids = kmeans.cluster_centers_


    dist_mat = pd.DataFrame(distance_matrix(X_weighted, centroids))
    dist_mat = dist_mat.min(axis = 1)
    print(dist_mat)

    df.loc[:, 'cluster_id'] = kmeans.labels_
    df.loc[:, 'distance'] = dist_mat

    print(df)

    dfToWrite = df[['id', 'cluster_id', 'avg_vote', 'distance']]

    write_on_database(dfToWrite)

    #df.to_csv('data/movies-enriched.csv', sep=',')
