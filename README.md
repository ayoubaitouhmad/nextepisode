# NextEpisode

A movie & TV-series discovery and recommendation web app. Helps users decide what to watch across multiple streaming platforms — with advanced filters, a hybrid recommendation engine, and per-country streaming availability.

> **Live demo:** _add link if deployed, otherwise remove this line_
> **Status:** In active development

---

## What it does

- **Search & filter** movies/series by genre, year, language, cast, runtime, and more
- **Recommendation engine** — hybrid approach (content-based + collaborative signals via TMDB metadata)
- **Where to watch** — shows availability per country (Netflix, Prime Video, Disney+, etc.) via TMDB Watch Providers API
- **User accounts** — favorites, watchlist, watched history, and personalized recommendations
- **Multilingual UI** — French, English, Arabic

---

## Architecture

NextEpisode is built as a small set of Spring Boot microservices behind an API Gateway, communicating via REST and event-driven messaging.

```
                                  ┌─────────────────┐
                                  │  Angular SPA    │
                                  │  (Tailwind CSS) │
                                  └────────┬────────┘
                                           │ HTTPS
                                           ▼
                                  ┌─────────────────┐
                                  │   API Gateway   │  ◄── JWT validation
                                  │  (Spring Cloud) │
                                  └────────┬────────┘
                                           │
                ┌──────────────────────────┼──────────────────────────┐
                ▼                          ▼                          ▼
        ┌──────────────┐          ┌──────────────┐          ┌──────────────┐
        │ Auth Service │          │ User Service │          │ TMDB Service │
        │  (JWT, RBAC) │          │ (favorites,  │          │  (cache +    │
        │              │ ◄──────► │  watchlist)  │ ◄──────► │  TMDB proxy) │
        └──────┬───────┘          └──────┬───────┘          └──────┬───────┘
               │                         │                         │
               └─────────────────────────┴─────────────────────────┘
                                         │
                                  ┌──────┴──────┐
                                  │  RabbitMQ   │  ◄── async events
                                  └─────────────┘
                                         │
                                   ┌─────┴─────┐
                                   │  MySQL    │
                                   └───────────┘
```

### Why microservices?
The TMDB integration is rate-limited and benefits from aggressive caching, while user-facing reads (favorites, watchlist) need different scaling characteristics than auth flows. Splitting these allowed independent caching strategies and decoupled deployment.

---

## Tech stack

**Backend**
- Java 17, Spring Boot 3
- Spring Cloud Gateway (API Gateway)
- Spring Security + JWT
- RabbitMQ for inter-service events
- MySQL with JPA/Hibernate
- Server-side caching layer

**Frontend**
- Angular 17
- Tailwind CSS
- RxJS for reactive state
- JWT-aware HTTP interceptor

**Infrastructure**
- Docker + Docker Compose for local dev
- GitHub Actions CI/CD
- Deployed via self-hosted VPS with Coolify

---

## Key engineering decisions

- **JWT in API Gateway, not per-service.** Auth validation happens once at the gateway. Downstream services trust the validated token. Simpler, faster, less duplication.
- **TMDB Service caches aggressively.** TMDB rate-limits at 40 requests / 10 seconds. Caching cuts upstream calls by ~85% and absorbs traffic spikes.
- **RabbitMQ for non-blocking writes.** Adding to watchlist returns 200 immediately; the cross-service update happens async. UX feels instant; eventual consistency is acceptable here.
- **Per-country availability.** Streaming rights vary by country, so the "Where to watch" feature uses the user's locale to query the right TMDB region endpoint.

---

## Local development

```bash
# Clone
git clone https://github.com/ayoubaitouhmad/nextepisode.git
cd nextepisode

# Start all services
docker compose up

# Frontend will be available at http://localhost:4200
# API Gateway at http://localhost:8080
```

You'll need a TMDB API key in `.env` — see `.env.example`.

---

## Screenshots

_Add 2-3 screenshots here showing: (1) the main browse view, (2) a movie detail page with "Where to watch", (3) the recommendations view. Drop them in `/docs/screenshots/` and reference them with `![Browse view](docs/screenshots/browse.png)`._

---

## About me

Full-stack developer based in Morocco. 4+ years of Laravel, plus Spring Boot, Angular, Docker, and production payment-gateway integrations (PayPal, CMI).

Available for freelance work — [reach me on Upwork](https://www.upwork.com/freelancers/) or [Malt](https://www.malt.fr/).
