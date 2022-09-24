# -*- coding: utf-8 -*-
import numpy as np
import pandas as pd
from currency_converter import CurrencyConverter

CSV_PATH = './film-database-service/data/movies.csv'

SUBSET_COLUMNS = ['imdb_title_id', 'original_title',
                    'genre', 'duration', 'country', 'language',
                     'director', 'writer',
                    'production_company', 'actors',
                    'description', 'avg_vote', 'votes']

COLUMNS = ['imdb_title_id', 'original_title', 'year', 'date_published',
           'genre', 'duration', 'country', 'language', 'director', 'writer',
           'production_company', 'actors', 'description', 'avg_vote', 'votes',
           'budget', 'metascore', 'reviews_from_users', 'reviews_from_critics',
           'img_url']

# Supported currencies of currency converter from ECB (European Central Bank)
# Last day currencies conversions rates to euro https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip
SUPPORTED_CURRENCIES = ['USD', 'JPY', 'BGN', 'CZK', 'DKK', 'GBP',
                        'HUF', 'PLN', 'RON', 'SEK', 'CHF', 'ISK',
                        'NOK', 'HRK', 'RUB', 'TRY', 'AUD', 'BRL',
                        'CAD', 'CNY', 'HKD', 'IDR', 'ILS', 'INR',
                        'KRW', 'MXN', 'MYR', 'NZD', 'PHP', 'SGD',
                        'THB', 'ZAR', 'EUR']

def stripArray(array):
    return list(map(lambda x: x.strip(), array))

if __name__ == "__main__":

    # Reading the csv with only the columns that interests us
    print('[+] Load csv data ...')
    df = pd.read_csv(CSV_PATH, usecols=COLUMNS)

    # Drop rows with null values on subset columns which are used for clustering (must be done after setting default values)
    print('[+] Drop if na ...')
    df.dropna(subset=SUBSET_COLUMNS, inplace=True)

    # DROPPING DUPLICATES OR NOT ASSIGNED VALUES
    # Drop duplicates by id if there is any
    print('[+] Drop duplicated entries ...')
    df.drop_duplicates(subset=['imdb_title_id'])

    # STRING FORMATTING #
    # Removing all possible characters in year and date_published for a datetime format
    print('[+] String formatting ...')
    df['year'] = df['year'].str.replace(r'[a-zA-Z]', '')
    df['date_published'] = df['date_published'].str.replace(r'[a-zA-Z]', '')

    # DEFAULT VALUES SETTING #
    # If no year is set, set the year from date_published
    print('[+] Default value for year ...')
    df['year'] = df['year'].fillna(df['date_published'].str.split('-').str[0])

    # RENAMING COLUMNS #
    print('[+] Rename original_title to title ...')
    df = df.rename(columns={"original_title": "title"})

    # DATA MAPPING WITH TYPES #
    print('[+] Map types ...')
    df = df.astype({
        "imdb_title_id": str,
        "title": str,
        "year": str,
        "genre": str,
        "duration": int,
        "country": str,
        "language": str,
        "director": str,
        "writer": str,
        "production_company": str,
        "actors": str,
        "description": str,
        "avg_vote": str,
        "img_url": str})
    # Convert date_published to datetime type (not using astype)
    print('[+] Convert date published ...')
    df['date_published'] = pd.to_datetime(df['date_published'])

    # STRING REPLACEMENT #
    # Removing string in imdb_title_id as it is not part of id
    print('[+] Replace imdb title id ...')
    df['imdb_title_id'] = df['imdb_title_id'].str.replace(r'[a-zA-Z]', '')

    # TO OPTIMIZE # Removing None values in languages
    print('[+] Remove None values in languages ...')
    df['language'] = df['language'].str.replace('None,', '')
    df['language'] = df['language'].str.replace('None', '')
    df['language'] = df['language'].apply(lambda x: '' if 'nan' in x else x)

    # TO OPTIMIZE # Get only first element of list in column
    print('[+] Get first element of genre, country and language ...')
    df['genre'] = df['genre'].str.split(',').str[0]
    df['country'] = df['country'].str.split(',').str[0]
    df['language'] = df['language'].str.split(',').str[0]

    print('[+] Create list for writer, director, production_company and actors ...')
    df['writer'] = df['writer'].str.split(',')
    df['director'] = df['director'].str.split(',')
    df['production_company'] = df['production_company'].str.split(',')
    df['actors'] = df['actors'].str.split(',')

    print('[+] Strip all item in list ...')
    df['writer'] = df['writer'].apply(lambda x: stripArray(x))
    df['director'] = df['director'].apply(lambda x: stripArray(x))
    df['production_company'] = df['production_company'].apply(lambda x: stripArray(x))
    df['actors'] = df['actors'].apply(lambda x: stripArray(x))


    # CURRENCY CONVERSION #
    # Split budget column into currency and budget column
    print('[+] Convert budget ...')
    df['currency'] = df['budget'].str.replace(r'[0-9 *]', '').astype("string")
    df['budget'] = df['budget'].str.replace(r'\D+', '').astype('float64')

    # Before any conversion renaming currency $ to USD
    df['currency'] = df['currency'].replace('$', 'USD')

    # Create currency converter
    cvt = CurrencyConverter()

    # Keep only supported currencies for converter
    df_cvt = df[df['currency'].isin(SUPPORTED_CURRENCIES)]

    # Remove na values of column currency for conversion
    df_cvt.dropna(subset=['currency'])

    # Converting the budget to budget_euro according to theur currency
    df_cvt['budget_euro'] = df_cvt.apply(lambda x:  cvt.convert(
        x['budget'], x['currency'], 'EUR'), axis=1).astype('float64')

    # Selecting the columns of df_cvt for concat
    df_cvt = df_cvt[['imdb_title_id', 'budget_euro']]

    # Updating original dataframe with currency conversion dataframe
    df = pd.merge(df, df_cvt, on='imdb_title_id', how='left')

    # EXPORT #
    # Exporting dataframe to csv
    print('Writing movies-cleaned files...')
    # df.to_csv('film-database-service/data/movies-cleaned.csv', sep=',')
    df.to_json('film-database-service/data/movies-cleaned.json', orient="records", date_format='iso')
