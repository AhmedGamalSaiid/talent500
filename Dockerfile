FROM node:14.19-alpine3.14 as builder

ARG REACT_APP_API_BASE_URL=https://ats-dev.talent500.co/atsbackend/api/v1/
ARG REACT_APP_ENVIRONMENT=development
ENV ENVIRONMENT=DEVELOPMENT
ENV NODE_PATH=/node_modules
ENV PATH=$PATH:/node_modules/.bin

ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL
ENV REACT_APP_ENVIRONMENT=$REACT_APP_ENVIRONMENT

WORKDIR '/app'
ADD yarn.lock /yarn.lock
ADD package.json /package.json

RUN yarn install --silent
COPY . .
RUN yarn build

FROM nginx:alpine
EXPOSE 80
COPY --from=builder /app/build/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
RUN touch /usr/share/nginx/html/health.txt

