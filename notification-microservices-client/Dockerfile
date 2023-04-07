FROM node:16-alpine as builder
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install && npm install axios
COPY . ./
CMD ["npm", "start"]
