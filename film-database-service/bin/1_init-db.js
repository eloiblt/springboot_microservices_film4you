db = db.getSiblingDB('film-db');

db.films.createIndex({
        title: "text",
        description: "text",
        writer: "text",
        director: "text",
        actors: "text"
    },
    {
        "weights": {
            "title": 3,
            "description": 2
        },
        "default_language": "none",
        "language_override": "none"
    }
);


db.createUser({
    'user': "mongo",
    'pwd': "root",
    'roles': [{
        'role': 'dbOwner',
        'db': 'film-db'
    }]
});
