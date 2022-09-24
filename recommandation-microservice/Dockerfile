# Use official node image as the base image
FROM node:16.7.0-slim as node-build

ENV HOME_PATH /opt/app
# Set the working directory
WORKDIR $HOME_PATH

COPY package-lock.json ./package-lock.json
COPY package.json ./package.json

RUN npm ci

# Add the source code to app
COPY ./ $HOME_PATH

EXPOSE 8080

# Generate the build of the application
ENTRYPOINT [ "npm", "start" ]
