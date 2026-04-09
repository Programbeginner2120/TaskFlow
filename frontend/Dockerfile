# ---- Dev Stage (hot reload via ng serve + volume-mounted source) ----
FROM node:22-alpine AS dev
WORKDIR /app

COPY package*.json ./
RUN npm ci

EXPOSE 4200

# Source is volume-mounted at runtime.
# --host 0.0.0.0 makes the dev server accessible outside the container.
# --poll 2000 enables polling for file changes (required on Windows volume mounts).
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--poll", "2000"]

# ---- Build Stage ----
FROM node:22-alpine AS builder
WORKDIR /app

# NG_CONFIG controls which Angular build configuration is used.
# Defaults to 'production'; override with --build-arg NG_CONFIG=qa in docker-compose.
ARG NG_CONFIG=production

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build -- --configuration=${NG_CONFIG}

# ---- Runtime Stage ----
FROM nginx:alpine AS runtime

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy built Angular output
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
