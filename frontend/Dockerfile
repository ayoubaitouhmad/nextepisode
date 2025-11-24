# ====== Build stage ======
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# Nom du projet Angular (doit matcher angular.json)
ARG APP_NAME=next-episode
# Build de prod Angular
RUN npm run build
# Normaliser le dossier dist (Angular 16/17 peuvent produire /dist/<app>/browser)
RUN mkdir -p /dist-serve \
&& if [ -d "/app/dist/${APP_NAME}/browser" ]; then \
cp -r "/app/dist/${APP_NAME}/browser/"* /dist-serve/; \
else \
cp -r "/app/dist/${APP_NAME}/"* /dist-serve/; \
fi

# ====== Runtime stage (nginx) ======
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /dist-serve /usr/share/nginx/html
EXPOSE 80
