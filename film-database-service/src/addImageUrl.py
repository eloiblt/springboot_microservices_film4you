import requests
import csv

def getImg(id):
    url = "https://api.themoviedb.org/3/find/" + id + "?api_key=1b8d877a8ee8b1b1b361f66f6e41c09e&external_source=imdb_id"
    r = requests.get(url)

    try:
        posterPath = r.json()['movie_results'][0]['poster_path']
        if not posterPath:
            raise Exception()
    except:
        print("error")
        return "https://www.teahub.io/photos/full/3-37351_sea-wallpaper-hd-portrait.jpg"

    return 'https://image.tmdb.org/t/p/original' + posterPath;

with open('movies_with_img_url.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)

    with open('movies.csv', 'r', encoding='UTF8') as file:
        reader = csv.reader(file)
        index = 0
        for row in reader:
            print(str(index) + "/85857 : " + str(round(index / 85857 * 100, 3)) + "%")
            if index == 0:
                row.append("img_url")
            else:
                row.append(getImg(row[0]))

            writer.writerow(row)
            index += 1
