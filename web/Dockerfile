# Use official node image as the base image
FROM node:16.10.0-slim as film4u-front-build

ENV FILM4U_FRONT_HOME /opt/film4u-front

# Set the working directory
WORKDIR $FILM4U_FRONT_HOME

COPY package-lock.json $FILM4U_FRONT_HOME/package-lock.json
COPY package.json $FILM4U_FRONT_HOME/package.json

RUN npm ci

# Add the source code to app
COPY ./ $FILM4U_FRONT_HOME/

# Generate the build of the application
RUN npm run build

# Stage 2: Serve app with nginx server

# Use official nginx image as the base image
FROM nginx:1.21.5-alpine as film4u-front-run

ENV FILM4U_FRONT_HOME /opt/film4u-front
ENV APP_NAME film4u-front
# Set the working directory
WORKDIR $FILM4U_FRONT_HOME

COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the build output to replace the default nginx contents.
COPY --from=film4u-front-build $FILM4U_FRONT_HOME/dist/web /usr/share/nginx/html

# Expose port 80
EXPOSE 80
