FROM node:14-alpine AS development
ENV NODE_ENV development
WORKDIR /app
COPY food-diary/package.json .
RUN npm install
COPY food-diary .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]


FROM node:14-alpine AS production
ENV NODE_ENV production
WORKDIR /app
COPY food-diary/package.json .
RUN npm install --production
COPY food-diary .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]