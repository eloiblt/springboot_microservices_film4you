from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
import matplotlib.pyplot as plt

N_TEXT_MINING_CLUSTER = 25


def find_cluster_number(X):
    sum_of_squared_distances = []


    K = range(1, 20, 1)
    for k in K:
        print("[+] Calculating squared distance for", k, "clusters\n")
        km = KMeans(n_clusters=k, init='k-means++', max_iter=100, n_init=1)
        km = km.fit(X)
        sum_of_squared_distances.append(km.inertia_)
        print(km.inertia_)

    print(sum_of_squared_distances)

    plt.xlabel('k')
    plt.ylabel('Sum_of_squared_distances')
    plt.title('Elbow Method For Optimal k')
    plt.plot(K, sum_of_squared_distances, 'bx-')
    plt.show()



def extract_text_feature(dataset):
    print("[+] Starting text mining enrichment with", N_TEXT_MINING_CLUSTER, "clusters\n")

    vectorizer = TfidfVectorizer(stop_words='english')
    # we vectorize the description
    X = vectorizer.fit_transform(dataset['description'])
    model = KMeans(n_clusters=N_TEXT_MINING_CLUSTER, init='k-means++', max_iter=100, n_init=1)

    #find_cluster_number(X)

    model.fit(X)

    # Save text mining related datas to the dataset
    dataset.loc[:, 'textClusterId'] = model.labels_

    print("Top terms for the first 3 clusters:")

    order_centroids = model.cluster_centers_.argsort()[:, ::-1]
    terms = vectorizer.get_feature_names()
    for i in range(3):
        print("Cluster %d:" % i),
        for ind in order_centroids[i, :10]:
            print(' %s' % terms[ind]),
        print()
    print("\n")

    print("End of text mining enrichment...\n")

    return dataset


